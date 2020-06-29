package com.leepuvier.dubbo.interfacetest.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.leepuvier.dubbo.client.utils.DubboClient;
import com.leepuvier.dubbo.interfacetest.common.BaseCase;
import com.leepuvier.dubbo.interfacetest.common.BaseProvider;
import com.leepuvier.dubbo.interfacetest.utils.ComUtils;
import com.leepuvier.dubbo.interfacetest.utils.DBUtils;
import com.leepuvier.dubbo.interfacetest.utils.RedisHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  10:01 PM
 * @ContentUse :
 */

@Slf4j
public class RemoteServiceInsert extends BaseCase {

    @Resource
    RedisHandler redisHandler;

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * Comment：添加银行列表
     **/
    @Test(description = "addIfAbsentTest",dataProvider = "addIfAbsentTest",dataProviderClass = BaseProvider.class)
    public void addIfAbsentTest(Map<String,Object> params) throws SQLException {
        //清理数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //初始化数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //删除缓存
        redisHandler.hdel("test:bank:table:code","test_null");
        redisHandler.hdel("test:bank:table:id", "55");
        //正常调用
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), StringUtils.isBlank(params.get("paraType").toString())?null:params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag")+":response-->"+res);
        //预期结果断言
        //Assert.assertEquals(true, params.get("except").toString().equals(res));
        //数据库预期结果
        String exceptSQL= params.get("exceptSQL").toString();
        //数据库查询语句
        String seSQL= params.get("selSQL").toString();

