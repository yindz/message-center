package com.apifan.standalone.messagecenter.service.app;

import com.apifan.standalone.messagecenter.vo.AppMessage;
import com.apifan.standalone.messagecenter.vo.BaseResult;

/**
 * APP消息推送接口
 *
 * @author yindz
 */
public interface IAppMsgPushService {

    /**
     * 推送app消息
     * @param appMessage
     * @return
     */
    BaseResult push(AppMessage appMessage);
}
