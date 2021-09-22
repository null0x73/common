package me.null0x73.common.network.web.http.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class HttpResponse {

    private HttpResponseStatus status;
    private byte[] responseBodyData;

    public HttpResponse(HttpResponseStatus status, byte[] responseBodyData) {
        this.status = status;
        this.responseBodyData = responseBodyData;
    }

    public byte[] getResponseBody(){
        return responseBodyData;
    }

    public String getResponseBodyAsText(){
        return new String(responseBodyData);
    }

    public <T> T getResponseBodyAsObject(Class<T> targetClass){
        try {
            return (new ObjectMapper()).readValue(getResponseBodyAsText(), targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
