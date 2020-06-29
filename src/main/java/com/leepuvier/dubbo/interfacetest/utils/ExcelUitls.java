package com.leepuvier.dubbo.interfacetest.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:20 PM
 * @ContentUse :
 */

@Slf4j
public class ExcelUitls {

    //lable用例等级区分
    public static ArrayList<Object[]> getTestData(String fileName, String sheetName, List lable)
            throws IOException {
        XSSFWorkbook workbook= getWorkbook(fileName);
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        ArrayList<Object[]> data=new ArrayList<Object[]>();
        XSSFSheet currentSheet = workbook.getSheet(sheetName);
        XSSFRow firstRow = currentSheet.getRow(0);
        int paranums = firstRow.getLastCellNum();
        Object[] cols=getRow(paranums,firstRow);
        if(null ==firstRow){
            return list;
        }
        for (int rowNum = 1; rowNum <= currentSheet.getLastRowNum(); rowNum++) {
            try {
                XSSFRow xssfRow = currentSheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }else {
                    if (lable.contains(getValue(xssfRow.getCell(0)).toString())){
                        Map<String,Object> paraMap=new HashMap<String, Object>();
                        Object[] row = getRow(paranums, xssfRow);
                        for (int i=0;i<cols.length;i++){
                            paraMap.put((String) cols[i],row[i]);
                        }
                        data.add(new Object[]{paraMap});
                    }else {
                        continue;
                    }
                }
            } catch (IllegalArgumentException e) {
                log.info("read case error !");
                e.printStackTrace();
            }
        }
        return data;
    }
    private static Object[] getRow(int paranums,XSSFRow xssfRow) {
        Object testCases[] = new Object[paranums-3];
        if (isRowEmpty(xssfRow,paranums)) {
            for (int cellNum = 4; cellNum < paranums; cellNum++) {
                XSSFCell xssfCell = xssfRow.getCell(cellNum);
                testCases[cellNum-4]=getValue(xssfCell);
            }
        }
        return testCases;
    }
    private static Object getValue(XSSFCell cell) {
        if (null == cell || cell.getCellType().getCode() == 3) {
            return "";
        } else if (cell.getCellType().getCode() == 4) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType().getCode() == 0) {
            //数组转换为str
            cell.setCellType(CellType.STRING);
//        return String.valueOf(cell.getNumericCellValue());
            return String.valueOf(cell.getStringCellValue());
        }
        else {
            return String.valueOf(cell.getStringCellValue());
        }
    }
    public static boolean isRowEmpty(XSSFRow xssfrow,int paranums) {
        int flag=0;
        for (int c = 0; c < paranums; c++) {
            XSSFCell cell = xssfrow.getCell(c);
            if(cell == null || cell.getCellType().getCode() == 3 ){
                flag++;
            }
        }
        return flag<paranums;
    }
    public static XSSFWorkbook getWorkbook(String fileName) throws IOException {
        FileInputStream excelFileInputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
        excelFileInputStream.close();
        return workbook;
    }
}