        //数据库断言
        if (null != exceptSQL && "" != exceptSQL && null != seSQL) {
            String mysql = DBUtils.queryDataMuil(seSQL, "bank_code");
            System.out.println("数据库查询结果："+mysql);
            Assert.assertEquals(true, mysql.equals(exceptSQL));
        }
        boolean r=params.get("caseNo").equals("2");
        if(r) {
            String red_1 = redisHandler.hget("test:bank:table:code", "test_null");
            System.out.println("缓存："+red_1);
            String red_2 = redisHandler.hget("test:bank:table:id", "55");
            System.out.println("缓存："+red_2);
            String  exceptRed=params.get("exceptRed").toString();
            Assert.assertEquals(true,red_1.equals(exceptRed));
            Assert.assertEquals(true,red_2.equals(exceptRed));
        }
        //返回结果断言
        String ExpectResponse = params.get("expect").toString();
        JSONObject  response =JSONObject.parseObject(res);
        //接口返回信息断言
        if (null != ExpectResponse &&""!=ExpectResponse){
            JSONObject value_exp = JSONObject.parseObject(params.get("expect").toString());
            System.out.println(value_exp);
            for (String key : value_exp.keySet()){
                if (null != value_exp.get(key)){
                    if(!key.equals("data")) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }else {
                        JSONObject value_exp_data = JSONObject.parseObject(value_exp.get("data").toString());
                        System.out.println(value_exp_data);
                        for (String key_data : value_exp_data.keySet()) {
                            if (null != value_exp_data.get(key_data)) {
                                Map value_dataRes = (Map) response.get("data");
                                logger.info(value_dataRes);
                                Assert.assertTrue(value_dataRes.get(key_data).toString().equals(value_exp_data.get(key_data).toString()), "接口返回data数据校验不一致");
                            }
                        }
                    }
                } else {
                    Assert.assertNull(value_exp.get(key),"接口返回校验不一致");
                }
            }
        }

        //删除缓存
        redisHandler.hdel("test:bank:table:code","test_null");
        //清理数据,
        DBUtils.clearData(params.get("clearDataSQL").toString());
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * Comment：更新银行列表
     **/
    @Test(description = "updateBankCardConfigTest",dataProvider = "updateBankCardConfigTest",dataProviderClass = BaseProvider.class)
    public void updateBankCardConfigTest(Map<String,Object> params) throws SQLException {
        //清理数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //初始化数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //删除缓存
        redisHandler.hdel("test:bank:table:code","testcode");
        redisHandler.hdel("test:bank:table:id","50");
        //插入缓存
        redisHandler.hset("test:bank:table:code","testcode","{\"bankCode\":\"testcode\",\"bankName\":\"测试银行\",\"createTime\":null,\"createdAt\":1574414509000,\"grayLogoUrl\":\"https://test.png\",\"id\":100,\"logoUrl\":\"https://test.png\",\"updatedAt\":1574409658000}");
        redisHandler.hset("test:bank:table:id","50","{\"bankCode\":\"testcode\",\"bankName\":\"测试银行\",\"createTime\":null,\"createdAt\":1574414509000,\"grayLogoUrl\":\"https://test.png\",\"id\":100,\"logoUrl\":\"https://test.png\",\"updatedAt\":1574409658000}");
        //String resg=redisHandler.hget("test:bank:table:code","testcode");
        //System.out.println(resg);
        //正常调用
        DubboClient dubboClient = getDubboClient(params);
        LinkedHashMap<String, Object> paraValues = JSON.parseObject(params.get("p_params").toString(),new TypeReference<LinkedHashMap<String,Object>>(){});
        String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), StringUtils.isBlank(params.get("paraType").toString())?null:params.get("paraType").toString(), paraValues));
        System.out.println(params.get("flag")+":response-->"+res);
        //预期结果断言
        // Assert.assertEquals(true, params.get("except").toString().equals(res));
        //数据库预期结果
        String exceptSQL= params.get("exceptSQL").toString();
        //数据库查询语句
        String seSQL= params.get("selSQL").toString();
        //数据库断言
        if (null != exceptSQL && "" != exceptSQL && null != seSQL) {
            String mysql = DBUtils.queryDataMuil(seSQL, "bank_name");
            System.out.println("数据库查询结果："+mysql);
            Assert.assertEquals(true, mysql.equals(exceptSQL));
        }
        //缓存断言
        boolean r =params.get("caseNo").toString().equals("3");
        if(r) {
            String resget_1=redisHandler.hget("test:bank:table:code","testcode");
            String resget_2=redisHandler.hget("test:bank:table:id","50");
            System.out.println("缓存："+resget_1);
            System.out.println("缓存："+resget_2);
            boolean red_1 =   null==resget_1;
            boolean red_2 =   null==resget_2;
            Assert.assertEquals(true,red_1);
            Assert.assertEquals(true,red_2);
        }
        //返回结果断言
        String ExpectResponse = params.get("expect").toString();
        JSONObject  response =JSONObject.parseObject(res);
        //接口返回信息断言
        if (null != ExpectResponse &&""!=ExpectResponse){
            JSONObject value_exp = JSONObject.parseObject(params.get("expect").toString());
            System.out.println(value_exp);
            for (String key : value_exp.keySet()){
                if (null != value_exp.get(key)){
                    if(!key.equals("data")) {
                        Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                    }else {
                        JSONObject value_exp_data = JSONObject.parseObject(value_exp.get("data").toString());
                        System.out.println(value_exp_data);
                        for (String key_data : value_exp_data.keySet()) {
                            if (null != value_exp_data.get(key_data)) {
                                Map value_dataRes = (Map) response.get("data");
                                logger.info(value_dataRes);
                                Assert.assertTrue(value_dataRes.get(key_data).toString().equals(value_exp_data.get(key_data).toString()), "接口返回data数据校验不一致");
                            }
                        }
                    }
                } else {
                    Assert.assertNull(value_exp.get(key),"接口返回校验不一致");
                }
            }
        }
        //删除缓存
        redisHandler.hdel("test:bank:table:code","testcode");
        redisHandler.hdel("test:bank:table:id","50");

        //清理数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 删除银行卡配置
     * 涉及表：
     */
    @Test(description = "deleteBankCardConfig",dataProvider = "deleteBankCardConfig",dataProviderClass = BaseProvider.class)
    public void deleteBankCardConfigTest(Map<String,Object> params) throws SQLException {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //构造redis数据
        String RedisData_bankId = params.get("preRedisData_bankId").toString();
        String RedisData_bankCode = params.get("preRedisData_bankCode").toString();
        String RedisData_bankList = params.get("preRedisData_bankList").toString();
        if (null !=RedisData_bankId && ""!=RedisData_bankId){
            redisHandler.hset(params.get("RedisKey_bankId").toString(),"4",JSONObject.parseObject(params.get("preRedisData_bankId").toString()));
            String resRedis_Id = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
            Assert.assertNotNull(resRedis_Id);
        }else {
            String resRedis_IDNoData = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
            Assert.assertNull(resRedis_IDNoData);
        }
        if (null != RedisData_bankCode && ""!=RedisData_bankCode){
            redisHandler.hset(params.get("RedisKey_Code").toString(),"CMBCHINA",JSONObject.parseObject(params.get("preRedisData_bankCode").toString()));
            String resRedis_Code = redisHandler.hget(params.get("RedisKey_Code").toString(), "CMBCHINA");
            Assert.assertNotNull(resRedis_Code);
        }else {
            String resRedis_CodeNoData = redisHandler.hget(params.get("RedisKey_Code").toString(), "CMBCHINA");
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
            if (null !=RedisData_bankId && ""!=RedisData_bankId) {
                //增加缓存断言
                String res_redis_bankId = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
                Assert.assertNull(res_redis_bankId, "bankId-Redis数据校验不一致");
            }
            if (null != RedisData_bankCode && ""!=RedisData_bankCode) {
                String res_redis_bankCode = redisHandler.hget(params.get("RedisKey_Code").toString(), "CMBCHINA");
                Assert.assertNull(res_redis_bankCode, "bankCode-Redis数据校验不一致");
            }
            //断言数据库信息
            String SQL = "SELECT COUNT(*) FROM mcc_bank_config WHERE id =4;";
            if (params.get("flag").toString().equals("0")){
                ResultSet Result = DBUtils.queryDataNoConn(SQL);
                JSONArray result = ComUtils.resultSetToJson(Result);
//                logger.info("--------"+result);
                Assert.assertTrue(JSONObject.parseObject(result.get(0).toString()).get("COUNT(*)").toString().equals("0"));
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
            redisHandler.del(params.get("RedisKey_bankId").toString());
            redisHandler.del(params.get("RedisKey_Code").toString());
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
     * @Conment: 查询银行卡配置
     * 涉及表：
     */
    @Test(description = "getBankConfigById",dataProvider = "getBankConfigById",dataProviderClass = BaseProvider.class)
    public void getBankConfigByIdTest(Map<String,Object> params) throws SQLException {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //构造redis数据
        redisHandler.del(params.get("RedisKey_bankId").toString());
        redisHandler.del(params.get("RedisKey_bankcode").toString());
        String RedisData_bankId = params.get("preRedisData_bankId").toString();
        if (null !=RedisData_bankId && ""!=RedisData_bankId){
            redisHandler.hset(params.get("RedisKey_bankId").toString(),"4",JSONObject.parseObject(params.get("preRedisData_bankId").toString()));
            String resRedis_Id = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
            Assert.assertNotNull(resRedis_Id);
        }else {
            String resRedis_IDNoData = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
            Assert.assertNull(resRedis_IDNoData);
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
            //增加缓存断言
            if (params.get("flag").toString().equals("0")) {
                String res_redis_bankId = redisHandler.hget(params.get("RedisKey_bankId").toString(), "4");
                Assert.assertNotNull(res_redis_bankId, "bankId-Redis数据校验不一致");
                String res_redis_bankCode = redisHandler.hget(params.get("RedisKey_bankcode").toString(), "CMBCHINA");
                Assert.assertNotNull(res_redis_bankCode, "bankCode-Redis数据校验不一致");
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
            redisHandler.del(params.get("RedisKey_bankId").toString());
            redisHandler.del(params.get("RedisKey_bankcode").toString());
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
     * 多条查询
     */
    @Test(description = "BankRemoteServiceFindTest",dataProvider = "BankRemoteServiceFindTest",dataProviderClass = BaseProvider.class)
    public void BankRemoteServiceFindTest(Map<String,Object> params) throws SQLException {
        try {
            //执行请求
            DubboClient dubboClient = getDubboClient(params);
            LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
            paraValues.put("bankConfigQuery", JSON.parseObject(params.get("bankConfigQuery").toString()));
            String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
            System.out.println(params.get("flag") + ":response-->" + res);
            String ExceptResponse = params.get("exceptResponse").toString();
            JSONObject value_exp = JSONObject.parseObject(ExceptResponse);
            JSONObject response = JSONObject.parseObject(res);
            for (String key : value_exp.keySet()) {
                if (null != value_exp.get(key)) {
                    Assert.assertTrue(response.get(key).toString().equals(value_exp.get(key).toString()), "接口返回数据校验不一致");
                }
            }
        }catch (RuntimeException e1){
            String message = e1.getMessage();
            logger.error(message);
        }
    }
    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 查询全部
     */
    @Test(description = "BankRemoteServiceListTest",dataProvider = "BankRemoteServiceListTest",dataProviderClass = BaseProvider.class)
    public void BankRemoteServiceListTest(Map<String,Object> params) throws SQLException {
        try {
            DBUtils.clearData(params.get("clearDataSQL").toString());
            Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
            String clearRedis = params.get("clearRedis").toString();
            if(null!=clearRedis && ""!=clearRedis) {
                redisHandler.del(params.get("clearRedis").toString());
            }
            DubboClient dubboClient = getDubboClient(params);
            String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(),null,null));
            System.out.println(params.get("flag") + ":response-->" + res);
            String exceptResponse = params.get("exceptResponse").toString();
            JSONObject exceptResponseJson = JSON.parseObject(exceptResponse);
            JSONObject resJson = JSON.parseObject(res);
            Assert.assertEquals(resJson.getString("success"),exceptResponseJson.getString("success"));
        }
        catch (RuntimeException e1){
            String message = e1.getMessage();
            logger.error(message);
        }
    }


    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * 根据批量code查询
     */
    @Test(description = "findBankConfigByCodesTest",dataProvider = "findBankConfigByCodesTest",dataProviderClass = BaseProvider.class)
    public void findBankConfigByCodesTest(Map<String,Object> params) throws SQLException {
        try {
            DBUtils.clearData(params.get("clearDataSQL").toString());
            Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
            DubboClient dubboClient = getDubboClient(params);
            LinkedHashMap<String, Object> paraValues = new LinkedHashMap<>();
            String Ids = params.get("codes").toString();
            if(""!=Ids && null!=Ids ) {
                String[] pram = Ids.split(",");
                paraValues.put("codes", Arrays.asList(pram));
            }else{
                paraValues.put("codes",null);
            }
            String res = JSONObject.toJSONString(dubboClient.call(params.get("method").toString(), params.get("paraType").toString(), paraValues));
            JSONObject response = JSONObject.parseObject(res);
            System.out.println(params.get("flag") + ":response-->" + res);
            String ExpectResponse = params.get("expect_response").toString();
            if (null != ExpectResponse && ""!=ExpectResponse){
                JSONObject value_exp = JSONObject.parseObject(params.get("expect_response").toString());
                for (String key : value_exp.keySet()){
                    if (null != value_exp.get(key)){
                        if(!key.equals("data")) {
                            Assert.assertEquals(response.get(key).toString(), value_exp.get(key).toString());
                        }
                    }
                }
            }
        }catch (RuntimeException e1){
            String message = e1.getMessage();
            logger.error(message);
        }finally{
            DBUtils.clearData(params.get("clearDataSQL").toString());
            Assert.assertEquals(true,DBUtils.initData(params.get("preDataSQL").toString()));
        }
    }

    /**
     * @Author:    LeePuvier
     * @Date: 2020/6/29
     * @Update:
     * 涉及表:
     * @Conment: 通过codes查询多条配置信息
     * 涉及表：mcc_bank_config
     */
    @Test(description = "getBankConfigByCode",dataProvider = "getBankConfigByCode",dataProviderClass = BaseProvider.class)
    public void getBankConfigByCodeTest(Map<String,Object> params) throws SQLException {
        //清理SQL数据
        DBUtils.clearData(params.get("clearDataSQL").toString());
        //构造SQL数据
        Assert.assertEquals(true, DBUtils.initData(params.get("preDataSQL").toString()));
        //执行请求
        DubboClient dubboClient = getDubboClient(params);
        //构造redis数据
        redisHandler.del(params.get("RedisKey_bankId").toString());
        redisHandler.del(params.get("RedisKey_bankcode").toString());
        String RedisData_bankCode = params.get("preRedisData_bankCode").toString();
        if (null !=RedisData_bankCode && ""!=RedisData_bankCode){
            redisHandler.hset(params.get("RedisKey_bankcode").toString(),"POST",JSONObject.parseObject(params.get("preRedisData_bankCode").toString()));
            String resRedis_Code = redisHandler.hget(params.get("RedisKey_bankcode").toString(), "POST");
            Assert.assertNotNull(resRedis_Code);
        }else {
            String resRedis_IDNoData = redisHandler.hget(params.get("RedisKey_bankcode").toString(), "POST");
            Assert.assertNull(resRedis_IDNoData);
        }

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
        } finally {
            if (DBUtils.clearData(params.get("clearDataSQL").toString())){
                logger.info("执行测试用例结束");
                Assert.assertTrue(true);
            }else {
                logger.info("执行测试用例失败");
                Assert.assertTrue(false);
            }
        }
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
