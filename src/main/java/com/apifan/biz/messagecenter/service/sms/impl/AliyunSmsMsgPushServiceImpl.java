package com.apifan.biz.messagecenter.service.sms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.apifan.biz.messagecenter.config.AliyunSmsProperties;
import com.apifan.biz.messagecenter.service.sms.ISmsMsgPushService;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 阿里云短信推送接口实现
 *
 * @author yinzl
 */
@Service("AliyunSmsService")
public class AliyunSmsMsgPushServiceImpl implements ISmsMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsMsgPushServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 短信API产品名称
     */
    private static final String product = "Dysmsapi";

    /**
     * 短信API产品域名
     */
    private static final String domain = "dysmsapi.aliyuncs.com";

    /**
     * 地区编码
     */
    private static final String regionId = "cn-hangzhou";

    @Autowired
    private AliyunSmsProperties aliyunSmsProperties;

    private static volatile IAcsClient client;

    /**
     * 推送单条短信消息
     *
     * @param smsMessage
     * @return
     */
    @Override
    public BaseResultVO push(SmsMessageVO smsMessage) {
        logger.info("推送短信: {}", smsMessage.getContent());
        if (smsMessage.getExtras() == null || smsMessage.getExtras().isEmpty()) {
            throw new RuntimeException("附加参数为空");
        }
        String templateCode = smsMessage.getExtras().get("templateCode");
        if (StringUtils.isEmpty(templateCode)) {
            throw new RuntimeException("短信模板编码为空");
        }

        initClient();

        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(smsMessage.getReceiver());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(aliyunSmsProperties.getSignName());
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"
        Map<String, Object> paramsMap = Maps.newHashMap();
        for (Map.Entry<String, String> entry : smsMessage.getExtras().entrySet()) {
            if (!"templateCode".equalsIgnoreCase(entry.getKey())) {
                paramsMap.put(entry.getKey(), entry.getValue());
            }
        }
        try {
            request.setTemplateParam(objectMapper.writeValueAsString(paramsMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        BaseResultVO result = new BaseResultVO();
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = client.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                logger.info("短信发送成功: {}", sendSmsResponse);
                result.setSuccess(true);
                return result;
            } else {
                logger.error("短信发送失败: {}", sendSmsResponse);
                result.setSuccess(false);
                result.setResponse(sendSmsResponse.getMessage());
                return result;
            }
        } catch (ClientException e) {
            logger.error("调用阿里大鱼短信网关出错", e);
            result.setSuccess(false);
            result.setResponse(e.getErrMsg());
            return result;
        }
    }

    /**
     * 初始化客户端
     */
    private void initClient() {
        if (client == null) {
            synchronized (AliyunSmsMsgPushServiceImpl.class) {
                if (client == null) {
                    logger.info("初始化阿里大鱼短信网关");
                    IClientProfile profile = DefaultProfile.getProfile(regionId, aliyunSmsProperties.getAccessKeyId(), aliyunSmsProperties.getAccessKeySecret());
                    try {
                        DefaultProfile.addEndpoint(regionId, regionId, product, domain);
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                    client = new DefaultAcsClient(profile);
                    logger.info("初始化阿里大鱼短信网关成功");
                }
            }
        }
    }
}
