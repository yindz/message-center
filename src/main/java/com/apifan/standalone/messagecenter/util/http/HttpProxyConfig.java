package com.apifan.standalone.messagecenter.util.http;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * HTTP代理配置
 * @author yindz
 * @since 1.0.0
 */
public class HttpProxyConfig {

    /**
     * 主机或IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 是否需要验证
     */
    private boolean needAuth;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 代理
     */
    private Proxy proxy;

    /**
     * 验证器
     */
    private Authenticator authenticator;

    /**
     * 获取不需验证的代理
     * @param host
     * @param port
     */
    public HttpProxyConfig(String host, int port){
        this.host = host;
        this.port = port;
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }

    /**
     * 获取需要验证的代理
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public HttpProxyConfig(String host, int port, final String username, final String password){
        this.host = host;
        this.port = port;
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)){
            this.needAuth = true;
            this.username = username;
            this.password = password;
            this.authenticator = (route, response) -> {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            };
        }
    }

    /**
     * 获取代理
     * @return 代理对象
     */
    public Proxy getProxy(){
        return this.proxy;
    }

    /**
     * 获取验证器
     * @return 验证器
     */
    public Authenticator getAuthenticator(){
        return this.authenticator;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
