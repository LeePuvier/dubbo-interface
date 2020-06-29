package com.leepuvier.dubbo.interfacetest.common;

import com.leepuvier.dubbo.interfacetest.utils.ExcelUitls;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:17 PM
 * @ContentUse :
 */
public class BaseProvider {

    @DataProvider(name = "test")
    public Iterator<Object[]> isSignedTest() throws IOException {
        List<String> lables = new ArrayList<String>();
        lables.add("0级");
        //lables.add("1级");
        ArrayList<Object[]> testdata = ExcelUitls.getTestData(ExcelConstant.test, "test", lables);
        return testdata.iterator();
    }
}
