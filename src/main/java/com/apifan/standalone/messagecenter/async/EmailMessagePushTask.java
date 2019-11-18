package com.apifan.standalone.messagecenter.async;

import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.util.CallbackUtils;
import com.apifan.standalone.messagecenter.util.StringUtils;
import com.apifan.standalone.messagecenter.vo.EmailMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件推送任务（异步）
 *
 * @author yindz
 */
@Component
public class EmailMessagePushTask {
    private static final Logger logger = LoggerFactory.getLogger(EmailMessagePushTask.class);

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 异步推送
     */
    @Async("emailThreadPool")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void push(EmailMessage message) {
        try {
            String msg = sendEmail(message);
            if (MessageConstant.RESULT_OK.equalsIgnoreCase(msg)) {
                logger.info("邮件已成功发送到 {}, messageId={}", message.getReceiver(), message.getMessageId());
                CallbackUtils.executeCallback(message, true, null);
            } else {
                throw new RuntimeException("发送邮件失败");
            }
        } catch (MessagingException e) {
            logger.error("发送邮件出现异常", e);
            throw new RuntimeException("发送邮件出现异常");
        }
    }

    /**
     * 重试失败时的日志记录
     *
     * @param re
     */
    @Recover
    private void recover(Exception re, EmailMessage message) {
        logger.error("重试异常", re);
        CallbackUtils.executeCallback(message, false, re.getMessage());
    }

    /**
     * 发送邮件
     *
     * @param message 邮件
     * @return 返回码
     */
    private String sendEmail(EmailMessage message) throws MessagingException {
        if (message == null) {
            return null;
        }
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(message.getReceiver());
        helper.setSubject(message.getSubject());
        helper.setText(message.getContent(), message.isHtmlContent());
        if (CollectionUtils.isNotEmpty(message.getAttachmentList())) {
            //添加附件
            message.getAttachmentList().forEach(i -> {
                if (StringUtils.isNotEmpty(i)) {
                    File file = new File(i);
                    try {
                        if (file.exists()) {
                            helper.addAttachment(parseFileName(i), new File(i));
                        } else {
                            logger.warn("附件不存在: {}", i);
                        }
                    } catch (MessagingException e) {
                        logger.warn("无法添加附件: {}", i, e);
                    }
                }
            });
        }
        try {
            mailSender.send(mail);
            return MessageConstant.RESULT_OK;
        } catch (MailException e) {
            logger.error("发送邮件出现异常", e);
            return e.getMessage();
        }
    }

    /**
     * 解析文件名
     *
     * @param fullName 文件完整路径
     * @return 文件名(含扩展名)
     */
    private String parseFileName(String fullName) {
        return FilenameUtils.getName(fullName);
    }
}
