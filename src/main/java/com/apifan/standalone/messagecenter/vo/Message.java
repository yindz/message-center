package com.apifan.standalone.messagecenter.vo;


import com.apifan.standalone.messagecenter.util.BaseBean;

/**
 * 消息
 *
 * @author yindz
 */
public class Message extends BaseBean {
    private static final long serialVersionUID = -7966786924504576244L;

    /**
     * 全局唯一ID
     */
    private String messageId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 回调URL
     */
    private String callbackUrl;

    /**
     * 获取 全局唯一ID
     *
     * @return messageId 全局唯一ID
     */
    public String getMessageId() {
        return this.messageId;
    }

    /**
     * 设置 全局唯一ID
     *
     * @param messageId 全局唯一ID
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取 消息类型
     *
     * @return messageType 消息类型
     */
    public String getMessageType() {
        return this.messageType;
    }

    /**
     * 设置 消息类型
     *
     * @param messageType 消息类型
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取 回调URL
     *
     * @return callbackUrl 回调URL
     */
    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    /**
     * 设置 回调URL
     *
     * @param callbackUrl 回调URL
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
