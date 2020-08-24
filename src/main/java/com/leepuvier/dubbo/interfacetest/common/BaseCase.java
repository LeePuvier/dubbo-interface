package com.leepuvier.dubbo.interfacetest.common;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:13 PM
 * @ContentUse :
 */

import com.leepuvier.dubbo.interfacetest.Entry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest(classes = Entry.class)
public class BaseCase extends AbstractTestNGSpringContextTests {
}
