package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:35 PM
 * @ContentUse :
 */

@Slf4j
public class HttpRequestUtils {

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final MediaType MULTIPARTFORMDATA = MediaType.parse("multipart/form-data");
    private static final MediaType XWWWFORMURLENCODED = MediaType.parse("application/x-www-form-urlencoded");
    public static Response get(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        return OK_HTTP_CLIENT.newCall(request).execute();
    }
    public static Response sendGet(String url, Map headers, Map params) throws Exception {
        if(!headers.isEmpty()){
            url=url+setUrlParams(params);
            log.info("with headers third url: [{}]",url);
            Request request = new Request.Builder().url(url).headers(setHeaders(headers)).build();
            return OK_HTTP_CLIENT.newCall(request).execute();
        }else {
            url=url+setUrlParams(params);
            log.info("non headers third url: [{}]",url);
            Request request = new Request.Builder().url(url).build();
            return OK_HTTP_CLIENT.newCall(request).execute();
        }
    }
    public static Optional<String> post(String url, String json)  {
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(body).build();
            return Optional.of(OK_HTTP_CLIENT.newCall(request).execute().body().string());
        }catch(Exception e){
            String message="access url:"+url+" error:"+e.getMessage();
            log.error(message,e);
            throw new RuntimeException(message);
        }
    }
    public static Response sendPost(String url, String json,Map headers)  {
        try {
            RequestBody body;
            if(!headers.isEmpty()&&headers.containsKey("Content-Type")){
                String ContentType= (String)headers.get("Content-Type");
                if(ContentType.contains("application/x-www-form-urlencoded")){
                    FormBody.Builder builder=new FormBody.Builder();
                    JSONObject params= JSONObject.parseObject(json);
                    for(String name:params.keySet()){
                        log.info("form-data is: {}={}",name,params.get(name).toString());
                        builder.add(name,params.get(name).toString());
                    }
                    body=builder.build();
                    Request request=new Request.Builder().url(url).headers(setHeaders(headers)).post(body).build();
                    return OK_HTTP_CLIENT.newCall(request).execute();
                }
//                else if(ContentType.contains("multipart/form-data")){
//                    //TODO
//                }
                else{
                    body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder().url(url).post(body).build();
                    return OK_HTTP_CLIENT.newCall(request).execute();
                }

            }else {
                body = RequestBody.create(JSON, json);
                if(headers.isEmpty()){
                    Request request = new Request.Builder().url(url).post(body).build();
                    return OK_HTTP_CLIENT.newCall(request).execute();
                }else {
                    log.info("entry post with headers!");
                    Request request = new Request.Builder().url(url).headers(setHeaders(headers)).post(body).build();
                    return OK_HTTP_CLIENT.newCall(request).execute();
                }
            }

        }catch(Exception e){
            String message="access url:"+url+" error:"+e.getMessage();
            log.error(message,e);
            throw new RuntimeException(message);
        }
    }
    public static Response sendPostWithHeaders(String url, String json, Map headers)  {
        try {
            String auth_token=(String) headers.get("auth_token");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).addHeader("Cookie","auth-token="+auth_token).post(body).build();
            return OK_HTTP_CLIENT.newCall(request).execute();
        }catch(Exception e){
            String message="access url:"+url+" error:"+e.getMessage();
            log.error(message,e);
            throw new RuntimeException(message);
        }
    }
    public static Response sendgetWithHeaders(String url,String cookie) throws IOException {
        Request request = new Request.Builder().url(url).addHeader("cookie",cookie).build();
        Response response = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build().newCall(request).execute();
        String body = response.body().string();
        Headers headers = response.networkResponse().request().headers();
        log.info(headers.toString());
        return response;
    }
    public static Optional<String> post(String url, MediaType mediaType, InputStream inputStream) throws Exception {
        RequestBody body = createRequestBody(mediaType, inputStream);
        Request request = new Request.Builder().url(url).post(body).build();
        return Optional.ofNullable(OK_HTTP_CLIENT.newCall(request).execute().body().string());
    }
    private static RequestBody createRequestBody(final MediaType mediaType, final InputStream inputStream){
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() throws IOException {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }
            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
    public static Response put(String url,String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).put(body).build();
        return OK_HTTP_CLIENT.newCall(request).execute();
    }
    public static Response delete(String url) throws IOException {
        Request request=new Request.Builder().url(url).delete().build();
        return OK_HTTP_CLIENT.newCall(request).execute();
    }
    public static Response sendRequest(String url, String json, Map headers){
        Set set = headers.keySet();
        for (Object key:set){

        }
        try {
            String auth_token=(String) headers.get("auth_token");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).addHeader("Cookie","auth-token="+auth_token).post(body).build();

            return OK_HTTP_CLIENT.newCall(request).execute();
        }catch(Exception e){
            String message="access url:"+url+" error:"+e.getMessage();
            log.error(message,e);
            throw new RuntimeException(message);
        }
    }
    public static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                System.out.println(("current_header===>"+ key + ":  " + headersParams.get(key)));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }
    public static String setUrlParams( Map<String, String> mapParams){
        String strParams = "";
        if(mapParams != null){
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                strParams += "&"+ key + "=" + String.valueOf(mapParams.get(key));
            }
        }
        return strParams.replaceFirst("&","?");
    }
}
