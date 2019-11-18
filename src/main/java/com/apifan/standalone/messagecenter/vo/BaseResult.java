package com.apifan.standalone.messagecenter.vo;


import com.apifan.standalone.messagecenter.util.BaseBean;

/**
 * 结果
 *
 * @author yindz
 */
public class BaseResult extends BaseBean {
    private static final long serialVersionUID = -1036893187412443132L;

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 原始响应数据
     */
    private String response;

    /**
     * 失败原因
     */
    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
