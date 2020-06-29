package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:44 PM
 * @ContentUse :
 */

@Slf4j
public class MongoUtils {

    private static String  host="127.0.0.1";
    private static int port = 20000;
    private static String user="test";
    private static String pwd="test";
    private static String datebase="test";

    //需要密码认证方式连接
    public static MongoDatabase getConnect() {
        List<ServerAddress> adds = new ArrayList<>();
        //ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress serverAddress = new ServerAddress(host, port);
        adds.add(serverAddress);

        List<MongoCredential> credentials = new ArrayList<>();
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, datebase, pwd.toCharArray());
        credentials.add(mongoCredential);

        //通过连接认证获取MongoDB连接
        MongoClient mongoClient = new MongoClient(adds, credentials);

        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(datebase);

        return mongoDatabase;

    }
    //查询mongo数据库
    public static JSONObject FilterfindTest(String collections, String key, long value) {
        //获取数据库连接对象
        MongoDatabase mongoDatabase = getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection(collections);
        //指定查询过滤器
        Bson filter = Filters.eq(key, value);
        //指定查询过滤器查询
        FindIterable findIterable = collection.find(filter);
        MongoCursor<Document> cursor = findIterable.iterator();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        try{
            while (cursor.hasNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.putAll(cursor.next());
                list.add(map);
                JSONObject mongo_json = new JSONObject(list.get(0));
                return mongo_json;
            }
            return null;
        }finally {
            cursor.close();
        }

    }
    //删除mongo库
    public static void DeleteTest(String collections, String key, long value) {
        //获取数据库连接对象
        MongoDatabase mongoDatabase = getConnect();
        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection(collections);
        //申明删除条件
        Bson filter = Filters.eq(key,value);
        //删除与筛选器匹配的所有文档
        collection.deleteMany(filter);

    }
}
