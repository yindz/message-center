package com.apifan.biz.messagecenter.consumer;

import com.apifan.biz.messagecenter.async.SmsMessagePushTask;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 短信消息消费者
 *
 * @author yinzl
 */
@Component
public class SmsMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageConsumer.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SmsMessagePushTask smsMessagePushTask;

    /**
     * 接收短信队列的消息
     *
     * @param msg
     */
    @JmsListener(destination = "${sms.queueName}")
    public void receive(String msg) {
        logger.info("接收到短信消息: {}", msg);
        try {
            SmsMessageVO smsMessage = objectMapper.readValue(msg, SmsMessageVO.class);
            smsMessagePushTask.push(smsMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
