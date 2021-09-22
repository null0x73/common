package me.null0x73.common.network.web.http.request;

import me.null0x73.common.network.web.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestFactory {

    private Map<String, String> defaultHeaderParamMap;
    private Map<String, String> defaultUrlParamMap;

    public HttpRequestFactory() {
        this.defaultHeaderParamMap = new HashMap<>();
        this.defaultUrlParamMap = new HashMap<>();
    }

    public HttpRequest create(HttpRequestType requestType, String url, Object requestBody){
        HttpRequest request = new HttpRequest(requestType, url, requestBody);
        request.setHeaderParamMap(new HashMap<>(defaultHeaderParamMap));
        request.setUrlParamMap(new HashMap<>(defaultUrlParamMap));
        return request;
    }

    public Map<String, String> getDefaultHeaderParamMap() {
        return defaultHeaderParamMap;
    }
    public void setDefaultHeaderParamMap(Map<String, String> defaultHeaderParamMap) {
        this.defaultHeaderParamMap = defaultHeaderParamMap;
    }
    public Map<String, String> getDefaultUrlParamMap() {
        return defaultUrlParamMap;
    }
    public void setDefaultUrlParamMap(Map<String, String> defaultUrlParamMap) {
        this.defaultUrlParamMap = defaultUrlParamMap;
    }

    public static HttpRequestFactoryBuilder.HttpRequestFactoryBuilderWrapper builder(){
        return new HttpRequestFactoryBuilder.HttpRequestFactoryBuilderWrapper();
    }
    public static class HttpRequestFactoryBuilder {
        private HttpRequestFactory factory;
        public HttpRequestFactoryBuilder() {
            this.factory = new HttpRequestFactory();
        }
        public HttpRequestFactory getFactory() {
            return factory;
        }
        public static class HttpRequestFactoryBuilderWrapper {
            private HttpRequestFactoryBuilder builder;
            public HttpRequestFactoryBuilderWrapper() {
                this.builder = new HttpRequestFactoryBuilder();
            }
            public HttpRequestFactoryBuilder getBuilder() {
                return builder;
            }
            public HttpRequestFactory build(){
                return builder.getFactory();
            }
            public DefaultHeaderParamClauseWrapper defaultHeaderParam(){
                return new DefaultHeaderParamClauseWrapper(this);
            }
            public DefaultUrlParamClauseWrapper defaultUrlParam(){
                return new DefaultUrlParamClauseWrapper(this);
            }
            public static class DefaultHeaderParamClauseWrapper {
                private HttpRequestFactoryBuilderWrapper rootWrapper;
                public DefaultHeaderParamClauseWrapper(HttpRequestFactoryBuilderWrapper rootWrapper){
                    this.rootWrapper = rootWrapper;
                }
                public HttpRequestFactoryBuilderWrapper put(String key, String value){
                    rootWrapper.getBuilder().getFactory().getDefaultHeaderParamMap().put(key, value);
                    return rootWrapper;
                }
                public DefaultCookieParamClauseWrapper defaultCookieParam(){
                    return new DefaultCookieParamClauseWrapper(rootWrapper);
                }
                public static class DefaultCookieParamClauseWrapper {
                    private HttpRequestFactoryBuilderWrapper rootWrapper;
                    public DefaultCookieParamClauseWrapper(HttpRequestFactoryBuilderWrapper rootWrapper){
                        this.rootWrapper = rootWrapper;
                    }
                    public HttpRequestFactoryBuilderWrapper put(String key, String value){
                        String originalCookie = rootWrapper.getBuilder().getFactory().getDefaultHeaderParamMap().get("cookie");
                        String appendedCookie = HttpUtil.appendCookie(originalCookie, key, value);
                        rootWrapper.getBuilder().getFactory().getDefaultHeaderParamMap().put("cookie", appendedCookie);
                        return rootWrapper;
                    }
                }
            }
            public static class DefaultUrlParamClauseWrapper {
                private HttpRequestFactoryBuilderWrapper rootWrapper;
                public DefaultUrlParamClauseWrapper(HttpRequestFactoryBuilderWrapper rootWrapper) {
                    this.rootWrapper = rootWrapper;
                }
                public HttpRequestFactoryBuilderWrapper put(String key, String value){
                    rootWrapper.getBuilder().getFactory().getDefaultUrlParamMap().put(key, value);
                    return rootWrapper;
                }
            }
        }
    }

}
