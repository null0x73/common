package me.null0x73.common.network.web.http.executor.impl;

import me.null0x73.common.network.web.http.*;
import me.null0x73.common.network.web.http.executor.HttpExecutor;
import me.null0x73.common.network.web.http.request.HttpRequest;
import me.null0x73.common.network.web.http.request.HttpRequestFactory;
import me.null0x73.common.network.web.http.response.HttpResponse;
import me.null0x73.common.network.web.http.response.HttpResponseStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class SpringRestTemplateHttpExecutor extends HttpExecutor {

    private String proxyHost;
    private Integer proxyPort;

    public SpringRestTemplateHttpExecutor(HttpRequestFactory factory) {
        super(factory);
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        // 处理请求方法
        HttpMethod httpMethod = null;
        switch (request.getRequestType()) {
            case GET:
                httpMethod = HttpMethod.GET;
                break;
            case POST:
                httpMethod = HttpMethod.POST;
                break;
            default:
                return null;
        }

        // 请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        if (request.getHeaderParamMap() != null) {
            request.getHeaderParamMap().forEach((key, value) -> {
                requestHeaders.add(key, value);
            });
        }

        // URL 参数
        String mergedUrl = HttpUtil.makeUrl(request.getUrl(), request.getPathVariableMap(), request.getUrlParamMap());

        // 请求收发
        HttpEntity requestEntity = new HttpEntity(request.getRequestBody(), requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        if (proxyHost != null && proxyPort != null) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
            restTemplate.setRequestFactory(requestFactory);
        }

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(mergedUrl, httpMethod, requestEntity, byte[].class);

        // 响应内容转换
        HttpResponse response = new HttpResponse(HttpResponseStatus.SUCCESS_200_OK, responseEntity.getBody());

        // 返回
        return response;
    }
}
