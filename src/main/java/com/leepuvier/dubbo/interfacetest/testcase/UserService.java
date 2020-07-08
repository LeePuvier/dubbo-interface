package com.leepuvier.dubbo.interfacetest.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.leepuvier.dubbo.client.utils.DubboClient;
import com.leepuvier.dubbo.interfacetest.common.BaseProvider;
import com.leepuvier.dubbo.interfacetest.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/7/7  7:26 PM
 * @ContentUse :
 */

@Slf4j
public class UserService {

    @Test(description = "获取所有用户", dataProvider = "getUser", dataProviderClass = BaseProvider.class)
    public void getUser(Map<String, Object> params) {

//        System.out.println("ZooKeeper地址为：" + params.get("addr").toString());

        //建立连接
        DubboClient dubboClient = getDubboClient(params);

        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("jsonObjectParam").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});

        log.info("传入接口参数信息为：" + paraValues);

        //执行请求
        Map reponse = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
        log.info("接口实际返回信息为：" + reponse.toString());

        System.out.println("接口实际返回信息为：" + JSON.toJSONString(reponse));

        //期望接口返回
        String expectResponse = params.get("expectResponse").toString();
        //实际接口放回
        String actResponse = JSON.toJSONString(reponse);
        //对比接口实际返回与预期返回
        Assert.assertTrue(DataUtils.compareJsonResult(actResponse, expectResponse));

        //TODO：对比接口实际返回与数据信息
    }


    public DubboClient getDubboClient(Map<String, Object> params) {
        DubboClient dubboClient;
        try {
            dubboClient = new DubboClient(
                    StringUtils.isBlank(params.get("addr").toString()) ? null : params.get("addr").toString(),
                    StringUtils.isBlank(params.get("url").toString()) ? null : params.get("url").toString(),
                    StringUtils.isBlank(params.get("interfacename").toString()) ? null : params.get("interfacename").toString(),
                    StringUtils.isBlank(params.get("version").toString()) ? null : params.get("version").toString(),
                    StringUtils.isBlank(params.get("group").toString()) ? null : params.get("group").toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取dubbo客户端出错");
            dubboClient = null;
        }
        return dubboClient;
    }
}