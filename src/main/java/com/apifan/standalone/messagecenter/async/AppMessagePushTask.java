package com.apifan.standalone.messagecenter.async;

import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.service.app.IAppMsgPushService;
import com.apifan.standalone.messagecenter.util.CallbackUtils;
import com.apifan.standalone.messagecenter.vo.AppMessage;
import com.apifan.standalone.messagecenter.vo.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * APP消息推送任务（异步）
 *
 * @author yindz
 */
@Component
public class AppMessagePushTask {
    private static final Logger logger = LoggerFactory.getLogger(AppMessagePushTask.class);

    @Autowired
    private MessageProperties messageProperties;

    @Resource(name = "JPushService")
    private IAppMsgPushService jpushService;

    @Resource(name = "GetuiPushService")
    private IAppMsgPushService getuiService;

    /**
     * 异步推送
     */
    @Async("msgPushThreadPool")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void push(AppMessage message) {
        if (message == null) {
            return;
        }
        if (CollectionUtils.isEmpty(message.getReceiverList())) {
            logger.error("app消息的接收者为空");
            return;
        }
        if (messageProperties.isJpushEnabled()) {
            BaseResult result = jpushService.push(message);
            if (result != null && result.isSuccess()) {
                logger.info("极光推送成功！messageId={}", message.getMessageId());
                CallbackUtils.executeCallback(message, true, null);
            } else {
                throw new RuntimeException("极光推送异常");
            }
        } else if (messageProperties.isGetuiEnabled()) {
            BaseResult result = getuiService.push(message);
            if (result != null && result.isSuccess()) {
                logger.info("个推推送成功！messageId={}", message.getMessageId());
                CallbackUtils.executeCallback(message, true, null);
            } else {
                throw new RuntimeException("个推推送异常");
            }
        } else{
            throw new RuntimeException("必须且只能开启1种app消息推送服务！");
        }
    }

    /**
     * 重试失败时的日志记录
     *
     * @param re
     */
    @Recover
    private void recover(Exception re, AppMessage message) {
        logger.error("重试异常", re);
        CallbackUtils.executeCallback(message, false, re.getMessage());
    }
}
