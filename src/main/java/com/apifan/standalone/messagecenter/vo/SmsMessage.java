package com.apifan.standalone.messagecenter.vo;

import java.util.Map;

/**
 * 短信消息
 *
 * @author yindz
 */
public class SmsMessage extends Message {
    private static final long serialVersionUID = -4732866581871201935L;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * 短信模板参数
     */
    private Map<String, String> templateParams;

    /**
     * 短信内容（仅用于支持直接传入内容的短信服务商）
     */
    private String content;

    /**
     * 获取 接收者
     *
     * @return receiver 接收者
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * 设置 接收者
     *
     * @param receiver 接收者
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取 短信签名
     *
     * @return signName 短信签名
     */
    public String getSignName() {
        return this.signName;
    }

    /**
     * 设置 短信签名
     *
     * @param signName 短信签名
     */
    public void setSignName(String signName) {
        this.signName = signName;
    }

    /**
     * 获取 短信模板ID
     *
     * @return templateId 短信模板ID
     */
    public String getTemplateId() {
        return this.templateId;
    }

    /**
     * 设置 短信模板ID
     *
     * @param templateId 短信模板ID
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * 获取 短信模板参数
     *
     * @return templateParams 短信模板参数
     */
    public Map<String, String> getTemplateParams() {
        return this.templateParams;
    }

    /**
     * 设置 短信模板参数
     *
     * @param templateParams 短信模板参数
     */
    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }

    /**
     * 获取 短信内容（仅用于支持直接传入内容的短信服务商）
     *
     * @return content 短信内容（仅用于支持直接传入内容的短信服务商）
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 设置 短信内容（仅用于支持直接传入内容的短信服务商）
     *
     * @param content 短信内容（仅用于支持直接传入内容的短信服务商）
     */
    public void setContent(String content) {
        this.content = content;
    }
}
