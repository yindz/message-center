package com.apifan.standalone.messagecenter.service.app.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.service.app.IAppMsgPushService;
import com.apifan.standalone.messagecenter.vo.AppMessage;
import com.apifan.standalone.messagecenter.vo.BaseResult;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 极光推送实现
 *
 * @author yindz
 */
@Service("JPushService")
public class JPushServiceImpl implements IAppMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(JPushServiceImpl.class);

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 缓存
     */
    private static Map<String, JPushClient> jPushClientMap = new ConcurrentHashMap<>();

    /**
     * 推送app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResult push(AppMessage appMessage) {
        if (appMessage == null) {
            throw new RuntimeException("极光消息为空");
        }
        if(CollectionUtils.isEmpty(appMessage.getReceiverList())){
            throw new RuntimeException("极光消息接收者为空");
        }
        logger.info("极光消息开始推送: {}", appMessage.getContent());
        BaseResult result = new BaseResult();
        result.setMessageId(appMessage.getMessageId());
        result.setSuccess(false);
        List<String> aliasList = appMessage.getReceiverList();
        PushPayload payload = getPayloadByAlias(aliasList, appMessage.getTitle(), appMessage.getExtras());
        if (push(payload)) {
            logger.info("极光消息推送成功");
            result.setSuccess(true);
            return result;
        }
        logger.error("极光消息推送失败");
        result.setErrorMsg("失败");
        return result;
    }

    /**
     * 进行推送
     *
     * @param payload 消息体
     * @return
     */
    private boolean push(PushPayload payload) {
        logger.info("极光推送消息体: {}", payload.toJSON());
        try {
            PushResult result = getJPushClient().sendPush(payload);
            if (result != null) {
                if (200 == result.getResponseCode()) {
                    logger.info("极光消息推送成功，消息ID: {}", result.msg_id);
                    return true;
                } else {
                    logger.error("极光消息推送失败，ResponseCode={}", result.getResponseCode());
                }
            }
        } catch (APIConnectionException e) {
            logger.error("推送极光消息出现APIConnectionException异常", e);
        } catch (APIRequestException e) {
            logger.error("推送极光消息出现APIRequestException异常", e);
            logger.error("推送极光消息出现异常, status={}, errorCode={}, errorMessage={}", e.getStatus(), e.getErrorCode(), e.getErrorMessage());
        }
        return false;
    }

    /**
     * 生成文本消息对象
     *
     * @param aliasList 别名列表
     * @param alert     提醒文字
     * @param extras    自定义参数
     * @return
     */
    private PushPayload getPayloadByAlias(List<String> aliasList, String alert, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(aliasList))
                //IOS配置为生产环境才能收到推送，参考：https://community.jiguang.cn/t/ios/13452/7
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .setNotification(
                        Notification
                                .newBuilder()
                                .setAlert(alert)
                                .addPlatformNotification(
                                        IosNotification.newBuilder().setSound("default").setBadge(1).addExtras(extras)
                                                .build())
                                .addPlatformNotification(AndroidNotification.newBuilder().setBuilderId(1).addExtras(extras).build()).build()).build();
    }

    /**
     * 获取客户端实例
     */
    private JPushClient getJPushClient() {
        String key = messageProperties.getJpushAppKey();
        return jPushClientMap.computeIfAbsent(key, k -> {
            ClientConfig clientConfig = ClientConfig.getInstance();
            return new JPushClient(messageProperties.getJpushMasterSecret(), messageProperties.getJpushAppKey(), null, clientConfig);
        });
    }
}
