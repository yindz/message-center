package com.apifan.standalone.messagecenter.util.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HTTP请求日志拦截器
 *
 * @author yindz
 * @since 1.0.0
 */
public class HttpLogInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(HttpLogInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        long start = System.currentTimeMillis();
        Request request = chain.request();
        logger.debug("发起请求到: {}, 请求头信息: {}", request.url(), request.headers());
        Response response = chain.proceed(request);
        long end = System.currentTimeMillis();
        logger.debug("收到来自 {} 的响应, 耗时 {} 毫秒", request.url(), end - start);
        return response;
    }
}
