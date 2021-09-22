package me.null0x73.common.network.web.http.executor;

import me.null0x73.common.network.web.http.HttpUtil;
import me.null0x73.common.network.web.http.executor.impl.SpringRestTemplateHttpExecutor;
import me.null0x73.common.network.web.http.request.HttpRequest;
import me.null0x73.common.network.web.http.request.HttpRequestFactory;
import me.null0x73.common.network.web.http.request.HttpRequestType;
import me.null0x73.common.network.web.http.response.HttpResponse;

public abstract class HttpExecutor {



    // 单例默认执行器，用于获取全局默认执行器对象。
    private static volatile HttpExecutor defaultExecutor;
    protected static HttpExecutor getDefaultExecutor(){
        if(defaultExecutor==null){
            synchronized (HttpExecutor.class){
                if(defaultExecutor==null){
                    defaultExecutor = new SpringRestTemplateHttpExecutor(null);     // TODO：use apache http-components implement
                }
            }
        }
        return defaultExecutor;
    }





    // 请求原型工厂类。包含了 defaultUrlParam 和 defaultHeaderParam 等逻辑，包含了默认请求内容的构造填充逻辑。
    private HttpRequestFactory requestFactory;

    protected abstract HttpResponse execute(HttpRequest request);

    // 当 client 构造时，可选传入指定可配置的默认值（请求内容）
    public HttpExecutor(HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory!=null ? requestFactory : new HttpRequestFactory();
    }





    public HttpExecutorRootWrapper doGet(String url) {
        HttpRequest request = requestFactory.create(HttpRequestType.GET, url, null);
        return new HttpExecutorRootWrapper(request, this);
    }

    public HttpExecutorRootWrapper doPost(String url, Object requestBody) {
        HttpRequest request = requestFactory.create(HttpRequestType.POST, url, requestBody);
        return new HttpExecutorRootWrapper(request, this);
    }

    // 两个静态方法，可以手动传入执行器以指定执行动作。也可以传入 null 以直接使用默认执行器逻辑。
    public static HttpExecutorRootWrapper doGet(String url, HttpRequestFactory requestFactory) {
        HttpRequest request = requestFactory.create(HttpRequestType.GET, url, null);
        return new HttpExecutorRootWrapper(request, getDefaultExecutor());
    }

    public static HttpExecutorRootWrapper doPost(String url, Object requestBody, HttpRequestFactory requestFactory){
        HttpRequest request = requestFactory.create(HttpRequestType.GET, url, requestBody);
        return new HttpExecutorRootWrapper(request, getDefaultExecutor());
    }

    public static class HttpExecutorRootWrapper {
        private HttpRequest request;
        private HttpExecutor executor;
        public HttpExecutorRootWrapper(HttpRequest request, HttpExecutor executor) {
            this.request = request;
            this.executor = executor;
        }
        public HttpRequest getRequest() {
            return request;
        }
        public HttpExecutor getExecutor() {
            return executor;
        }
        // 具体子句
        public HeaderParamClauseWrapper headerParam(){
            return new HeaderParamClauseWrapper(this);
        }
        public UrlParamClauseWrapper urlParam(){
            return new UrlParamClauseWrapper(this);
        }
        public PathVariableClauseWrapper pathVariable(){
            return new PathVariableClauseWrapper(this);
        }
        // 状态出口（执行）
        public HttpResponse execute(){
            return executor.execute(request);
        }
        // 子句包装类
        public static class HeaderParamClauseWrapper{
            private HttpExecutorRootWrapper rootWrapper;
            public HeaderParamClauseWrapper(HttpExecutorRootWrapper rootWrapper) {
                this.rootWrapper = rootWrapper;
            }
            public HttpExecutorRootWrapper put(String key, String value){
                rootWrapper.getRequest().getHeaderParamMap().put(key, value);
                return rootWrapper;
            }
            public CookieParamClauseWrapper cookieParam(){
                return new CookieParamClauseWrapper(rootWrapper);
            }
            public static class CookieParamClauseWrapper{
                private HttpExecutorRootWrapper rootWrapper;
                public CookieParamClauseWrapper(HttpExecutorRootWrapper rootWrapper){
                    this.rootWrapper = rootWrapper;
                }
                public HttpExecutorRootWrapper put(String key, String value){
                    String originalCookie = rootWrapper.getRequest().getHeaderParamMap().get("cookie");
                    String appendedCookie = HttpUtil.appendCookie(originalCookie, key, value);
                    rootWrapper.getRequest().getHeaderParamMap().put("cookie", appendedCookie);
                    return rootWrapper;
                }
            }
        }
        public static class UrlParamClauseWrapper {
            private HttpExecutorRootWrapper rootWrapper;
            public UrlParamClauseWrapper(HttpExecutorRootWrapper rootWrapper){
                this.rootWrapper = rootWrapper;
            }
            public HttpExecutorRootWrapper put(String key, String value){
                rootWrapper.getRequest().getUrlParamMap().put(key, value);
                return rootWrapper;
            }
        }
        public static class PathVariableClauseWrapper {
            private HttpExecutorRootWrapper rootWrapper;
            public PathVariableClauseWrapper(HttpExecutorRootWrapper rootWrapper){
                this.rootWrapper = rootWrapper;
            }
            public HttpExecutorRootWrapper put(String key, String value){
                rootWrapper.getRequest().getPathVariableMap().put(key, value);
                return rootWrapper;
            }
        }
    }


}
