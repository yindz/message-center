package com.apifan.biz.messagecenter.service.sms;

import com.apifan.biz.messagecenter.vo.BaseResultVO;
import com.apifan.biz.messagecenter.vo.SmsMessageVO;

/**
 * 短信推送接口
 *
 * @author yinzl
 */
public interface ISmsMsgPushService {

    /**
     * 推送单条短信消息
     * @param smsMessage
     * @return
     */
    BaseResultVO push(SmsMessageVO smsMessage);
}
