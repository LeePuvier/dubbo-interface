package com.leepuvier.dubbo.client.common;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  8:47 PM
 * @ContentUse :
 */
public interface Constants {
    String REGISTRY_NONE = "none";
    String REGISTRY_ZOOKEEPER = "zookeeper";
    String REGISTRY_MULTICAST = "multicast";
    String REGISTRY_REDIS = "redis";
    String REGISTRY_SIMPLE = "simple";
    String RPC_PROTOCOL_DUBBO = "dubbo";
    String RPC_PROTOCOL_RMI = "rmi";
    String RPC_PROTOCOL_HESSIAN = "hessian";
    String RPC_PROTOCOL_HTTP = "http";
    String RPC_PROTOCOL_WEBSERVICE = "webservice";
    String RPC_PROTOCOL_THRIFT = "thrift";
    String RPC_PROTOCOL_MEMCACHED = "memcached";
    String RPC_PROTOCOL_REDIS = "redis";
    String ASYNC = "async";
    String SYMBOL = "://";
    int INT_DEFAULT = 0;
    double DOUBLE_DEFAULT = 0.0D;
    boolean BOOLEAN_DEFAULT = false;
    char CHAR_DEFAULT = '\u0000';
    float FLOAT_DEFAULT = 0.0F;
    byte BYTE_DEFAULT = 0;
    long LONG_DEFAULT = 0L;
    short SHORT_DEFAULT = 0;
    int[] INT_ARRAY_DEFAULT = null;
    double[] DOUBLE_ARRAY_DEFAULT = null;
    boolean[] BOOLEAN_ARRAY_DEFAULT = null;
    char[] CHAT_ARRAY_DEFAULT = null;
    float[] FLOAT_ARRAY_DEFAULT = null;
    byte[] BYTE_ARRAY_DEFAULT = null;
    long[] LONG_ARRAY_DEFAULT = null;
    short[] SHORT_ARRAY_DEFAULT = null;
    String FIELD_DUBBO_REGISTRY_PROTOCOL = "FIELD_DUBBO_REGISTRY_PROTOCOL";
    String FIELD_DUBBO_REGISTRY_GROUP = "FIELD_DUBBO_REGISTRY_GROUP";
    String FIELD_DUBBO_RPC_PROTOCOL = "FIELD_DUBBO_RPC_PROTOCOL";
    String FIELD_DUBBO_ADDRESS = "FIELD_DUBBO_ADDRESS";
    String FIELD_DUBBO_TIMEOUT = "FIELD_DUBBO_TIMEOUT";
    String FIELD_DUBBO_VERSION = "FIELD_DUBBO_VERSION";
    String FIELD_DUBBO_RETRIES = "FIELD_DUBBO_RETRIES";
    String FIELD_DUBBO_CLUSTER = "FIELD_DUBBO_CLUSTER";
    String FIELD_DUBBO_GROUP = "FIELD_DUBBO_GROUP";
    String FIELD_DUBBO_CONNECTIONS = "FIELD_DUBBO_CONNECTIONS";
    String FIELD_DUBBO_LOADBALANCE = "FIELD_DUBBO_LOADBALANCE";
    String FIELD_DUBBO_ASYNC = "FIELD_DUBBO_ASYNC";
    String FIELD_DUBBO_INTERFACE = "FIELD_DUBBO_INTERFACE";
    String FIELD_DUBBO_METHOD = "FIELD_DUBBO_METHOD";
    String FIELD_DUBBO_METHOD_ARGS = "FIELD_DUBBO_METHOD_ARGS";
    String FIELD_DUBBO_METHOD_ARGS_SIZE = "FIELD_DUBBO_METHOD_ARGS_SIZE";
    String FIELD_DUBBO_ATTACHMENT_ARGS = "FIELD_DUBBO_ATTACHMENT_ARGS";
    String FIELD_DUBBO_ATTACHMENT_ARGS_SIZE = "FIELD_DUBBO_ATTACHMENT_ARGS_SIZE";
    Integer DEFAULT_TIMEOUT = 6000;
    String DEFAULT_VERSION = "1.0.0";
    Integer DEFAULT_RETRIES = 0;
    String DEFAULT_CLUSTER = "failfast";
    String DEFAULT_CONNECTIONS = "100";
    String DEFAULT_APPLICATION = "Dubbo-Client";
    String DEFAULT_OWER = "LeePuvier";
    String DEFAULT_ORGANIZATION = "LeePuvier";
}
