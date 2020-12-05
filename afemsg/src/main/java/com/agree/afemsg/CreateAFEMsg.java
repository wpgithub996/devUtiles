package com.agree.afemsg;

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
public class CreateAFEMsg {
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
    public void init(String filePath, String exportFilePath) {
        final File file = new File(filePath);
        try {
            //读取工作簿
            XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(file));
            //获得工作表迭代器
            Iterator<Sheet> iterator = sheets.iterator();
            CreateAFEMsg createAFEMsg = new CreateAFEMsg();
            createAFEMsg.iteratorSheets(iterator,exportFilePath);
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
    public void iteratorSheets(Iterator<Sheet> iterator, String exportFilePath){
        ShowMessageDialog messageDialog = new ShowMessageDialog();
        System.out.println("=============开始===================");
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
                try {
                   String msgType = String.valueOf(sheet.getRow(1).getCell(1));
                   if(msgType.contains("JSON")){
                       CreateJsonMsg createJsonMsg = new CreateJsonMsg();
                       createJsonMsg.creatJsonMsg(sheet,exportFilePath);
                   }else if(msgType.contains("NATP")){
                       CreateNatpMsg createNatpMsg = new CreateNatpMsg();
                       createNatpMsg.creatNatpMsg(sheet,exportFilePath);
                   }else {
                       messageDialog.showWarningDialog(sheet.getSheetName()+" 报文格式有误,请检查");
                       return;
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        ShowMessageDialog showMessageDialog = new ShowMessageDialog();
        showMessageDialog.showMessageDialog("处理完成");
        }
}
