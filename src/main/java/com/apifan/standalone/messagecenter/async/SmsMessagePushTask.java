package com.apifan.standalone.messagecenter.async;

import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.service.sms.ISmsMessageService;
import com.apifan.standalone.messagecenter.util.CallbackUtils;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 短信推送任务（异步）
 *
 * @author yindz
 */
@Component
public class SmsMessagePushTask {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessagePushTask.class);

    @Autowired
    private MessageProperties messageProperties;

    @Resource(name = "aliyunSmsMessageService")
    private ISmsMessageService aliyunSmsService;

    @Resource(name = "qcloudSmsMessageService")
    private ISmsMessageService qcloudSmsService;

    @Resource(name = "yunpianSmsMessageService")
    private ISmsMessageService yunpianSmsService;

    //限流器
    private RateLimiter rateLimiter;

    /**
     * 异步推送
     */
    @Async("smsThreadPool")
    public void push(SmsMessage message) {
        if (rateLimiter.tryAcquire(1, MessageConstant.SMS_WAIT_SECONDS, TimeUnit.SECONDS)) {
            String code = sendSms(message);
            if (MessageConstant.RESULT_OK.equalsIgnoreCase(code)) {
                logger.info("已成功发送短信到手机号 {}, messageId={}", message.getReceiver(), message.getMessageId());
                CallbackUtils.executeCallback(message, true, null);
            } else {
                logger.error("无法发送短信到手机号 {}, messageId={}", message.getReceiver(), message.getMessageId());
                CallbackUtils.executeCallback(message, false, code);
            }
        } else {
            throw new RuntimeException("已达到短信限流策略的上限");
        }
    }

    /**
     * 发送短信
     *
     * @param message 短信
     * @return 返回码
     */
    private String sendSms(SmsMessage message) {
        if (message == null) {
            return null;
        }

        if (messageProperties.isAliyunSmsEnabled()) {
            return aliyunSmsService.sendSms(message);
        } else if (messageProperties.isQcloudSmsEnabled()) {
            return qcloudSmsService.sendSms(message);
        } else if (messageProperties.isYunpianSmsEnabled()) {
            return yunpianSmsService.sendSms(message);
        } else {
            throw new RuntimeException("必须且只能开启1种短信服务！");
        }
    }

    /**
     * 检查参数
     */
    @PostConstruct
    private void checkSmsServices() {
        int enabledServicesCount = BooleanUtils.toInteger(messageProperties.isAliyunSmsEnabled())
                + BooleanUtils.toInteger(messageProperties.isQcloudSmsEnabled()) + BooleanUtils.toInteger(messageProperties.isYunpianSmsEnabled());
        if (enabledServicesCount != 1) {
            throw new RuntimeException("必须且只能开启1种短信服务！");
        }
        int maxSmsPerSecond = MessageConstant.DEFAULT_SMS_PER_SECOND;
        if (messageProperties.getMaxSmsPerSecond() != null && messageProperties.getMaxSmsPerSecond() > 0) {
            maxSmsPerSecond = messageProperties.getMaxSmsPerSecond();
        }
        rateLimiter = RateLimiter.create(maxSmsPerSecond);
        logger.info("短信限流策略的上限为每秒 {} 条", maxSmsPerSecond);
    }
}
