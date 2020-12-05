package com.agree.afamsg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 王鹏
 * @date 2020/11/18 15:49
 */
public class WriteData {
    /*
     * @desc   输出SQL到指定路径文件
     * @author wangpeng
     * @date 2020/11/18 15:54
     * @param [sqlStr, wirteFilePath]
     * @return void
     */
    public void outPutData(String sqlStr,String wirteFilePath){
        File file = new File(wirteFilePath);
        FileOutputStream os = null;
        try {
            if (!file.exists()){
                String parent = file.getParent();
                File parentFile = new File(parent);
                if (!parentFile.exists() || !parentFile.isDirectory()) {
                    parentFile.mkdirs();
                }
                boolean newFile = file.createNewFile();
            }
            os=new FileOutputStream(file,true);
            os.write(sqlStr.getBytes());
            os.write("\r\n".getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageDialog showMessageDialog = new ShowMessageDialog();
            showMessageDialog.showWarningDialog(e.getMessage());
        }

    }
}
