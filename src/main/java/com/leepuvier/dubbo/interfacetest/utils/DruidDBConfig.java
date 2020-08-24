package com.leepuvier.dubbo.interfacetest.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/7/10  6:50 PM
 * @ContentUse :
 */

@Configuration
public class DruidDBConfig {

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    @ConfigurationProperties( "spring.datasource" )
    public com.alibaba.druid.pool.DruidDataSource dataSource() {

        List filterList = new ArrayList<>();
        filterList.add(wallFilter());
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setProxyFilters(filterList);
        return druidDataSource;

    }

    /**
     * 配置druid执行多条sql（批量执行），避免报sql注入异常
     * 链式配置
     * @return
     */
    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }


    @Bean
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        //允许一次执行多条语句
        config.setMultiStatementAllow(true);
        //允许非基本语句的其他语句
        config.setNoneBaseStatementAllow(true);
        return config;
    }


    @Bean
    public ServletRegistrationBean druidServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");

        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>();
        //配置一个拦截器
        servletRegistrationBean.setServlet(new StatViewServlet());
        //指定拦截器只拦截druid管理页面的请求
        servletRegistrationBean.addUrlMappings("/druid/*");
        //ip白名单，如果没有设置或为空，则表示允许所有访问
        bean.addInitParameter("allow","127.0.0.1");
        //是否允许重置druid的统计信息
        bean.addInitParameter("resetEnable","false");
        bean.addInitParameter("loginUsername","admin");
        bean.addInitParameter("loginPassword","admin");
        return bean;
    }

    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions","*.js,*.gif,/druid/*");
        return bean;
    }

}
