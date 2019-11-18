package com.apifan.standalone.messagecenter.listener;

import com.apifan.standalone.messagecenter.async.AppMessagePushTask;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.util.StringUtils;
import com.apifan.standalone.messagecenter.vo.AppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息队列中的app消息并进行处理
 *
 * @author yindz
 */
@Component
public class AppMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppMessageHandler.class);

    @Autowired
    private AppMessagePushTask appMessagePushTask;

    /**
     * 接收APP队列的消息
     *
     * @param msg 消息体
     */
    @KafkaListener(id = "appMessageHandler", groupId = MessageConstant.GROUP_ID, topics = MessageConstant.APP_MSG_TOPIC)
    public void receive(String msg) {
        if (StringUtils.isEmpty(msg)) {
            logger.warn("从 {} 接收到的消息为空", MessageConstant.APP_MSG_TOPIC);
            return;
        }
        logger.info("已接收到app消息: {}", msg);
        try {
            AppMessage appMessage = JsonUtils.readAsObject(msg, AppMessage.class);
            appMessagePushTask.push(appMessage);
        } catch (Exception e) {
            logger.error("推送app消息时出现异常", e);
        }
    }
}
