package com.apifan.standalone.messagecenter.service.sms.impl;

import com.apifan.standalone.messagecenter.config.MessageProperties;
import com.apifan.standalone.messagecenter.constant.CommonConstant;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.service.sms.ISmsMessageService;
import com.apifan.standalone.messagecenter.util.JsonUtils;
import com.apifan.standalone.messagecenter.util.http.HttpResponse;
import com.apifan.standalone.messagecenter.util.http.HttpUtils;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 云片短信服务适配
 * 参考文档：https://www.yunpian.com/doc/zh_CN/domestic/list.html
 *
 * @author yindz
 */
@Service("yunpianSmsMessageService")
public class YunpianSmsMessageServiceImpl implements ISmsMessageService {
    private static final Logger logger = LoggerFactory.getLogger(YunpianSmsMessageServiceImpl.class);

    private static final String BASE_URL = "https://sms.yunpian.com/v2/sms/single_send.json";

    private static Map<String, String> headersMap = new HashMap<>();
    static {
        headersMap.put("Accept", "application/json;charset=utf-8");
        headersMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }

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
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apikey", messageProperties.getYunpianSmsApiKey());

        //电话号码
        paramsMap.put("mobile", message.getReceiver());

        //内容
        paramsMap.put("text", message.getContent());
        logger.info("调用云片短信接口URL: {}, 请求参数: {}", BASE_URL, JsonUtils.toJson(paramsMap, true));
        try {
            HttpResponse resp = HttpUtils.postForm(BASE_URL, headersMap, paramsMap);
            if (resp == null) {
                logger.error("云片短信接口返回值为空");
                return MessageConstant.RESULT_ERROR;
            }
            if (resp.getCode() != CommonConstant.HTTP_OK) {
                logger.error("云片短信接口返回的 HTTP 状态码为{}", resp.getCode());
                return MessageConstant.RESULT_ERROR;
            }
            logger.info("云片短信接口返回参数: {}", resp.getMessage());
            Map<String, Object> returnMap = JsonUtils.readAsMap(resp.getMessage());
            if (!"0".equals(Objects.toString(returnMap.get("code")))) {
                logger.error("云片短信发送失败");
                return Objects.toString(returnMap.get("msg"), MessageConstant.RESULT_ERROR);
            }
            logger.info("云片短信发送成功！");
            return MessageConstant.RESULT_OK;
        } catch (IOException e) {
            logger.error("调用云片短信接口异常", e);
            return MessageConstant.RESULT_ERROR;
        }
    }
}
