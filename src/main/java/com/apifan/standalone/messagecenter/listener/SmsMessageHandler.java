package com.apifan.standalone.messagecenter.listener;

import com.apifan.standalone.messagecenter.async.SmsMessagePushTask;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息队列中的短信消息并进行处理
 *
 * @author yindz
 */
@Component
public class SmsMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageHandler.class);

    @Autowired
    private SmsMessagePushTask smsMessagePushTask;

    /**
     * 接收短信队列的消息
     *
     * @param payload
     */
    @KafkaListener(id = "smsMessageHandler", groupId = MessageConstant.GROUP_ID, topics = MessageConstant.SMS_MSG_TOPIC)
    public void receive(String payload) {
        logger.info("接收到短信消息: {}", payload);
        try {
            SmsMessage smsMessage = JsonUtils.readAsObject(payload, SmsMessage.class);
            smsMessagePushTask.push(smsMessage);
        } catch (Exception e) {
            logger.error("发送短信时出现异常", e);
        }
    }
}
