package com.apifan.biz.messagecenter.async;

import com.apifan.biz.messagecenter.service.sms.ISmsMsgPushService;
import com.apifan.biz.messagecenter.service.sys.ServiceBeanManager;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;
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
 * 短信推送任务（异步）
 *
 * @author yinzl
 */
@Component
public class SmsMessagePushTask {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessagePushTask.class);

    /**
     * 短信推送技术服务商映射
     */
    private static Map<String, ISmsMsgPushService> smsPusherMap = Maps.newConcurrentMap();

    @Autowired
    private ServiceBeanManager serviceBeanManager;

    /**
     * 异步推送
     */
    @Async("smsSendThreadPool")
    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void push(SmsMessageVO message) {
        if (message != null) {
            ISmsMsgPushService smsMsgPushService = getSmsMsgPushService(message.getProvider());
            BaseResultVO result = smsMsgPushService.push(message);
            if (!result.isSuccess()) {
                throw new RuntimeException("短信推送异常");
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
     * @param provider 推送服务商标识
     * @return
     */
    private ISmsMsgPushService getSmsMsgPushService(String provider) {
        ISmsMsgPushService pusher = smsPusherMap.get(provider);
        if (pusher == null) {
            pusher = serviceBeanManager.getServiceBean(provider, ISmsMsgPushService.class);
            if (pusher == null) {
                throw new RuntimeException("未找到 " + provider + " 对应的推送技术服务商");
            }
            smsPusherMap.put(provider, pusher);
        }
        return pusher;
    }
}
