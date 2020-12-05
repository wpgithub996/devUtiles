package com.agree.afamsg;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 * @author 王鹏
 * @date 2020/11/16 15:07
 */
public class CreateInterface {
    public static void main(String[] args) {
        ShowPanel panel = new ShowPanel();
        panel.showPanel();

    }

    /*
     * @desc   读取工作簿
     * @author wangpeng
     * @date 2020/11/17 14:15
     * @param [filePath, exportFilePath]
     * @return void
     */
    public void init(String sysCode,String filePath,String templateCode, String exportFilePath) {
        final File file = new File(filePath);
        try {
            //读取工作簿
            XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(file));
            //获得工作表迭代器
            Iterator<Sheet> iterator = sheets.iterator();
            CreateInterface createInterface = new CreateInterface();
            createInterface.iteratorSheets(sysCode,templateCode,iterator,exportFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * @desc 循环工作表
     * @author wangpeng
     * @date 2020/11/17 10:51
     * @param [sheetIterator]
     * @return void
     */
    public void iteratorSheets(String sysCode,String templateCode,Iterator<Sheet> iterator, String exportFilePath){
            while (iterator.hasNext()) {
                Sheet nextSheet = iterator.next();
                try {
                    CreateReqSql createReqSql = new CreateReqSql();
                    createReqSql.creatInSql(sysCode, nextSheet, templateCode, exportFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        //写入公共头
        ReadData readData = new ReadData();
        readData.inPutData("",exportFilePath,templateCode);
    }
}
