package me.null0x73.common.network.web.http;

import me.null0x73.common.collection.CollectionEditor;
import me.null0x73.common.collection.CollectionUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    // URL 处理部分 ————————————————————————————————————

    public static String makeUrl(String baseUrl, Map<String, String> pathVariableMap, Map<String, String> urlParamMap){
        String url = baseUrl;
        if(pathVariableMap!=null){
            url = replacePathVariables(url, pathVariableMap);
        }
        if(urlParamMap!=null){
            url = appendUrlParams(url, urlParamMap);
        }
        return url;
    }

    private static String appendUrlParams(String baseUrl, Map<String, String> urlParamMap) {
        if(urlParamMap==null){
            return baseUrl;
        }
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if(baseUrl.contains("?")) {
            CollectionUtil.process(urlParamMap,
                    entry -> urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue()));
            // TODO: current behavior does not replace duplicate params in baseurl, multiple k-v entries would cause error
        } else {
            CollectionUtil.process(urlParamMap,
                    entry -> urlBuilder.append("?").append(entry.getKey()).append("=").append(entry.getValue()),
                    entry -> urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue()),
                    entry -> urlBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue()));

        }
        return urlBuilder.toString();
    }

    private static String replacePathVariables(String url, Map<String, String> pathVariableMap){
        if(pathVariableMap==null){
            return url;
        }
        for (Map.Entry<String, String> entry : pathVariableMap.entrySet()){
            if(url.contains("{"+entry.getKey()+"}")){
                url = url.replaceAll("\\{"+entry.getKey()+"}",entry.getValue());
            }
        }
        return url;
    }

    // Cookie 处理部分 ——————————————————————————————————————————

    public static String appendCookie(String originalCookie, String newKey, String newValue){
        if(originalCookie==null){
            return makeCookie(CollectionEditor.newMap().put(newKey, newValue).save());
        }
        Map<String, String> cookieMap = parseCookie(originalCookie);
        cookieMap.put(newKey, newValue);
        String cookieString = makeCookie(cookieMap);
        return cookieString;
    }

    public static String makeCookie(Map<String, String> cookieMap){
        StringBuilder builder = new StringBuilder();
        int indexCounter = 0;
        for(Map.Entry<String, String> cookieEntry: cookieMap.entrySet()){
            builder.append(cookieEntry.getKey());
            builder.append("=");
            builder.append(cookieEntry.getValue());
            if(++indexCounter<cookieMap.size()){
                builder.append("; ");
            }
        }
        return builder.toString();
    }

    public static Map<String, String> parseCookie(String cookieString){
        Map<String, String> cookieMap = new HashMap<>();
        String[] cookieItemStrings = cookieString.split(";");
        for(String cookieItemString: cookieItemStrings){
            cookieItemString = cookieItemString.trim();
            String[] cookieItemEntry = cookieItemString.split("=");
            cookieMap.put(cookieItemEntry[0], cookieItemEntry[1]);
        }
        return cookieMap;
    }

}
