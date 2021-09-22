package me.null0x73.common.network.web.http.request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    // basic
    HttpRequestType requestType;
    String url;
    Object requestBody;

    // extension
    Map<String, String> pathVariableMap;
    Map<String, String> headerParamMap;
    Map<String, String> urlParamMap;

    // 全新构造
    public HttpRequest(HttpRequestType requestType, String url, Object requestBody) {
        this.requestType = requestType;
        this.url = url;
        this.requestBody = requestBody;
        this.headerParamMap = new HashMap<>();
        this.urlParamMap = new HashMap<>();
        this.pathVariableMap = new HashMap<>();
    }

    public HttpRequestType getRequestType() {
        return requestType;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaderParamMap() {
        return headerParamMap;
    }

    public Map<String, String> getUrlParamMap() {
        return urlParamMap;
    }

    public Map<String, String> getPathVariableMap() {
        return pathVariableMap;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setHeaderParamMap(Map<String, String> headerParamMap) {
        this.headerParamMap = headerParamMap;
    }

    public void setUrlParamMap(Map<String, String> urlParamMap) {
        this.urlParamMap = urlParamMap;
    }
}
