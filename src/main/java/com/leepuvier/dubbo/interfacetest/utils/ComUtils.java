package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:33 PM
 * @ContentUse :
 */

@Slf4j
public class ComUtils {

    //list
    public   static boolean listCompare(List<String> list1, List<String> list2) {
        if(list1.size()!=list2.size()){
            log.info("两个List的Size不一致");
            log.info(String.valueOf(list1.size()));
            log.info(String.valueOf(list2.size()));
            return false;
        }else {
            for(String item:list1){
                if(!list2.contains(item)){
                    return false;
                }
            }
        }
        return true;
    }
    //set
    public   static boolean setCompare(Set<String> set1, Set<String> set2) {
        if(set1.size()!=set2.size()){
            return false;
        }else {
            for(String item:set2){
                if(!set1.contains(item)){
                    return false;
                }
            }
        }
        return true;
    }
    //json对比
    public static boolean jsonCompare(JSONObject json1, JSONObject json2){
        try {
            Set<String> json1Keys = json1.keySet();
            Set<String> json2Keys = json2.keySet();
            if(!setCompare(json1Keys,json2Keys)){
                return false;
            }else {
                for (String item:json1Keys){
                    if(!json1.get(item).toString().equals(json2.get(item).toString())){
                        return false;
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Exceptions----"+e);
            return false;
        }
        return true;
    }

    //json对比，except中的内容与接口返回至对比，非全量
    public static boolean jsonCompairExpect(JSONObject response,JSONObject expect) {
        try {
            Set<String> responseKeys = response.keySet();
            Set<String> exceptKeys = expect.keySet();
            for (String key : exceptKeys) {
                if (!responseKeys.contains(key)) {
                    return false;
                }
            }
            for (String key : exceptKeys) {
                if (!response.get(key).toString().equals(expect.get(key).toString())) {
                    return false;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // JSONArray对比，JAONArray下面只有一层元素，是字典
    // jsonA1 第一个JSONArray
    // jsonA2 第二个JSONArray
    // key    JSONArray中各字典元素的唯一标识字段
    public static boolean jsonArrayCompare(JSONArray jsonA1, JSONArray jsonA2, String key){
        try{

            if (jsonA1.size() != jsonA2.size()){
                System.out.println("两个 jsonArray 长度不一样");
                return false;
            }

            for (int i = 0; i < jsonA1.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < jsonA2.size(); j++) {
                    // 获取JSONArray中的一个元素
                    JSONObject jsonObjectA1 = jsonA1.getJSONObject(i);
                    JSONObject jsonObjectA2 = jsonA2.getJSONObject(j);

                    // 获取元素中的key ，这个是唯一标识
                    String valueA1 = jsonObjectA1.getString(key);
                    String valueA2 = jsonObjectA2.getString(key);

                    // 当两者的channelOrder相等时进行比较
                    boolean equalflag = true;
                    if(valueA1.equals(valueA2)){
                        flag = true;
                        for(String dictKey:jsonObjectA1.keySet()){
                            if(!jsonObjectA1.getString(dictKey).equals(jsonObjectA2.getString(dictKey))){
                                equalflag = false;
                                System.out.println("不一样的key："+dictKey);
                                System.out.println("不一样的value："+jsonObjectA1.getString(dictKey));
                                System.out.println("不一样位value："+jsonObjectA2.getString(dictKey));
                                break;
                            }
                        }
                        if (!equalflag){
                            System.out.println("jsonArray中的元素不一样");
                            return false;
                        }
                    }
                }
                if(!flag){
                    System.out.println("jsonA1中存在jsonA2没有的元素");
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 对比mysql数据库
    public static Boolean compareMysqlData(JSONObject mysqlData, String specialSql) throws SQLException {
        List<String> keyList = new ArrayList<String>();
        String mysqlField = "";
        for (String key:mysqlData.keySet()){
            keyList.add(key);
            mysqlField = mysqlField+key+",";
        }

        String sql = specialSql.replace("$field$",mysqlField.substring(0,mysqlField.length() - 1));

        log.info("sql语句："+sql);

        ResultSet resultSet = DBUtils.queryDataNoConn(sql);
        if(null != resultSet && !"".equals(resultSet)){
            while (resultSet.next()){
                for (String key:mysqlData.keySet()) {
                    String expectedValue = mysqlData.get(key).toString();
                    String mysqlValue = resultSet.getString(keyList.indexOf(key)+1);
                    if(!expectedValue.equals(mysqlValue)){
                        log.info("expectedValue:"+expectedValue);
                        log.info("mysqlValue:"+mysqlValue);
                        return false;
                    }
                }
                return true;
            }
        }else {
            log.info("数据库无记录");
            return false;
        }
        return false;
    }

    //遍历数据库信息，转化格式
    public static JSONArray resultSetToJson(ResultSet rs) throws SQLException, JSONException {
        // json数组
        JSONArray array = new JSONArray();
        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.add(jsonObj);
        }
        return array;
    }
    //多条对比数据库值
    public static Boolean compareMultiMysqlData(String mysqlDataArray, String specialSql) throws SQLException {
        JSONArray ja = JSONObject.parseArray(mysqlDataArray);
        JSONObject mysqlData = ja.getJSONObject(0);
        List<String> keyList = new ArrayList<String>();
        String mysqlField = "";
        for (String key : mysqlData.keySet()) {
            keyList.add(key);
            mysqlField = mysqlField + key + ",";
        }

        String sql = specialSql.replace("$field$", mysqlField.substring(0, mysqlField.length() - 1));

        log.info("sql语句：" + sql);

        ResultSet resultSet = DBUtils.queryDataNoConn(sql);
        if (null != resultSet && resultSet.next()) {
            int i = 0;
            resultSet.previous();
            while (resultSet.next()) {
                mysqlData = ja.getJSONObject(i);
                for (String key : mysqlData.keySet()) {
                    String expectedValue = mysqlData.get(key).toString();
                    String mysqlValue = resultSet.getString(keyList.indexOf(key) + 1);
                    if (!expectedValue.equals(mysqlValue)) {
                        log.info("key:" + key);
                        log.info("expectedValue:" + expectedValue);
                        log.info("mysqlValue:" + mysqlValue);
                        return false;
                    }
                }
                i++;
            }
            return true;
        } else {
            log.info("数据库无记录");
            return false;
        }
    }
}
