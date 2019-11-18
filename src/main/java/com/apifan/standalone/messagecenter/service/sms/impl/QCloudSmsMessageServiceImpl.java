package com.apifan.standalone.messagecenter.service.sms.impl;

import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.constant.CommonConstant;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.service.sms.ISmsMessageService;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.util.StringUtils;
import com.apifan.standalone.messagecenter.util.http.HttpResponse;
import com.apifan.standalone.messagecenter.util.http.HttpUtils;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 腾讯云短信服务适配
 * 参考文档：https://cloud.tencent.com/document/product/382/5976
 *
 * @author yindz
 */
@Service("qcloudSmsMessageService")
public class QCloudSmsMessageServiceImpl implements ISmsMessageService {
    private static final Logger logger = LoggerFactory.getLogger(QCloudSmsMessageServiceImpl.class);

    private static final String BASE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=%s&random=%s";

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
        String randomNumber = StringUtils.getRandomString(10, true);

        Map<String, Object> paramsMap = new HashMap<>();

        //电话号码
        Map<String, Object> telMap = new HashMap<>();
        telMap.put("mobile", message.getReceiver());
        telMap.put("nationcode", "86");
        paramsMap.put("tel", telMap);

        //模板ID
        paramsMap.put("tpl_id", Long.parseLong(message.getTemplateId()));

        //短信签名内容
        paramsMap.put("sign", message.getSignName());

        //模板参数
        List<String> templateParams = new LinkedList<>();
        if (message.getTemplateParams() != null && !message.getTemplateParams().isEmpty()) {
            for (Map.Entry<String, String> entry : message.getTemplateParams().entrySet()) {
                templateParams.add(entry.getValue());
            }
        }
        paramsMap.put("params", templateParams);

        //时间戳
        long timestamp = System.currentTimeMillis();
        paramsMap.put("time", timestamp);

        //App 凭证
        String src = messageProperties.getQcloudSmsAppKey() + "&random=" + randomNumber + "&time=" + timestamp + "&mobile=" + message.getReceiver();
        paramsMap.put("sig", StringUtils.sha256(src));

        String url = String.format(BASE_URL, messageProperties.getQcloudSmsAppId(), randomNumber);
        String requestJson = JsonUtils.toJson(paramsMap);
        logger.info("调用腾讯云短信接口URL: {}, 请求参数: {}", url, requestJson);
        try {
            HttpResponse resp = HttpUtils.postJson(url, new HashMap<>(), requestJson);
            if (resp == null) {
                logger.error("腾讯云短信接口返回值为空");
                return MessageConstant.RESULT_ERROR;
            }
            if (resp.getCode() != CommonConstant.HTTP_OK) {
                logger.error("腾讯云短信接口返回的 HTTP 状态码为{}", resp.getCode());
                return MessageConstant.RESULT_ERROR;
            }
            logger.info("腾讯云短信接口返回参数: {}", resp.getMessage());
            Map<String, Object> returnMap = JsonUtils.readAsMap(resp.getMessage());
            if (!"0".equals(Objects.toString(returnMap.get("result")))) {
                logger.error("腾讯云短信发送失败");
                return Objects.toString(returnMap.get("errmsg"), MessageConstant.RESULT_ERROR);
            }
            logger.info("腾讯云短信发送成功！");
            return MessageConstant.RESULT_OK;
        } catch (IOException e) {
            logger.error("调用腾讯云短信接口异常", e);
            return MessageConstant.RESULT_ERROR;
        }
    }
}
