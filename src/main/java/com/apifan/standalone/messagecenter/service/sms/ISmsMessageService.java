package com.apifan.standalone.messagecenter.service.sms;

import com.apifan.standalone.messagecenter.vo.SmsMessage;

/**
 * 短信接口
 *
 * @author yindz
 */
public interface ISmsMessageService {

    /**
     * 发送短信
     *
     * @param message 短信
     * @return 状态码
     */
    String sendSms(SmsMessage message);
}
