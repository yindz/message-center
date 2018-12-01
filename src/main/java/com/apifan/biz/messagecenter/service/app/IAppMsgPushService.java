package com.apifan.biz.messagecenter.service.app;

import com.apifan.biz.messagecenter.vo.AppMessageVO;
import com.apifan.biz.messagecenter.vo.BaseResultVO;

/**
 * APP消息推送接口
 *
 * @author yinzl
 */
public interface IAppMsgPushService {

    /**
     * 推送单条app消息
     * @param appMessage
     * @return
     */
    BaseResultVO push(AppMessageVO appMessage);
}
