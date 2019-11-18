package com.apifan.standalone.messagecenter.constant;

import java.nio.charset.StandardCharsets;

/**
 * 通用常量
 *
 * @author yindz
 * @since 1.1.0
 */
public final class CommonConstant {

    /**
     * HTTP请求成功
     */
    public static final int HTTP_OK = 200;

    /**
     * HTTP-禁止
     */
    public static final int HTTP_FORBIDDEN = 403;

    /**
     * HTTP-不存在
     */
    public static final int HTTP_NOT_FOUND = 404;

    /**
     * HTTP-错误
     */
    public static final int HTTP_ERROR = 500;

    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

    /**
     * GBK编码
     */
    public static final String GBK_CHARSET = "GBK";
}
