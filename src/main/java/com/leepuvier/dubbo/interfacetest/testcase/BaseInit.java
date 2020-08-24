package com.leepuvier.dubbo.interfacetest.testcase;

import com.leepuvier.dubbo.client.utils.DubboClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.annotations.BeforeClass;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/7/13  7:09 PM
 * @ContentUse :
 */

@Slf4j
public class BaseInit {

    @Resource
    JdbcTemplate jdbcTemplate;


    @BeforeClass
    public void setUp(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("123123123");

        jdbcTemplate = new JdbcTemplate(dataSource);

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
