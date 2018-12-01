package com.apifan.biz.messagecenter.service.sms.impl;

import com.apifan.biz.messagecenter.config.YunpianProperties;
import com.apifan.biz.messagecenter.service.sms.ISmsMsgPushService;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 云片短信推送接口实现
 *
 * @author yinzl
 */
@Service("YunpianSmsService")
public class YunpianSmsMsgPushServiceImpl implements ISmsMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(YunpianSmsMsgPushServiceImpl.class);

    @Autowired
    private YunpianProperties yunpianProperties;

    private static volatile YunpianClient client;

    /**
     * 推送单条短信消息
     *
     * @param smsMessage
     * @return
     */
    @Override
    public BaseResultVO push(SmsMessageVO smsMessage) {
        logger.info("推送短信: {}", smsMessage.getContent());
        initClient();
        Map<String, String> param = client.newParam(2);
        param.put(YunpianClient.MOBILE, smsMessage.getReceiver());
        param.put(YunpianClient.TEXT, smsMessage.getContent());
        Result<SmsSingleSend> r = client.sms().single_send(param);
        if(r == null){
            throw new RuntimeException("云片短信接口响应为空");
        }
        BaseResultVO result = new BaseResultVO();
        if(r.getCode() != null && r.getCode() == 0){
            logger.info("短信发送成功");
            result.setSuccess(true);
            return result;
        }
        result.setSuccess(false);
        result.setErrorMsg(r.getMsg());
        result.setResponse(r.getDetail());
        logger.error("短信发送失败: {}", r.getMsg());
        return result;
    }

    /**
     * 初始化客户端
     */
    private void initClient() {
        if (client == null) {
            synchronized (YunpianSmsMsgPushServiceImpl.class) {
                if (client == null) {
                    logger.info("初始化YunpianClient");
                    client = new YunpianClient(yunpianProperties.getApiKey()).init();
                    logger.info("初始化YunpianClient成功");
                }
            }
        }
    }
}
