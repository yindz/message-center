package com.apifan.standalone.messagecenter.service.app.impl;

import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.service.app.IAppMsgPushService;
import com.apifan.standalone.messagecenter.vo.AppMessage;
import com.apifan.standalone.messagecenter.vo.BaseResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.TagTarget;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 个推推送实现
 *
 * @author yindz
 */
@Service("GetuiPushService")
public class GetuiPushServiceImpl implements IAppMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(GetuiPushServiceImpl.class);

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 缓存
     */
    private static Map<String, IGtPush> iGtPushMap = new ConcurrentHashMap<>();

    /**
     * 推送app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResult push(AppMessage appMessage) {
        if (appMessage == null) {
            throw new RuntimeException("个推消息为空");
        }
        if (CollectionUtils.isEmpty(appMessage.getReceiverList())) {
            throw new RuntimeException("个推消息接收者为空");
        }
        logger.info("个推消息开始推送: {}", appMessage.getContent());
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(messageProperties.getGetuiAppId());
        template.setAppkey(messageProperties.getGetuiAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent(appMessage.getContent());
        List<TagTarget> tagTargetList = new ArrayList<>(appMessage.getReceiverList().size());
        for (String receiver : appMessage.getReceiverList()) {
            TagTarget tagTarget = new TagTarget();
            tagTarget.setAppId(messageProperties.getGetuiAppId());
            tagTarget.setClientId(receiver);
            tagTargetList.add(tagTarget);
        }

        IPushResult ret = getGtClient().pushMessageToListByTag(template.getTransmissionContent(), tagTargetList);
        if (ret == null) {
            throw new RuntimeException("个推响应为空");
        }
        logger.info("个推响应数据: {}", ret);
        BaseResult result = new BaseResult();
        result.setMessageId(appMessage.getMessageId());
        result.setSuccess(true);
        return result;
    }

    /**
     * 获取客户端实例
     */
    private IGtPush getGtClient() {
        String key = messageProperties.getGetuiAppId();
        return iGtPushMap.computeIfAbsent(key, k -> new IGtPush(messageProperties.getGetuiUrl(), messageProperties.getGetuiAppKey(), messageProperties.getGetuiMasterSecret()));
    }
}
