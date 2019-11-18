package com.apifan.standalone.messagecenter.listener;

import com.apifan.standalone.messagecenter.async.EmailMessagePushTask;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.vo.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息队列中的邮件消息并进行处理
 *
 * @author yindz
 */
@Component
public class EmailMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailMessageHandler.class);

    @Autowired
    private EmailMessagePushTask emailMessagePushTask;

    /**
     * 接收邮件队列的消息
     *
     * @param payload
     */
    @KafkaListener(id = "emailMessageHandler", groupId = MessageConstant.GROUP_ID, topics = MessageConstant.EMAIL_MSG_TOPIC)
    public void receive(String payload) {
        logger.info("接收到邮件消息: {}", payload);
        try {
            EmailMessage emailMessage = JsonUtils.readAsObject(payload, EmailMessage.class);
            emailMessagePushTask.push(emailMessage);
        } catch (Exception e) {
            logger.error("发送邮件时出现异常", e);
        }
    }
}
