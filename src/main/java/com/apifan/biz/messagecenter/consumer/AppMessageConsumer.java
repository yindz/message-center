package com.apifan.biz.messagecenter.consumer;

import com.apifan.biz.messagecenter.async.AppMessagePushTask;
import com.apifan.biz.messagecenter.vo.AppMessageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * APP消息消费者
 *
 * @author yinzl
 */
@Component
public class AppMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(AppMessageConsumer.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AppMessagePushTask appMessagePushTask;

    /**
     * 接收APP队列的消息
     * @param msg
     */
    @JmsListener(destination = "${msg.queueName}")
    public void receive(String msg){
        logger.info("接收到消息: {}", msg);
        try {
            AppMessageVO appMessage = objectMapper.readValue(msg, AppMessageVO.class);
            appMessagePushTask.push(appMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
