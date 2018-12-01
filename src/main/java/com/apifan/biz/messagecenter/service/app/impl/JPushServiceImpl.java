package com.apifan.biz.messagecenter.service.app.impl;

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
import com.apifan.biz.messagecenter.config.JPushProperties;
import com.apifan.biz.messagecenter.service.app.IAppMsgPushService;
import com.apifan.biz.messagecenter.vo.AppMessageVO;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 极光推送实现
 *
 * @author yinzl
 */
@Service("JPushService")
public class JPushServiceImpl implements IAppMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(JPushServiceImpl.class);

    @Autowired
    private JPushProperties jPushProperties;

    private static volatile JPushClient jPushClient;

    /**
     * 推送单条app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResultVO push(AppMessageVO appMessage) {
        logger.info("极光开始推送: {}", appMessage.getContent());
        BaseResultVO result = new BaseResultVO();

        List<String> aliasList = Lists.newArrayList(appMessage.getReceiver());
        PushPayload payload = getPayloadByAlias(aliasList, appMessage.getTitle(), appMessage.getExtras());
        if (push(payload)) {
            logger.info("极光推送成功");
            result.setSuccess(true);
            return result;
        }
        logger.error("极光推送失败");
        result.setSuccess(false);
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
        initJPushClient();
        logger.info("推送消息体: {}", payload.toJSON());
        try {
            PushResult result = jPushClient.sendPush(payload);
            if (result != null) {
                if (200 == result.getResponseCode()) {
                    logger.info("消息推送成功，消息ID: {}", result.msg_id);
                    return true;
                } else {
                    logger.error("消息 {} 推送失败，ResponseCode={}", result.getResponseCode());
                }
            }
        } catch (APIConnectionException e) {
            logger.error("推送消息出现APIConnectionException异常", e);
        } catch (APIRequestException e) {
            logger.error("推送消息出现APIRequestException异常", e);
            logger.error("推送消息出现异常, status={}, errorCode={}, errorMessage={}", e.getStatus(), e.getErrorCode(), e.getErrorMessage());
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
     * 初始化客户端
     */
    private void initJPushClient() {
        if (jPushClient == null) {
            synchronized (JPushServiceImpl.class) {
                if (jPushClient == null) {
                    logger.info("初始化JPushClient");
                    ClientConfig clientConfig = ClientConfig.getInstance();
                    jPushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey(), null, clientConfig);
                    logger.info("初始化JPushClient成功");
                }
            }
        }
    }
}
