package com.apifan.standalone.messagecenter.controller;

import com.apifan.standalone.messagecenter.async.AppMessagePushTask;
import com.apifan.standalone.messagecenter.async.EmailMessagePushTask;
import com.apifan.standalone.messagecenter.async.SmsMessagePushTask;
import com.apifan.standalone.messagecenter.constant.MessageConstant;
import com.apifan.standalone.messagecenter.vo.AppMessage;
import com.apifan.standalone.messagecenter.vo.EmailMessage;
import com.apifan.standalone.messagecenter.vo.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息提交接口
 *
 * @author yindz
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SmsMessagePushTask smsMessagePushTask;

    @Autowired
    private AppMessagePushTask appMessagePushTask;

    @Autowired
    private EmailMessagePushTask emailMessagePushTask;

    /**
     * 短信
     *
     * @param sms
     * @return
     */
    @PostMapping("/submitSms")
    public String submitSms(@RequestBody SmsMessage sms) {
        try {
            smsMessagePushTask.push(sms);
            return MessageConstant.RESULT_OK;
        } catch (Exception e) {
            logger.error("提交短信异常", e);
            return e.getMessage();
        }
    }

    /**
     * app消息
     *
     * @param appMessage
     * @return
     */
    @PostMapping("/submitAppMsg")
    public String submitAppMsg(@RequestBody AppMessage appMessage) {
        try {
            appMessagePushTask.push(appMessage);
            return MessageConstant.RESULT_OK;
        } catch (Exception e) {
            logger.error("提交app消息异常", e);
            return e.getMessage();
        }
    }

    /**
     * 邮件
     *
     * @param email
     * @return
     */
    @PostMapping("/submitEmail")
    public String submitEmail(@RequestBody EmailMessage email) {
        try {
            emailMessagePushTask.push(email);
            return MessageConstant.RESULT_OK;
        } catch (Exception e) {
            logger.error("提交邮件异常", e);
            return e.getMessage();
        }
    }
}
