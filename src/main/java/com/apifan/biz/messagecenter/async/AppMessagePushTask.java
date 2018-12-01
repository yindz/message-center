package com.apifan.biz.messagecenter.async;

import com.apifan.biz.messagecenter.service.app.IAppMsgPushService;
import com.apifan.biz.messagecenter.service.sys.ServiceBeanManager;
import com.apifan.biz.messagecenter.vo.AppMessageVO;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * APP消息推送任务（异步）
 *
 * @author yinzl
 */
@Component
public class AppMessagePushTask {
    private static final Logger logger = LoggerFactory.getLogger(AppMessagePushTask.class);

    /**
     * 推送技术服务商映射
     */
    private static Map<String, IAppMsgPushService> appMsgPusherMap = Maps.newConcurrentMap();

    @Autowired
    private ServiceBeanManager serviceBeanManager;

    /**
     * 异步推送
     */
    @Async("msgPushThreadPool")
    @Retryable(value = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 1000L, multiplier = 1.5))
    public void push(AppMessageVO message) {
        if (message != null) {
            IAppMsgPushService appMsgPushService = getAppMsgPushService(message.getProvider());
            BaseResultVO result = appMsgPushService.push(message);
            if (!result.isSuccess()) {
                throw new RuntimeException("推送异常");
            }
        }
    }

    /**
     * 重试失败时的日志记录
     * @param re
     */
    @Recover
    private void recover(Exception re) {
        logger.error("重试异常", re);
    }

    /**
     * 获取推送服务商
     *
     * @param vendor 推送服务商标识
     * @return
     */
    private IAppMsgPushService getAppMsgPushService(String vendor) {
        /**
         * GetuiPushService
         * JPushService
         */
        IAppMsgPushService pusher = appMsgPusherMap.get(vendor);
        if (pusher == null) {
            pusher = serviceBeanManager.getServiceBean(vendor, IAppMsgPushService.class);
            if (pusher == null) {
                throw new RuntimeException("未找到 " + vendor + " 对应的推送技术服务商");
            }
            appMsgPusherMap.put(vendor, pusher);
        }
        return pusher;
    }
}
