package com.apifan.biz.messagecenter.service.sms.impl;

import com.apifan.biz.messagecenter.config.QcloudSmsProperties;
import com.apifan.biz.messagecenter.service.sms.ISmsMsgPushService;
import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 腾讯云短信推送接口实现
 *
 * @author yinzl
 */
@Service("QcloudSmsService")
public class QcloudSmsMsgPushServiceImpl implements ISmsMsgPushService {
    private static final Logger logger = LoggerFactory.getLogger(QcloudSmsMsgPushServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private QcloudSmsProperties qcloudSmsProperties;

    private static volatile SmsSingleSender client;

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
        if (StringUtils.isEmpty(smsMessage.getExtras().get("templateCode"))) {
            throw new RuntimeException("短信模板编码为空");
        }
        Integer templateId = Integer.parseInt(smsMessage.getExtras().get("templateCode"));

        initClient();
        BaseResultVO result = new BaseResultVO();
        result.setSuccess(false);
        int paramsCount = smsMessage.getExtras().size() - 1;
        try {
            //数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
            String[] params = new String[paramsCount];
            if(paramsCount > 0){
                int i = 0;
                for (Map.Entry<String, String> entry : smsMessage.getExtras().entrySet()) {
                    if (!"templateCode".equalsIgnoreCase(entry.getKey())) {
                        params[i] = entry.getValue();
                    }
                    i++;
                }
            }
            SmsSingleSenderResult senderResult = client.sendWithParam("86", smsMessage.getReceiver(),
                    templateId, params, qcloudSmsProperties.getSignName(), "", "");
            logger.info("腾讯云短信网关返回结果: {}", objectMapper.writeValueAsString(senderResult));
            result.setSuccess(true);
            result.setResponse(senderResult.errMsg);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 初始化客户端
     */
    private void initClient() {
        if (client == null) {
            synchronized (QcloudSmsMsgPushServiceImpl.class) {
                if (client == null) {
                    logger.info("初始化腾讯云短信网关");
                    client = new SmsSingleSender(qcloudSmsProperties.getAppId(), qcloudSmsProperties.getAppKey());
                    logger.info("初始化腾讯云短信网关成功");
                }
            }
        }
    }
}
