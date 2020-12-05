package com.agree.afamsg;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author 王鹏
 * @date 2020/11/18 15:48
 */
public class CreateReqSql {
    private WriteData writeData = new WriteData();

    /*
     * @desc 创建单工作表接口映射SQL-请报文(T_ARSM_INTERFACECOLMAP)
     * @author wangpeng
     * @date 2020/11/17 10:49
     * @param [sheet]
     * @return void
     */
    public void creatInSql(String sysCode, Sheet sheet, String templateCode, String exportFilePath) {
        //获得工作表名称
        String sheetName = sheet.getSheetName();
        //服务名称
        String serviceName = sheet.getRow(2).getCell(1).toString();
        //排除
        String transCode;
        if (sheetName.contains(sysCode) && !sheetName.equals("IBP.000000000.01")) {
            //系统交易码
            if (sheetName.contains("IBP")) {
                transCode = sheet.getRow(1).getCell(1).toString();
                String initSql = "insert into T_ARSM_INTERFACECOLMAP (PRODUCTCODE, MSGTYPE, COLNUM, COLNAME, COLFLAGE, COLDEFAULT, COLTYPE, COLLENGTH, COLSCALE, COLDESC, COLMUST, COLMAPNAME, PACKTYPE) " +
                        "values (\'" + templateCode + "\', \'" + transCode + "\', '1', \'" + transCode + "\', 'F', \'" + sheetName.replace("0.01", "1.01") + "\', 'Max20char', '9999', '0', \'" + serviceName + "\', 'O', \'" + transCode + "\', 'esbxml');";
                writeData.outPutData(initSql, exportFilePath);
            } else {
                transCode = sheetName;
            }

            System.out.println("---------------" + sheetName + "---------------");
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            //循环表字段
            int outIndex = 0; //初始化接口输出字段行数
            int sqlReqIndex = 1; //初始化请求报文字段序号
            String nodeName = "";  //初始化节点名称
            //输入字段
            for (int i = 5; i < physicalNumberOfRows; i++) {
                //获取当前行
                Row row = sheet.getRow(i);
                //获取当前行第一列名称(中文字段)
                String zhName = null;
                try {
                    zhName = row.getCell(0).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    writeData.outPutData("----此处可能存在异常,请确认----", exportFilePath);
                }
                if (zhName.equals("接口字段(输出)")) {
                    outIndex = i;
                    //跳出循环
                    break;
                }
                //取值(接口输入)
                String engName = row.getCell(1).toString();//英文名称
                String mustIn = row.getCell(2).toString();//是否必输
                String type = row.getCell(3).toString();//类型
                String fieldLen = row.getCell(4).toString().replace(".0", "").split(",")[0];//字段长度
                String colMapName = engName; // 对应平台字段代码
                String colFlage = "F";  //字段处理标识 F-单字段类型 A-数组类型 R-引用类型
                if (type.equals("AM-A&M节点")) {
                    if (nodeName.equals("")) {
                        nodeName = engName.replace(".", "");  //记录节点名称
                    } else if (!nodeName.contains("/") && engName.contains(".")) { //不为空且不包含斜杠且节点名称包含 . (节点字段)增加二级节点
                        nodeName = nodeName + "/" + (engName.replace(".", ""));  //记录节点名称
                    } else if (nodeName.contains("/") || !engName.contains(".")) { //包含斜杠说明已经有二级节点了
                        nodeName = engName.replace(".", "");  //记录节点名称
                    }
                    continue; //跳出本次循环
                }
                if (sheetName.equals("VCBS.60200240.01")) {
                    System.out.println(nodeName);
                }
                //拼接SQL
                if (engName.contains(".")) {//集合单字段
                    colMapName = engName.replace(".", "");
                    engName = nodeName + "/" + engName.replace(".", "");
                    colFlage = "A";
                }
                String sqlStr = "insert into T_ARSM_INTERFACECOLMAP (PRODUCTCODE, MSGTYPE, COLNUM, COLNAME, COLFLAGE, COLDEFAULT, COLTYPE, COLLENGTH, COLSCALE, COLDESC, COLMUST, COLMAPNAME, PACKTYPE) " +
                        "values ('" + templateCode + "', '" + transCode + ".req', \'" + sqlReqIndex + "\', \'/body/request/" + engName + "\', \'" + colFlage + "\', null, 'Max" + fieldLen + "Char', \'9999\', \'0\', \'" + zhName + "\', \'" + mustIn + "\', \'" + colMapName.toLowerCase() + "\', \'esbxml\');";
                sqlReqIndex++;
//                System.out.println(sqlStr);

                writeData.outPutData(sqlStr, exportFilePath);
            }
            //调用生成响应报文sql
            CreateRspSql createRspSql = new CreateRspSql();
            createRspSql.creatOutSql(sheet, templateCode, outIndex, exportFilePath);
        }
    }
}
