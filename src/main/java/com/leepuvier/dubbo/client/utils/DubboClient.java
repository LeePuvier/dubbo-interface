package com.leepuvier.dubbo.client.utils;

import com.google.common.base.Preconditions;
import com.leepuvier.dubbo.client.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  8:48 PM
 * @ContentUse :
 */
public class DubboClient {

    private static final Logger log = LoggerFactory.getLogger(DubboClient.class);
    private static final ApplicationConfig applicationConfig = new ApplicationConfig();
    private RegistryConfig registry;
    private ReferenceConfigCache cache;
    private String version;
    private String group;
    private String url;
    private String addr;
    private Integer timeout;
    private Boolean check;
    private Boolean generi;
    private String interfaceName;
    private String protocol;
    private Integer retries;

    public DubboClient(String addr, String url, String interfaceName, String version, String group) {
        this.timeout = Constants.DEFAULT_TIMEOUT;
        this.check = Boolean.FALSE;
        this.generi = Boolean.TRUE;
        this.retries = Constants.DEFAULT_RETRIES;
        this.version = version;
        this.group = group;
        this.url = url;
        this.addr = addr;
        this.interfaceName = interfaceName;
        ReferenceConfig<GenericService> reference = new ReferenceConfig();
        reference.setApplication(applicationConfig);
        RegistryConfig registryConfig = DubboClient.RegistryConfigFactory.get(addr);
        this.registry = registryConfig;
    }

    public DubboClient(String version, String group, String url, String addr, Integer timeout, Boolean check, String interfaceName, String protocol, Integer retries) {
        this.timeout = Constants.DEFAULT_TIMEOUT;
        this.check = Boolean.FALSE;
        this.generi = Boolean.TRUE;
        this.retries = Constants.DEFAULT_RETRIES;
        this.version = version;
        this.group = group;
        this.url = url;
        this.addr = addr;
        this.timeout = timeout;
        this.check = check;
        this.interfaceName = interfaceName;
        this.protocol = protocol;
        this.retries = retries;
        ReferenceConfig<GenericService> reference = new ReferenceConfig();
        reference.setApplication(applicationConfig);
        RegistryConfig registryConfig = DubboClient.RegistryConfigFactory.get(addr);
        this.registry = registryConfig;
    }

    public DubboClient() {
        this.timeout = Constants.DEFAULT_TIMEOUT;
        this.check = Boolean.FALSE;
        this.generi = Boolean.TRUE;
        this.retries = Constants.DEFAULT_RETRIES;
    }

    public Object call(String methodName, String parameterType, LinkedHashMap params) {
        boolean b = StringUtils.isBlank(this.addr) && StringUtils.isBlank(this.url);
        Preconditions.checkArgument(!b);
        Preconditions.checkArgument(StringUtils.isNotBlank(this.interfaceName));
        ReferenceConfig<GenericService> reference = new ReferenceConfig();
        if (this.registry != null) {
            reference.setRegistry(this.registry);
        } else {
            if (StringUtils.isNotBlank(this.version)) {
                this.url = this.url + "&version=" + this.version;
            }

            if (StringUtils.isNotBlank(this.group)) {
                this.url = this.url + "&group=" + this.group;
            }

            reference.setUrl(this.url);
        }

        if (StringUtils.isNotBlank(this.version)) {
            reference.setVersion(this.version);
        }

        if (StringUtils.isNotBlank(this.group)) {
            reference.setGroup(this.group);
        }

        reference.setTimeout(this.timeout);
        reference.setCheck(this.check);
        reference.setId(this.interfaceName);
        reference.setProtocol("dubbo");
        reference.setRetries(this.retries);
        reference.setInterface(this.interfaceName);
        reference.setGeneric(this.generi);

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = (GenericService)cache.get(reference);

        if (genericService == null) {
            cache.destroy(reference);
            throw new IllegalStateException("========service unavailable========");
        } else {

            if (params == null){
                return parameterType == null ? genericService.$invoke(methodName, new String[0], new Object[0]) : genericService.$invoke(methodName, parameterType.split(","), null);
            }else {
                return parameterType == null ? genericService.$invoke(methodName, new String[0], new Object[0]) : genericService.$invoke(methodName, parameterType.split(","), params.values().toArray());
            }
        }
    }

    static {
        applicationConfig.setName("Dubbo-Client");
        applicationConfig.setOwner("LeePuvier");
        applicationConfig.setOrganization("LeePuvier");
    }

    public static class RegistryConfigFactory {
        private static ConcurrentMap<String, RegistryConfig> zkAddressMap = new ConcurrentHashMap();

        public RegistryConfigFactory() {
        }

        public static RegistryConfig get(String zookeeperAddress) {
            if (StringUtils.isBlank(zookeeperAddress)) {
                return null;
            } else {
                RegistryConfig registryConfig = (RegistryConfig)zkAddressMap.get(zookeeperAddress);
                if (registryConfig == null) {
                    RegistryConfig registry = new RegistryConfig();
                    registry.setAddress(zookeeperAddress);
                    registry.setProtocol("zookeeper");
                    registry.setCheck(false);
                    zkAddressMap.putIfAbsent(zookeeperAddress, registry);
                }

                return (RegistryConfig)zkAddressMap.get(zookeeperAddress);
            }
        }
    }
}
