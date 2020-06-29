package com.leepuvier.dubbo.interfacetest.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.leepuvier.dubbo.client.utils.DubboClient;
import com.leepuvier.dubbo.interfacetest.common.BaseCase;
import com.leepuvier.dubbo.interfacetest.common.BaseProvider;
import com.leepuvier.dubbo.interfacetest.utils.DBUtils;
import com.leepuvier.dubbo.interfacetest.utils.MockUtils;
import com.leepuvier.dubbo.interfacetest.utils.RedisHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.leepuvier.dubbo.interfacetest.utils.ComUtils.compareMultiMysqlData;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:53 PM
 * @ContentUse :
 */

@Slf4j
public class RemoteServiceSearch extends BaseCase {

    @Resource
    RedisHandler redisHandler;

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 根据用户id查询用户银行卡
     * @Other:
     */
    @Test(description = "通过用户id查询银行卡list", dataProvider = "test", dataProviderClass = BaseProvider.class)
    public void getBankCardListByCustIdTest(Map<String, Object> params) {

        System.out.println("用例编号："+params.get("caseNo").toString());

        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //构造缓存数据
        redisHandler.del(params.get("RedisKey").toString());
        redisHandler.del("test:bank:table:code");
        String RedisData = params.get("preRedisData").toString();
        if (null !=RedisData && ""!= RedisData){
            redisHandler.hset(params.get("RedisKey").toString(),"4", JSONObject.parseObject(params.get("preRedisData").toString()));
            String resRedis = redisHandler.hget(params.get("RedisKey").toString(), "4");
            Assert.assertNotNull(resRedis);
        }else {
            String resRedis = redisHandler.hget(params.get("RedisKey").toString(), "4");
            Assert.assertNull(resRedis);
        }
        //执行请求
        DubboClient dubboClient = getDubboClient(params);

        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        System.out.println("参数信息为："+paraValues);

        //读取flag
        int flag = Integer.parseInt(params.get("flag").toString());

        switch (flag){
            case 0:
                //发送响应
                Map res1 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res1);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                Assert.assertTrue(res1.get("errmsg").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("msg")), "数据校验不一致");
                break;
            case 1:
                //发送响应
                Map res2 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res2);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res2.get("data").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()));
                Assert.assertTrue(res2.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res2.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                break;
            case 2:
                //发送响应
                Map res3 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res3);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res3.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res3.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data =(Map) (((ArrayList<Object>)res3.get("data")).get(0));
                Assert.assertTrue(data.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNotNull(data.get("logoUrl"));
                Assert.assertNotNull(data.get("grayLogoUrl"));
                break;
            case 3:
                //发送响应
                Map res4 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res4);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res4.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res4.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data1 =(Map) (((ArrayList<Object>)res4.get("data")).get(0));
                Assert.assertTrue(data1.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data1.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data1.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data1.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNull(data1.get("logoUrl"));
                Assert.assertNull(data1.get("grayLogoUrl"));
                break;
            case 4:
                //发送响应
                Map res5 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res5);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res5.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res5.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data3 =(Map) (((ArrayList<Object>)res5.get("data")).get(0));
                Assert.assertTrue(data3.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data3.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data3.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data3.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNotNull(data3.get("logoUrl"));
                Assert.assertNotNull(data3.get("grayLogoUrl"));
                //增加缓存断言
                String res_redis = redisHandler.hget(params.get("RedisKey").toString(),"4");
                Assert.assertNotNull(res_redis);
                String res_redis1 = redisHandler.hget("test:bank:table:code", "CMBCHINA");
                Assert.assertNotNull(res_redis1);
                break;
        }
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 通过用户id和银行卡号查询银行卡
     * @Other:
     */
    @Test(description = "通过用户id和银行卡号查询银行卡", dataProvider = "getBankCardByCardNo", dataProviderClass = BaseProvider.class)
    public void getBankCardByCardNoTest(Map<String, Object> params) {

        System.out.println("用例编号："+params.get("caseNo").toString());

        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //构造缓存数据
        redisHandler.del(params.get("RedisKey").toString());
        redisHandler.del("test:bank:table:code");
        String RedisData = params.get("preRedisData").toString();
        if (null !=RedisData && ""!= RedisData){
            redisHandler.hset(params.get("RedisKey").toString(),"4",JSONObject.parseObject(params.get("preRedisData").toString()));
            String resRedis = redisHandler.hget(params.get("RedisKey").toString(), "4");
            Assert.assertNotNull(resRedis);
        }else {
            String resRedis = redisHandler.hget(params.get("RedisKey").toString(), "4");
            Assert.assertNull(resRedis);
        }
        //执行请求
        DubboClient dubboClient = getDubboClient(params);

        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        System.out.println("参数信息为："+paraValues);

        //读取flag
        int flag = Integer.parseInt(params.get("flag").toString());

        switch (flag){
            case 0:
                //发送响应
                Map res1 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res1);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                Assert.assertTrue(res1.get("errmsg").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("msg")), "errmsg数据校验不一致");
                break;
            case 1:
                //发送响应
                Map res2 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res2);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res2.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()), "errcode数据校验不一致");
                Assert.assertTrue(res2.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()), "ret数据校验不一致");
                JSONObject js = JSONObject.parseObject(params.get("expect_response").toString());
                if(null == js.get("data")){
                    Assert.assertNull(res2.get("data"),"data 字段对比错误");
                }else {
                    Assert.assertEquals(js.getString("data"),res2.get("data").toString(),"data 字段对比错误");
                }
                break;
            case 2:
                //发送响应
                Map res3 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res3);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res3.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res3.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data =(Map) res3.get("data");
                Assert.assertTrue(data.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNotNull(data.get("logoUrl"));
                Assert.assertNotNull(data.get("grayLogoUrl"));
                break;
            case 3:
                //发送响应
                Map res4 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res4);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res4.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res4.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data1 =(Map) res4.get("data");
                Assert.assertTrue(data1.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data1.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data1.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data1.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNull(data1.get("logoUrl"));
                Assert.assertNull(data1.get("grayLogoUrl"));
                break;
            case 4:
                //发送响应
                Map res5 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                System.out.println(params.get("case_name") + ":实际返回：" + res5);
                System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                //接口业务断言
                Assert.assertTrue(res5.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                Assert.assertTrue(res5.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()));
                //返回值data取值
                Map data3 =(Map) res5.get("data");
                Assert.assertTrue(data3.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()));
                Assert.assertTrue(data3.get("bankId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankId").toString()));
                Assert.assertTrue(data3.get("customerId").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("customerId").toString()));
                Assert.assertTrue(data3.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()));
                Assert.assertNotNull(data3.get("logoUrl"));
                Assert.assertNotNull(data3.get("grayLogoUrl"));
                //增加缓存断言
                String res_redis = redisHandler.hget(params.get("RedisKey").toString(),"4");
                Assert.assertNotNull(res_redis);
                String res_redis1 = redisHandler.hget("test:bank:table:code", "CMBCHINA");
                Assert.assertNotNull(res_redis1);
                break;
        }
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 设置银行卡为默认银行卡
     * @Other:
     */
    @Test(description = "设置银行卡为默认银行卡", dataProvider = "setCardAsDefault", dataProviderClass = BaseProvider.class)
    public void setCardAsDefaultTest(Map<String, Object> params) {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));

        logger.info("用例编号："+params.get("caseNo").toString());
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        System.out.println("参数信息为："+paraValues);
        try {
            //读取flag
            int flag = Integer.parseInt(params.get("flag").toString());
            switch (flag) {
                case 0:
                    //发送响应
                    Map res1 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    System.out.println(params.get("case_name") + ":实际返回：" + res1);
                    System.out.println(params.get("case_name") + ":期望数据：" + params.get("expect_response").toString());
                    Assert.assertTrue(res1.get("errmsg").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("msg")), "errmsg数据校验不一致");
                    break;
                case 1:
                    //发送响应
                    Map res3 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+res3);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    //接口业务断言
                    Assert.assertTrue(res3.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()), "errcode数据校验不一致");
                    Assert.assertTrue(res3.get("ret").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("ret").toString()), "ret数据校验不一致");
                    JSONObject js_res = JSONObject.parseObject(params.get("expect_response").toString());
                    if (null == js_res.get("data")) {
                        Assert.assertNull(res3.get("data"), "data 字段对比错误");
                    } else {
                        Assert.assertEquals(js_res.getString("data"), res3.get("data").toString(), "data 字段对比错误");
                    }
                    //对比mcc_customer_card
                    if (!"null".equals(params.get("mcc_customer_card").toString())){
                        String expect_sql_data = params.get("mcc_customer_card").toString();
                        JSONObject sql_field = JSONObject.parseObject(params.get("sql_field").toString());
                        String pre_sql = "SELECT $field$ FROM mcc_customer_card WHERE customer_id = \""+ sql_field.getString("customerId") + "\";";
                        Assert.assertTrue(compareMultiMysqlData(expect_sql_data,pre_sql),"mcc_customer_card数据对比不一致");
                    }
                    break;
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
        }
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 多条件统计银行卡数量
     * @Other:
     */
    @Test(description = "多条件统计银行卡数量", dataProvider = "countByCondition", dataProviderClass = BaseProvider.class)
    public void countByConditionTest(Map<String, Object> params) {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        logger.info("用例编号："+params.get("caseNo").toString());
        logger.info("参数信息为："+paraValues);
        try {
            int flag = Integer.parseInt(params.get("flag").toString());
            switch (flag){
                case 0:
                    Map response_false = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+response_false);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    Assert.assertTrue(response_false.get("errmsg").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("msg").toString()), "银行卡数量校验不一致");
                    break;
                case 1:
                    Map response = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+response);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    //接口返回断言
                    Assert.assertTrue(response.get("data").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("count").toString()), "银行卡数量校验不一致");
                    break;
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
        }
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 调用易宝支付查询银行卡信息
     * @Other:
     */
    @Test(description = "调用易宝支付查询银行卡信息", dataProvider = "getBankCardInfoByYeePay", dataProviderClass = BaseProvider.class)
    public void getBankCardInfoByYeePayTest(Map<String, Object> params) {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        logger.info("用例编号："+params.get("caseNo").toString());
        logger.info("参数信息为："+paraValues);
        try {
            int flag = Integer.parseInt(params.get("flag").toString());
            switch (flag){
                case 0:
                    Map response_false = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+response_false);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    Assert.assertTrue(response_false.get("errmsg").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("msg").toString()), "银行卡数量校验不一致");
                    break;
                case 1:
                    Map response1 = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+response1);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    //接口返回断言
                    Map response_data1 =(Map) response1.get("data");
                    Assert.assertTrue(response_data1.get("cardNo").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("cardNo").toString()));
                    Assert.assertTrue(response_data1.get("isvalid").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("isValid").toString()));
                    break;
                case 2:
                    Map response = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
                    logger.info("接口实际返回响应："+response);
                    logger.info("接口期望返回数据："+params.get("expect_response").toString());
                    //接口返回断言
                    Assert.assertTrue(response.get("errcode").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("errcode").toString()));
                    Assert.assertTrue(response.get("success").toString().equals(JSONObject.parseObject(params.get("expect_response").toString()).get("success").toString()));
                    Map response_data =(Map) response.get("data");
                    Assert.assertTrue(response_data.get("cardNo").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("cardNo").toString()), "银行卡信息校验不一致");
                    Assert.assertTrue(response_data.get("bankName").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankName").toString()), "银行卡信息校验不一致");
                    Assert.assertTrue(response_data.get("bankCode").toString().equals(JSONObject.parseObject(JSONObject.parseObject(params.get("expect_response").toString()).get("data").toString()).get("bankCode").toString()), "银行卡信息校验不一致");
                    break;
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
        }
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 保存银行卡mySQL
     */
    @Test(description = "saveBankCardTest",dataProvider = "BankCardRemoteServiceSaveBankCard",dataProviderClass = BaseProvider.class)
    public void saveBankCardTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        String mockId = MockUtils.addMock_http(params);
        logger.info("MockId:"+mockId);
        try {
            DubboClient dubboClient = getDubboClient(params);
            LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
            paraValues.put("bankCardDTO", JSON.parseObject(params.get("bankCardDTO").toString()));
            JSONObject res = JSON.parseObject(JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues)));
            System.out.println(params.get("flag") + ":response-->" + res.toString());
            String ExceptResponse = params.get("exceptResponse").toString();
            if(null!=ExceptResponse && ""!=ExceptResponse) {
                JSONObject value_exp = JSONObject.parseObject(ExceptResponse);
                for (String key : value_exp.keySet()) {
                    if (null != value_exp.get(key)) {
                        Assert.assertTrue(res.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }
                }
            }
            String exceptSQL = params.get("exceptSQL").toString();
            if (null != exceptSQL && "" != exceptSQL) {
                String mysql = DBUtils.queryDataMuil("select * from mcc_customer_card where customer_id = " + params.get("customerId"), "cardno");
                JSONObject exceptSQLJson = JSON.parseObject(exceptSQL);
                Assert.assertEquals(exceptSQLJson.getString("cardno"), mysql);
            }
            DBUtils.clearData(params.get("clearDataSQL").toString());
        }catch (RuntimeException e1){
            String message = e1.getMessage();
            logger.error(message);
            Assert.assertTrue(message.contains(params.get("exceptMessage").toString()));
        }finally {
            DBUtils.clearData(params.get("clearDataSQL").toString());
            if (!mockId.equals("false")){
                Integer resCode = MockUtils.delete_httpMock(mockId);
                Assert.assertTrue(resCode==200, "删除mock数据失败");
            }
        }
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 更新银行卡信息mySQL
     */
    @Test(description = "updateBankCardTest",dataProvider = "BankCardRemoteServiceUpdateBankCard",dataProviderClass = BaseProvider.class)
    public void updateBankCardTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        DubboClient dubboClient = getDubboClient(params);
        redisHandler.del("test.bankcard.update.times.auth.6217001020005561346.991237998");
        if (params.get("set_red").toString() != null && params.get("set_red").toString() != "") {
            int setRedis = Integer.valueOf(params.get("set_red").toString()).intValue();
            redisHandler.set("test.bankcard.update.times.auth.6217001020005561346.991237998",setRedis);
        }
        LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
        paraValues.put("bankCardDTO", JSON.parseObject(params.get("bankCardDTO").toString()));
        JSONObject res = JSON.parseObject(JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues)));
        System.out.println(params.get("flag") + ":response-->" + res.toString());
        String ExceptResponse = params.get("exceptResponse").toString();
        if(null!=ExceptResponse && ""!=ExceptResponse) {
            JSONObject value_exp = JSONObject.parseObject(ExceptResponse);
            for (String key : value_exp.keySet()) {
                if (null != value_exp.get(key)) {
                    Assert.assertTrue(res.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                }
            }
        }
        String exceptSQL = params.get("exceptSQL").toString();
        if(null!=exceptSQL && ""!=exceptSQL) {
            String mysql = DBUtils.queryDataMuil("select * from mcc_customer_card where id = " + params.get("id"),"branch_bank");
            JSONObject exceptSQLJson = JSON.parseObject(exceptSQL);
            Assert.assertEquals(exceptSQLJson.getString("branch_bank"),mysql);
        }
        DBUtils.clearData(params.get("clearDataSQL").toString());
        redisHandler.del("test.bankcard.update.times.sign.6217001020005561346.991237998");
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 删除银行卡信息isvail置为1
     */
    @Test(description = "removeBankCardByIdTest",dataProvider = "BankCardRemoteServiceRemoveBankCardById",dataProviderClass = BaseProvider.class)
    public void removeBankCardByIdTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
        paraValues.put("id", StringUtils.isBlank(params.get("id").toString())?null:params.get("id").toString());
        paraValues.put("customerId", StringUtils.isBlank(params.get("customerId").toString())?null:params.get("customerId").toString());
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag") + ":response-->" + res);
        String ExceptResponse = params.get("exceptResponse").toString();
        if(null!=ExceptResponse && ""!=ExceptResponse) {
            JSONObject value_exp = JSONObject.parseObject(ExceptResponse);
            JSONObject response = JSONObject.parseObject(res);
            for (String key : value_exp.keySet()) {
                if (null != value_exp.get(key)) {
                    Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                }
            }
        }
        String exceptSQL = params.get("exceptSQL").toString();
        if(null!=exceptSQL && ""!=exceptSQL) {
            String mysql = DBUtils.queryDataMuil("select * from mcc_customer_card where id = " + params.get("id"),"isValid");
            JSONObject exceptSQLJson = JSON.parseObject(exceptSQL);
            Assert.assertEquals(java.util.Optional.ofNullable(exceptSQLJson.getFloat("isValid")),Float.parseFloat(mysql));
        }
        DBUtils.clearData(params.get("clearDataSQL").toString());
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 重新鉴权银行卡
     * @涉及表: 
     */
    @Test(description = "reCardAuth",dataProvider = "reCardAuth",dataProviderClass = BaseProvider.class)
    public void reCardAuthTest(Map<String,Object> params) throws SQLException {
        logger.info("用例编号："+params.get("caseNo").toString());
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));

        //新增mock数据
        String mockId = MockUtils.addMock_http(params);
        logger.info("MockId:"+mockId);

        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        logger.info("参数信息为："+paraValues);
        try {
            Map response = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
            logger.info("接口实际返回响应："+response);
            logger.info("接口期望返回数据："+params.get("expect_response").toString());
            String ExpectResponse = params.get("expect_response").toString();
            //接口返回信息断言
            if (null != ExpectResponse &&""!=ExpectResponse){
                JSONObject value_exp = JSONObject.parseObject(params.get("expect_response").toString());
                for (String key : value_exp.keySet()){
                    if (null != value_exp.get(key)){
                        if(!key.equals("data")) {
                            Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                        }else {
                            JSONObject value_exp_data = JSONObject.parseObject(value_exp.get("data").toString());
                            for (String key_data : value_exp_data.keySet()) {
                                if (null != value_exp_data.get(key_data)) {
                                    Map value_dataRes = (Map) response.get("data");
                                    Assert.assertTrue(value_dataRes.get(key_data).toString().equals(value_exp_data.get(key_data).toString()), "接口返回data数据校验不一致");
                                }
                            }
                        }
                    } else {
                        Assert.assertNull(value_exp.get(key),"接口返回校验不一致");
                    }
                }
            }
            //mcc_customer_card 数据校验valid_result
            String exceptSQLData = params.get("mcc_customer_card").toString();
            if(null!=exceptSQLData && ""!=exceptSQLData) {
                String expect_sql_data = params.get("mcc_customer_card").toString();
                JSONObject sql_field = JSONObject.parseObject(params.get("sql_field").toString());
                String pre_sql = "SELECT $field$ FROM mcc_customer_card WHERE id = \""+ sql_field.getString("id") + "\";";
                Assert.assertTrue(compareMultiMysqlData(expect_sql_data,pre_sql),"mcc_customer_card:valid_result数据对比不一致");
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
            if (!mockId.equals("false")){
                Integer resCode = MockUtils.delete_httpMock(mockId);
                Assert.assertTrue(resCode==200, "删除mock数据失败");
            }
        }
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 更新银行卡渠道信息
     * @涉及表: 
     */
    @Test(description = "updateChannelId",dataProvider = "updateChannelId",dataProviderClass = BaseProvider.class)
    public void updateChannelIdTest(Map<String,Object> params) throws SQLException {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        logger.info("用例编号："+params.get("caseNo").toString());
        logger.info("参数信息为："+paraValues);
        try {
            Map response = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
            logger.info("接口实际返回响应："+response);
            logger.info("接口期望返回数据："+params.get("expect_response").toString());
            String ExpectResponse = params.get("expect_response").toString();
            //接口返回信息断言
            if (null != ExpectResponse &&""!=ExpectResponse){
                JSONObject value_exp = JSONObject.parseObject(params.get("expect_response").toString());
                logger.info(value_exp);
                for (String key : value_exp.keySet()){
                    if (null != value_exp.get(key)){
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()),"接口返回校验不一致");
                    }else {
                        Assert.assertNull(value_exp.get(key),"接口返回校验不一致");
                    }
                }
//                Assert.assertTrue(response.toString().equals(params.get("expect_response").toString()), "接口返回信息校验不一致");
            }
            //mcc_customer_card 数据校验channel-id
            String exceptSQLData = params.get("mcc_customer_card").toString();
            if(null!=exceptSQLData && ""!=exceptSQLData) {
                String expect_sql_data = params.get("mcc_customer_card").toString();
                JSONObject sql_field = JSONObject.parseObject(params.get("sql_field").toString());
                String pre_sql = "SELECT $field$ FROM mcc_customer_card WHERE id = \""+ sql_field.getString("id") + "\";";
                Assert.assertTrue(compareMultiMysqlData(expect_sql_data,pre_sql),"mcc_customer_card数据对比不一致");
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
        }
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 查询银行卡详细信息
     */
    @Test(description = "getBankCardInfo",dataProvider = "getBankCardInfo",dataProviderClass = BaseProvider.class)
    public void getBankCardInfoTest(Map<String,Object> params) throws SQLException {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //新增mock数据
        String mockId = MockUtils.addMock_http(params);
        logger.info("MockId:"+mockId);

        //构造mock数据
        if (null !=params.get("RedisKey")) {
            redisHandler.del(params.get("RedisKey").toString());
            String resRedis_CodeNoData = redisHandler.hget(params.get("RedisKey").toString(), "CMBCHINA");
            Assert.assertNull(resRedis_CodeNoData);
        }
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //读取参数信息
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        logger.info("用例编号："+params.get("caseNo").toString());
        logger.info("参数信息为："+paraValues);
        try {
            Map response = (Map) dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues);
            logger.info("接口实际返回响应："+response);
            logger.info("接口期望返回数据："+params.get("expect_response").toString());
            String ExpectResponse = params.get("expect_response").toString();
            //接口返回信息断言
            if (null != ExpectResponse &&""!=ExpectResponse){
                JSONObject value_exp = JSONObject.parseObject(params.get("expect_response").toString());
                for (String key : value_exp.keySet()){
                    if (null != value_exp.get(key)){
                        if(!key.equals("data")) {
                            Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                        }else {
                            JSONObject value_exp_data = JSONObject.parseObject(value_exp.get("data").toString());
                            for (String key_data : value_exp_data.keySet()) {
                                if (null != value_exp_data.get(key_data)) {
                                    Map value_dataRes = (Map) response.get("data");
                                    Assert.assertTrue(value_dataRes.get(key_data).toString().equals(value_exp_data.get(key_data).toString()), "接口返回data数据校验不一致");
                                }
                            }
                        }
                    } else {
                        Assert.assertNull(value_exp.get(key),"接口返回校验不一致");
                    }
                }
            }
        }catch (Exception e){
            logger.info("ExceptionMassage:"+e.getMessage());
            String ee = params.get("expect_Exception").toString();
            if (e.getMessage().contains(ee)){
                Assert.assertEquals(true,true);
            }else {
                Assert.assertEquals(true,false);
            }
        }finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
            if (!mockId.equals("false")){
                Integer resCode = MockUtils.delete_httpMock(mockId);
                Assert.assertTrue(resCode==200, "删除mock数据失败");
            }
        }
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 查询银行卡的配置信息logourl等
     */
    @Test(description = "getBankCardByIdTest",dataProvider = "BankCardRemoteServiceGetBankCardById",dataProviderClass = BaseProvider.class)
    public void getBankCardByIdTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        redisHandler.hdel("test:bank:table:id","112233");
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
        paraValues.put("id", StringUtils.isBlank(params.get("id").toString())?null:params.get("id").toString());
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag") + ":response-->" + res);
        String exceptResponse = params.get("exceptResponse").toString();
        if(null!=exceptResponse && ""!=exceptResponse) {
            JSONObject exceptResponseJson = JSON.parseObject(exceptResponse);
            JSONObject resJson = JSON.parseObject(res);
            if(null!=exceptResponseJson.getString("data") && ""!=exceptResponseJson.getString("data")){
                JSONObject resJsonData = JSON.parseObject(resJson.getString("data"));
                JSONObject exceptData =JSON.parseObject(exceptResponseJson.getString("data"));
                Assert.assertEquals(resJsonData.getString("grayLogoUrl"),exceptData.getString("grayLogoUrl"));
            }else if(null==exceptResponseJson.getString("data")){
                JSONObject value_exp = JSONObject.parseObject(exceptResponse);
                JSONObject response = JSONObject.parseObject(res);
                for (String key : exceptResponseJson.keySet()) {
                    if (null != value_exp.get(key)) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }
                }
            }
        }
        String exceptRedis = params.get("exceptRedis").toString();
        if(null!=exceptRedis && ""!=exceptRedis) {
            JSONObject exceptRedisJson = JSON.parseObject(exceptRedis);
            String redisRes = redisHandler.hget("test:bank:table:id","112233");
            JSONObject redisJson = JSON.parseObject(redisRes);
            Assert.assertEquals(redisJson.getString("bankName"),exceptRedisJson.get("bankName").toString());
        }
        DBUtils.clearData(params.get("clearDataSQL").toString());
        redisHandler.hdel("test:bank:table:id","112233");
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 根据id批量查询银行卡信息
     */
    @Test(description = "getBankCardListByIdsTest",dataProvider = "BankCardRemoteServiceGetBankCardListByIds",dataProviderClass = BaseProvider.class)
    public void getBankCardListByIdsTest(Map<String,Object> params) throws SQLException {
        try {
            DBUtils.clearData(params.get("clearDataSQL").toString());
            Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
            DubboClient dubboClient = getDubboClient(params);
            LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
            String Ids = params.get("ids").toString();
            if(""!=Ids && null!=Ids ) {
                String[] pram = Ids.split(",");
                paraValues.put("ids", Arrays.asList(pram));
            }else{
                paraValues.put("ids",null);
            }
            String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
            System.out.println(params.get("flag") + ":response-->" + res);
            String ExceptResponse = params.get("exceptResponse").toString();
            if(null!=ExceptResponse && ""!=ExceptResponse) {
                JSONObject value_exp = JSONObject.parseObject(ExceptResponse);
                JSONObject response = JSONObject.parseObject(res);
                for (String key : value_exp.keySet()) {
                    if (null != value_exp.get(key)) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }
                }
            }
        }catch (RuntimeException e1){
            String message = e1.getMessage();
            logger.error(message);
        }finally{
            DBUtils.clearData(params.get("clearDataSQL").toString());
        }
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 多条件查询银行卡信息
     */
    @Test(description = "getBankCardListTest",dataProvider = "BankCardRemoteServiceGetBankCardList",dataProviderClass = BaseProvider.class)
    public void getBankCardListTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        redisHandler.hdel("test:bank:table:id","51");
        redisHandler.hdel("test:bank:table:code","CCBBAA");
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
        paraValues.put("bankCardQuery", JSON.parseObject(params.get("bankCardQuery").toString()));
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag") + ":response-->" + res);
        String ExpectResponse = params.get("exceptResponse").toString();
        JSONObject response =JSONObject.parseObject(res);
        if(null!=ExpectResponse && ""!=ExpectResponse) {
            JSONObject value_exp = JSONObject.parseObject(params.get("exceptResponse").toString());
            for (String key : value_exp.keySet()){
                if (null != value_exp.get(key)){
                    if(!key.equals("data")) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }else {
                        if(0!=value_exp.getJSONArray("data").size()) {
                            JSONObject value_exp_data = JSONObject.parseObject(value_exp.getJSONArray("data").get(0).toString());
                            for (String key_data : value_exp_data.keySet()) {
                                if (null != value_exp_data.get(key_data)) {
                                    JSONObject value_dataRes = JSONObject.parseObject(response.getJSONArray("data").get(0).toString());
                                    Assert.assertTrue(value_dataRes.get(key_data).toString().equals(value_exp_data.get(key_data).toString()), "接口返回data数据校验不一致");
                                }
                            }
                        }
                    }
                }
            }
        }
        String exceptRedis = params.get("exceptRedis").toString();
        if(null!=exceptRedis && ""!=exceptRedis) {
            JSONObject exceptRedisJson = JSON.parseObject(exceptRedis);
            String redisRes = redisHandler.hget("test:bank:table:id","51");
            JSONObject redisJson = JSON.parseObject(redisRes);
            Assert.assertEquals(redisJson.getString("logoUrl"),exceptRedisJson.get("logoUrl").toString());
        }
        DBUtils.clearData(params.get("clearDataSQL").toString());
        redisHandler.hdel("test:bank:table:id","51");
        redisHandler.hdel("test:bank:table:code","CCBBAA");
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 多条件查询最近一张银行卡
     */
    @Test(description = "getRecentBankCardTest",dataProvider = "BankCardRemoteServiceGetRecentBankCard",dataProviderClass = BaseProvider.class)
    public void getRecentBankCardTest(Map<String,Object> params) throws SQLException {
        DBUtils.clearData(params.get("clearDataSQL").toString());
        Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        redisHandler.hdel("test:bank:table:id","51");
        redisHandler.hdel("test:bank:table:code","CCBBAA");
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
        paraValues.put("bankCardQuery", JSON.parseObject(params.get("bankCardQuery").toString()));
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag") + ":response-->" + res);
        String exceptResponse = params.get("exceptResponse").toString();
        if(null!=exceptResponse && ""!=exceptResponse) {
            JSONObject exceptResponseJson = JSON.parseObject(exceptResponse);
            JSONObject resJson = JSON.parseObject(res);
            if(null!=exceptResponseJson.getString("data") && ""!=exceptResponseJson.getString("data")){
                JSONObject resJsonData = JSON.parseObject(resJson.getString("data"));
                JSONObject exceptData =JSON.parseObject(exceptResponseJson.getString("data"));
                Assert.assertEquals(resJsonData.getString("bankName"),exceptData.getString("bankName"));
            }else if(null==exceptResponseJson.getString("data")){
                JSONObject value_exp = JSONObject.parseObject(exceptResponse);
                JSONObject response = JSONObject.parseObject(res);
                for (String key : exceptResponseJson.keySet()) {
                    if (null != value_exp.get(key)) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }
                }
            }
        }
        String exceptRedis = params.get("exceptRedis").toString();
        if(null!=exceptRedis && ""!=exceptRedis) {
            JSONObject exceptRedisJson = JSON.parseObject(exceptRedis);
            String redisRes = redisHandler.hget("test:bank:table:id","51");
            JSONObject redisJson = JSON.parseObject(redisRes);
            Assert.assertEquals(redisJson.getString("bankName"),exceptRedisJson.get("bankName").toString());
        }
        DBUtils.clearData(params.get("clearDataSQL").toString());
        redisHandler.hdel("test:bank:table:id","51");
        redisHandler.hdel("test:bank:table:code","CCBBAA");
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
