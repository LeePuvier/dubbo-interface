package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parse;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/7/8  3:16 PM
 * @ContentUse :
 */

@Slf4j
public class DataUtils {

    /**
     * 对比实际接口返回值和期望接口返回值（支持JSONObject和JSONArray格式的校验）
     * @param actInfo 实际接口返回
     * @param expectInfo 期望接口返回
     * @return
     */
    public static Boolean compareJsonResult(String actInfo, String expectInfo) {
        boolean flag = false;
        int len = 0;
        List<Boolean> assertResult = new ArrayList<Boolean>();
        Object actObject = parse(actInfo.trim());
        Object expectObject = parse(expectInfo.trim());
        //遍历第二个Json Key，在第一个Json中取对应Key，替换对应的Value
        if(actObject instanceof JSONObject && expectObject instanceof JSONObject) {
            JSONObject jsonObject1 = (JSONObject) actObject;
            JSONObject jsonObject2 = (JSONObject) expectObject;
            for (Map.Entry<String, Object> entry : jsonObject2.entrySet()) {
                try {
                    // 判断下一级Json格式
                    Object o2 = entry.getValue();

                    if(o2 instanceof String) {
                        flag = jsonObject1.get(entry.getKey()).toString().equals(entry.getValue().toString());
                        assertResult.add(flag);
                        log.info(" 实际字段内容为：" + jsonObject1.get(entry.getKey()) + "  期望字段内容为：" + entry.getValue() + "  对比结果为：" + flag);
                        if (!flag){
                            return flag;
                        }
                    } else if(o2 instanceof Integer){
                        flag = jsonObject1.get(entry.getKey()).toString().equals(entry.getValue().toString());
                        assertResult.add(flag);
                        log.info(" 实际字段数值为：" + jsonObject1.get(entry.getKey()) + "  期望字段数值为：" + entry.getValue() + "  对比结果为：" + flag);
                        if (!flag){
                            return flag;
                        }
                    } else if(o2 == null){
                        flag = jsonObject1.get(entry.getKey()) == null && entry.getValue() == null;
                        assertResult.add(flag);
                        log.info(" 实际字段内容为：" + jsonObject1.get(entry.getKey()) + "  期望字段内容为：" + entry.getValue() + "  对比结果为：" + flag);
                        if (!flag){
                            return flag;
                        }
                    } else if(o2 instanceof JSONArray){
                        log.warn(" 获取到的Json类型为：JsonArray ~ ~ ~");
                        return compareJsonResult(jsonObject1.get(entry.getKey()).toString(),o2.toString());
                    }else if(o2 instanceof JSONObject){
                        return compareJsonResult(jsonObject1.get(entry.getKey()).toString(), o2.toString());
                    }else {
                        log.error(" 请完善Json对比类型 ~ ~ ~");
                    }
                    len++;
                    if (len == jsonObject2.size()){
                        flag = true;
                        return flag;
                    }
                }catch (Exception e){
                    //两个json格式不一致的时候，返回false
                    log.warn("两个json格式不一致，请仔细检查一下");
                    flag = false;
                    return flag;
                }
            }
        }else if (actObject instanceof JSONArray && expectObject instanceof JSONArray){
            JSONArray jsonArrayObj1 = (JSONArray)actObject;
            JSONArray jsonArrayObj2 = (JSONArray)expectObject;
            for (Object obj2 : jsonArrayObj2){
                int arrayLen = 0;
                for (Object obj1 : jsonArrayObj1){
                    String str1 = JSONObject.toJSONString(obj1);
                    String str2 = JSONObject.toJSONString(obj2);
                    flag = compareJsonResult(str1, str2);
                    if (flag == true){
                        break;
                    }
                    arrayLen++;
                    if (arrayLen == jsonArrayObj1.size()){
                        return false;
                    }
                }
            }
        }else if (expectInfo instanceof String){
            flag = expectInfo.equals(actInfo);
            return flag;
        }else {
            log.info("请完善json对比类型");
        }
        return flag;
    }
}
