package com.apifan.biz.messagecenter.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息
 *
 * @author yinzl
 */
public class MessageVO implements Serializable {
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
     * 消息创建时间
     */
    private Date createTime;

    /**
     * 消息创建者
     */
    private String createBy;

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
     * 获取 消息创建时间
     *
     * @return createTime 消息创建时间
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置 消息创建时间
     *
     * @param createTime 消息创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 消息创建者
     *
     * @return createBy 消息创建者
     */
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * 设置 消息创建者
     *
     * @param createBy 消息创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
