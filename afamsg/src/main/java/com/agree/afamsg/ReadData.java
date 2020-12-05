package com.agree.afamsg;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author 王鹏
 * @date 2020/11/18 15:49
 */
@Component
public class ReadData {
    /*
     * @desc   读取文件
     * @author wangpeng
     * @date 2020/11/18 15:54
     * @param [sqlStr, wirteFilePath]
     * @return void
     */
    public void inPutData(String readFilePath,String wirteFilePath,String templateCode) {
        if(null == readFilePath || readFilePath.equals("")){
            readFilePath = "CommHeader";
        }
        BufferedReader bufferedReader = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(readFilePath);
            File file = classPathResource.getFile();
            bufferedReader = new BufferedReader(new FileReader(file));
            String readLine = bufferedReader.readLine();
            WriteData writeData = new WriteData();
            writeData.outPutData("---公共头---\n",wirteFilePath);
            while (readLine != null) {
                String commHeaderStr = readLine.replace("templateCode", templateCode).trim();
                writeData.outPutData(commHeaderStr,wirteFilePath);
                readLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
