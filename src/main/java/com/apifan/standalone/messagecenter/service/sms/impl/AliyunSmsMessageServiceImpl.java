package com.apifan.standalone.messagecenter.service.sms.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.constant.CommonConstant;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.service.sms.ISmsMessageService;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云短信服务适配
 * 参考文档：https://help.aliyun.com/document_detail/101874.html
 *
 * @author yindz
 */
@Service("aliyunSmsMessageService")
public class AliyunSmsMessageServiceImpl implements ISmsMessageService {
    private static final Logger logger = LoggerFactory.getLogger(AliyunSmsMessageServiceImpl.class);

    private IAcsClient client;

    @Autowired
    private MessageProperties messageProperties;

    /**
     * 发送短信
     *
     * @param message 短信
     * @return 状态码
     */
    @Override
    public String sendSms(SmsMessage message) {
        if (message == null) {
            return null;
        }
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", message.getReceiver());
        request.putQueryParameter("SignName", message.getSignName());
        request.putQueryParameter("TemplateCode", message.getTemplateId());
        request.putQueryParameter("TemplateParam", JsonUtils.toJson(message.getTemplateParams()));
        try {
            CommonResponse sendSmsResponse = client.getCommonResponse(request);
            if (sendSmsResponse == null) {
                logger.error("阿里云短信网关返回数据为空");
                return MessageConstant.RESULT_ERROR;
            }
            logger.info("阿里云短信网关返回数据: {}", JsonUtils.toJson(sendSmsResponse, true));
            if (sendSmsResponse.getHttpStatus() == CommonConstant.HTTP_OK) {
                String dataStr = sendSmsResponse.getData();
                Map<String, Object> dataMap = JsonUtils.readAsMap(dataStr);
                if (dataMap != null && !dataMap.isEmpty()) {
                    String code = Objects.toString(dataMap.get("Code"), "");
                    if ("OK".equals(code)) {
                        logger.info("阿里云短信发送成功!");
                        return MessageConstant.RESULT_OK;
                    }
                }
            }
        } catch (ClientException e) {
            logger.error("阿里云短信发送异常", e);
        }
        return MessageConstant.RESULT_ERROR;
    }

    /**
     * 初始化
     */
    @PostConstruct
    private void initClient() {
        DefaultProfile profile = DefaultProfile.getProfile(messageProperties.getAliyunSmsRegion(), messageProperties.getAliyunSmsAccessKey(), messageProperties.getAliyunSmsAccessSecret());
        client = new DefaultAcsClient(profile);
    }
}
