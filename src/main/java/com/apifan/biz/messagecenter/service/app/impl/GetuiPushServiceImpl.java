package com.apifan.biz.messagecenter.service.app.impl;

import com.apifan.biz.messagecenter.config.GetuiPushProperties;
import com.apifan.biz.messagecenter.service.app.IAppMsgPushService;
import com.apifan.biz.messagecenter.vo.AppMessageVO;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 个推
 *
 * @author yinzl
 */
@Service("GetuiPushService")
public class GetuiPushServiceImpl implements IAppMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(GetuiPushServiceImpl.class);

    @Autowired
    private GetuiPushProperties getuiPushProperties;

    private static volatile IGtPush gtPushClient;

    /**
     * 推送单条app消息
     *
     * @param appMessage
     * @return
     */
    @Override
    public BaseResultVO push(AppMessageVO appMessage) {
        logger.info("个推开始推送: {}", appMessage.getContent());
        initClient();

        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(getuiPushProperties.getAppId());
        template.setAppkey(getuiPushProperties.getAppKey());
        template.setTransmissionType(1);
        template.setTransmissionContent(appMessage.getContent());

        SingleMessage singleMessage = new SingleMessage();
        singleMessage.setData(template);

        Target target = new Target();
        target.setAlias(appMessage.getReceiver());
        target.setAppId(getuiPushProperties.getAppId());
        IPushResult ret = gtPushClient.pushMessageToSingle(singleMessage, target);
        if(ret == null){
            throw new RuntimeException("个推响应为空");
        }
        logger.info("个推响应数据: {}", ret);
        BaseResultVO result = new BaseResultVO();
        result.setSuccess(true);
        return result;
    }

    /**
     * 初始化客户端
     */
    private void initClient() {
        if (gtPushClient == null) {
            synchronized (GetuiPushServiceImpl.class) {
                if (gtPushClient == null) {
                    logger.info("初始化gtPushClient");
                    gtPushClient = new IGtPush(getuiPushProperties.getUrl(), getuiPushProperties.getAppKey(), getuiPushProperties.getMasterSecret());
                    logger.info("初始化gtPushClient成功");
                }
            }
        }
    }
}
