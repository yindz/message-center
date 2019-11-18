package com.apifan.standalone.messagecenter.vo;

import java.util.List;

/**
 * 电子邮件
 *
 * @author yindz
 */
public class EmailMessage extends Message {
    private static final long serialVersionUID = 5388297236840437564L;

    /**
     * 接收者邮箱地址
     */
    private String receiver;

    /**
     * 标题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否为HTML内容
     */
    private boolean htmlContent;

    /**
     * 附件
     */
    private List<String> attachmentList;

    /**
     * 获取 接收者邮箱地址
     *
     * @return receiver 接收者邮箱地址
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * 设置 接收者邮箱地址
     *
     * @param receiver 接收者邮箱地址
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取 标题
     *
     * @return subject 标题
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * 设置 标题
     *
     * @param subject 标题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取 内容
     *
     * @return content 内容
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 设置 内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取 附件
     *
     * @return attachmentList 附件
     */
    public List<String> getAttachmentList() {
        return this.attachmentList;
    }

    /**
     * 设置 附件
     *
     * @param attachmentList 附件
     */
    public void setAttachmentList(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }

    /**
     * 获取 是否为HTML内容
     *
     * @return htmlContent 是否为HTML内容
     */
    public boolean isHtmlContent() {
        return this.htmlContent;
    }

    /**
     * 设置 是否为HTML内容
     *
     * @param htmlContent 是否为HTML内容
     */
    public void setHtmlContent(boolean htmlContent) {
        this.htmlContent = htmlContent;
    }
}
