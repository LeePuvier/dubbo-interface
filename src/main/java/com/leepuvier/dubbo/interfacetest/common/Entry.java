package com.leepuvier.dubbo.interfacetest.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:07 PM
 * @ContentUse :
 */

@SpringBootApplication
@EnableCaching
public class Entry {
    public static void main(String[] args){
        SpringApplication.run(Entry.class, args);
    }
}
