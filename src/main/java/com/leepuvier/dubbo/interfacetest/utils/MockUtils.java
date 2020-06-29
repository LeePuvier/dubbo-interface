package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leepuvier.dubbo.interfacetest.utils.HttpRequestUtils.*;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:40 PM
 * @ContentUse :
 */

@Slf4j
public class MockUtils {

    /**
     * @功能描述 新增mock配置
     */
    //获取后台登录token
    public static String GetToken() {
        String result = "";
        String url = "http://127.0.0.1:8080/user/login";
        JSONObject params = new JSONObject();
        params.put("user", "test");
        params.put("password", "test");
        Map Header = new HashMap();
        Header.put("Content-Type", "application/json;charset=UTF-8");
        Response response = sendPost(url, params.toJSONString(), Header);

        if (200 != response.code()) {
            System.out.println("mock配置后台登录异常");
        } else {

            String res = response.headers().toString();
            result = response.header("token");
            log.info("返回Token信息" + result);
        }
        return result;
    }
    //增加单条mock配置
    public static Integer addMockNew(Map<String, Object> params, String token) {
        if (params.get("isMock").equals("false")) {
            return -1;
        }
        JSONObject resConfigMock = null;
        Map Header = new HashMap();
        Header.put("Authorization", token);
        Response response = sendPost(params.get("mockDomian").toString(), params.get("mockData").toString(), Header);
        if (null != response) {
            try {
                resConfigMock = JSONObject.parseObject(response.body().string());
                log.info("新增mock配置响应数据#{}", resConfigMock.toJSONString());
                if (2001 != resConfigMock.getInteger("code")) {
                    log.info("mock配置失败,请确认后才继续测试,当前响应码#{}", resConfigMock.getInteger("code"));
                    return 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("mock配置响应解析异常,请确认后才继续测试");
                return 0;
            }
        } else {
            log.info("mock配置请求无响应,请确认后才继续测试");
            return 0;
        }
        return resConfigMock.getInteger("data");
    }
    //增加多条mock配置
    public static List addMockMulti(Map<String, Object> params, String token) {
        List list = new ArrayList();
        if (params.get("isMock").equals("false")) {
            list.add(-1);
            return list;
        }
        JSONObject resConfigMock = null;
        Map Header = new HashMap();
        Header.put("Authorization", token);
        JSONArray mockData = JSONArray.parseArray(params.get("mockData").toString());
        int size = mockData.size();
        for (int i = 0; i < size; i++) {
            Response response = sendPost(params.get("mockDomian").toString(), mockData.get(i).toString(), Header);
            if (null != response) {
                try {
                    resConfigMock = JSONObject.parseObject(response.body().string());
                    log.info("新增mock配置响应数据#{}", resConfigMock.toJSONString());
                    if (2001 != resConfigMock.getInteger("code")) {
                        log.info("mock配置失败,请确认后才继续测试,当前响应码#{}", resConfigMock.getInteger("code"));
                        list.add(0);
                    }else {
                        log.info("mock配置成功");
                        list.add(resConfigMock.getInteger("data"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("mock配置响应解析异常,请确认后才继续测试");
                    list.add(-2);
                }
            } else {
                log.info("mock配置请求无响应,请确认后才继续测试");
                list.add(-3);
            }
        }
        return list;
    }
    //删除单条mock
    public static Integer deleteMock(Integer id,String token){
        String url="http://127.0.0.1:8080/dubbo/config/del?id="+id;
        System.out.println("url---------------:"+ url);
        Map header = new HashMap();
        header.put("Authorization",token);
        Map param = new HashMap();

        Integer resCode=0;
        try {
            Response response = sendGet(url,header,param);
            System.out.println("deleteResponse------------------:"+ response);
            resCode = response.code();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除mock配置请求异常");
        }
        return resCode;
    }
    public static Integer addMockMuti(Map<String, Object> params,String str,String token) {
        if(params.get("isMock").equals("false")){
            return -1;
        }
        JSONObject resConfigMock=null;
        Map Header = new HashMap();
        Header.put("Authorization",token);
        Response response = sendPost(params.get("mockDomian").toString(), str, Header);
        if(null!=response){
            try {
                resConfigMock = JSONObject.parseObject(response.body().string());
                log.info("新增mock配置响应数据#{}",resConfigMock.toJSONString());
                if(2001!=resConfigMock.getInteger("code")){
                    log.info("mock配置失败,请确认后才继续测试,当前响应码#{}",resConfigMock.getInteger("code"));
                    return 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("mock配置响应解析异常,请确认后才继续测试");
                return 0;
            }
        }else {
            log.info("mock配置请求无响应,请确认后才继续测试");
            return 0;
        }
        return resConfigMock.getInteger("data");
    }
    /**
     * @功能描述 新增mock配置，http接口
     */
    public static String addMock_http(Map<String, Object> params) {
        String flag = "false";
        if (params.get("isMock").equals("false")) {
            return flag;
        }
        JSONObject resConfigMock = null;
        Map Header = new HashMap();
        Response response = sendPost(params.get("mockDomian").toString(), params.get("mockData").toString(), Header);
        System.out.println("reponse:"+response);
        if (null != response) {
            try {
                resConfigMock = JSONObject.parseObject(response.body().string());
                log.info("新增mock配置响应数据#{}", resConfigMock.toJSONString());
                if (!resConfigMock.containsKey("id")) {
                    log.info("mock配置失败,请确认后才继续测试");
                    return flag;
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("mock配置响应解析异常,请确认后才继续测试");
                return flag;
            }
        } else {
            log.info("mock配置请求无响应,请确认后才继续测试");
            return flag;
        }
        return resConfigMock.getString("uuid");
    }

    //删除httpmock接口
    public static Integer delete_httpMock(String id){
        String url="http://127.0.0.1:8090/__admin/mappings/"+id;
        System.out.println("url---------------:"+ url);
        Integer resCode=0;
        try {
            Response response = delete(url);
            System.out.println("deleteResponse------------------:"+ response);
            resCode = response.code();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除mock配置请求异常");
        }
        return resCode;
    }
//    //删除多条mock
//    public static Integer deleteMockMulti(List list){
//        int size = list.size();
////        String url="http://127.0.0.1:8080/dubbo/config/del?id="+id;
//        for (int i = 0;i<size;i++){
////            if (list.get(i).toString())
////            url.replace("target",String.valueOf(list.get(i)));
//        }
//
//        Integer resCode=0;
//        try {
////            resCode = JSONObject.parseObject(get(url).body().string()).getInteger("id");
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("删除mock配置请求异常");
//        }
//        return resCode;
//    }
}
