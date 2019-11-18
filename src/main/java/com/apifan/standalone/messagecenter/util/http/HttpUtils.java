package com.apifan.standalone.messagecenter.util.http;

import com.apifan.standalone.messagecenter.util.StringUtils;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * HTTP工具类
 *
 * @author yindz
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /* 其它值参考 http://tools.jb51.net/table/http_header */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    /**
     * 默认的各项超时时间(毫秒)
     */
    private static final int DEFAULT_READ_TIMEOUT = 15000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    private static final int DEFAULT_WRITE_TIMEOUT = 15000;

    private static final String KEY_USER_AGENT = "User-Agent";

    private static final String USER_AGENT_PC = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";

    private static Map<String, OkHttpClient> okHttpClientMap = new ConcurrentHashMap<>();

    /**
     * 发起GET请求并获得响应(使用代理)
     *
     * @param url         URL
     * @param proxyConfig 代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse get(String url, HttpProxyConfig proxyConfig) throws IOException {
        return get(url, null, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, proxyConfig);
    }

    /**
     * 发起GET请求并获得响应(不使用代理)
     *
     * @param url URL
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse get(String url) throws IOException {
        return get(url, new HashMap<>());
    }

    /**
     * 发起GET请求并获得响应(使用自定义请求头和代理)
     *
     * @param url         URL
     * @param headersMap  自定义header信息
     * @param proxyConfig 代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse get(String url, Map<String, String> headersMap, HttpProxyConfig proxyConfig) throws IOException {
        return get(url, headersMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, proxyConfig);
    }

    /**
     * 发起GET请求并获得响应(使用自定义请求头)
     *
     * @param url        URL
     * @param headersMap 自定义header信息
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse get(String url, Map<String, String> headersMap) throws IOException {
        return get(url, headersMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, null);
    }

    /**
     * 发起GET请求并获得响应
     *
     * @param url            URL
     * @param headersMap     自定义header信息
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout    读超时时间(毫秒)
     * @param writeTimeout   写超时时间(毫秒)
     * @param proxyConfig    代理
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse get(String url, Map<String, String> headersMap, int connectTimeout, int readTimeout, int writeTimeout, HttpProxyConfig proxyConfig) throws IOException {
        Request.Builder builder = prepareRequestBuilder(url, headersMap);
        Request request = builder.build();
        OkHttpClient client = getOkHttpClient(connectTimeout, readTimeout, writeTimeout, proxyConfig);
        Response response = client.newCall(request).execute();
        return getHttpResponse(response);
    }

    /**
     * 向某个URL提交Form请求
     *
     * @param url         URL
     * @param headersMap  自定义header信息
     * @param paramsMap   请求参数
     * @param proxyConfig 代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postForm(String url, Map<String, String> headersMap, Map<String, String> paramsMap, HttpProxyConfig proxyConfig) throws IOException {
        return postForm(url, headersMap, paramsMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, proxyConfig);
    }

    /**
     * 向某个URL提交Form请求
     *
     * @param url        URL
     * @param headersMap 自定义header信息
     * @param paramsMap  请求参数
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postForm(String url, Map<String, String> headersMap, Map<String, String> paramsMap) throws IOException {
        return postForm(url, headersMap, paramsMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, null);
    }

    /**
     * 向某个URL提交Form请求
     *
     * @param url            URL
     * @param headersMap     自定义header信息
     * @param paramsMap      请求参数
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout    读超时时间(毫秒)
     * @param writeTimeout   写超时时间(毫秒)
     * @param proxyConfig    代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postForm(String url, Map<String, String> headersMap, Map<String, String> paramsMap, int connectTimeout, int readTimeout, int writeTimeout, HttpProxyConfig proxyConfig) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            paramsMap.forEach((k, v) -> {
                if (StringUtils.isNotEmpty(k) && StringUtils.isNotEmpty(v)) {
                    formBodyBuilder.add(k, v);
                }
            });
        }
        RequestBody body = formBodyBuilder.build();
        return postBody(url, headersMap, body, connectTimeout, readTimeout, writeTimeout, proxyConfig);
    }

    /**
     * 向某个URL提交JSON字符串
     *
     * @param url         URL
     * @param headersMap  自定义header信息
     * @param json        JSON字符串
     * @param proxyConfig 代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postJson(String url, Map<String, String> headersMap, String json, HttpProxyConfig proxyConfig) throws IOException {
        return postJson(url, headersMap, json, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, proxyConfig);
    }

    /**
     * 向某个URL提交JSON字符串
     *
     * @param url        URL
     * @param headersMap 自定义header信息
     * @param json       JSON字符串
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postJson(String url, Map<String, String> headersMap, String json) throws IOException {
        return postJson(url, headersMap, json, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT, null);
    }

    /**
     * 向某个URL提交JSON字符串
     *
     * @param url            URL
     * @param headersMap     自定义header信息
     * @param json           JSON字符串
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout    读超时时间(毫秒)
     * @param writeTimeout   写超时时间(毫秒)
     * @param proxyConfig    代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    public static HttpResponse postJson(String url, Map<String, String> headersMap, String json, int connectTimeout, int readTimeout, int writeTimeout, HttpProxyConfig proxyConfig) throws IOException {
        return postBody(url, headersMap, RequestBody.create(JSON, json), connectTimeout, readTimeout, writeTimeout, proxyConfig);
    }

    /**
     * 发起POST请求并获得响应数据
     *
     * @param url            URL
     * @param headersMap     自定义header信息
     * @param body           请求体
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout    读超时时间(毫秒)
     * @param writeTimeout   写超时时间(毫秒)
     * @param proxyConfig    代理配置
     * @return 响应
     * @throws IOException IO异常
     */
    private static HttpResponse postBody(String url, Map<String, String> headersMap, RequestBody body, int connectTimeout, int readTimeout, int writeTimeout, HttpProxyConfig proxyConfig) throws IOException {
        Request.Builder builder = prepareRequestBuilder(url, headersMap);
        Request request = builder.post(body).build();
        OkHttpClient client = getOkHttpClient(connectTimeout, readTimeout, writeTimeout, proxyConfig);
        Response response = client.newCall(request).execute();
        return getHttpResponse(response);
    }

    /**
     * 设置请求headers
     *
     * @param builder 请求builder
     * @param headers 自定义header信息
     */
    private static void setRequestHeaders(Request.Builder builder, Map<String, String> headers) {
        if (builder == null || headers == null) {
            return;
        }
        if (!headers.containsKey(KEY_USER_AGENT)) {
            //默认使用PC的User-Agent
            headers.put(KEY_USER_AGENT, USER_AGENT_PC);
        }
        headers.forEach((k, v) -> {
            if (StringUtils.isNotEmpty(k) && StringUtils.isNotEmpty(v)) {
                builder.header(k.trim(), v.trim());
            }
        });
    }

    /**
     * 获取OkHttpClient对象
     *
     * @param connectTimeout 连接超时时间(毫秒)
     * @param readTimeout    读超时时间(毫秒)
     * @param writeTimeout   写超时时间(毫秒)
     * @param proxyConfig    代理配置
     * @return OkHttpClient对象
     */
    private static OkHttpClient getOkHttpClient(int connectTimeout, int readTimeout, int writeTimeout, HttpProxyConfig proxyConfig) {
        StringBuilder cacheKey = new StringBuilder();
        cacheKey.append(connectTimeout).append("_").append(readTimeout).append("_").append(writeTimeout);
        if (proxyConfig != null) {
            cacheKey.append("_").append(proxyConfig);
        }
        return okHttpClientMap.computeIfAbsent(cacheKey.toString(), k -> {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (connectTimeout > 0) {
                builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            }
            if (readTimeout > 0) {
                builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            }
            if (writeTimeout > 0) {
                builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
            }
            if (proxyConfig != null) {
                builder.proxy(proxyConfig.getProxy());
                if (proxyConfig.isNeedAuth()) {
                    builder.proxyAuthenticator(proxyConfig.getAuthenticator());
                }
            }
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(256);
            dispatcher.setMaxRequestsPerHost(32);
            builder.dispatcher(dispatcher);
            builder.addInterceptor(new HttpLogInterceptor());
            builder.retryOnConnectionFailure(true);
            return builder.build();
        });
    }

    /**
     * 从URL解析域名
     *
     * @param url URL
     * @return 域名
     */
    public static String parseDomain(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String[] tmp = url.split("/");
        if (tmp.length >= 3) {
            return tmp[2];
        }
        return url;
    }

    /**
     * 获取响应数据
     *
     * @param response Response
     * @return 响应
     * @throws IOException IO异常
     */
    private static HttpResponse getHttpResponse(Response response) throws IOException {
        if (response == null) {
            return null;
        }
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setCode(response.code());
        httpResponse.setMessage(response.body() != null ? response.body().string() : null);
        Headers responseHeaders = response.headers();
        Set<String> headerKeys = responseHeaders.names();
        if (!CollectionUtils.isEmpty(headerKeys)) {
            Map<String, String> headersMap = new HashMap<>();
            for (String headerKey : headerKeys) {
                headersMap.put(headerKey, responseHeaders.get(headerKey));
            }
            httpResponse.setHeaders(headersMap);
        }
        response.close();
        return httpResponse;
    }

    /**
     * 准备请求构建器
     *
     * @param url        URL
     * @param headersMap 自定义header信息
     * @return 请求构建器
     */
    private static Request.Builder prepareRequestBuilder(String url, Map<String, String> headersMap) {
        checkURL(url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        setRequestHeaders(builder, headersMap);
        return builder;
    }

    /**
     * 检查URL
     *
     * @param url URL
     */
    private static void checkURL(String url) {
        if (StringUtils.isEmpty(url) || !url.startsWith("http")) {
            throw new IllegalArgumentException("URL无效");
        }
        if (url.startsWith("https://")) {
            //暂时禁用SNI，避免 JDK1.7 及之后版本出现 javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name 错误
            //http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
            System.setProperty("jsse.enableSNIExtension", "false");
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        }
    }
}
