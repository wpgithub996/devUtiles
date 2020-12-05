package com.agree.afamsg;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author 王鹏
 * @date 2020/11/18 15:48
 */
public class CreateRspSql {
    private WriteData writeData = new WriteData();
    public void creatOutSql(Sheet sheet, String templateCode, Integer outIndex, String exportFilePath){
        CreateInterface createInterface = new CreateInterface();
        //系统交易码
        String transCode;
        String sheetName = sheet.getSheetName();
        //系统交易码
        if(sheetName.contains("IBP")){
            transCode = sheet.getRow(1).getCell(1).toString();
        }else {
            transCode = sheetName.replace("0.01","1.01");
        }
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
        //循环表字段
        int sqlRspIndex =1;//初始化响应报文字段序号
        String nodeName ="";  //初始化节点名称
        //输出字段
        for (int i = outIndex+2; i < physicalNumberOfRows; i++) {
            //获取当前行
            Row row = sheet.getRow(i);
            //获取当前行第一列名称(中文字段)
            String zhName = null;
            try {
                zhName = row.getCell(0).toString();
            } catch (Exception e) {
                e.printStackTrace();
                writeData.outPutData("----此处可能存在异常,请确认----",exportFilePath);
            }
            if(zhName.equals("") || null == zhName){
                break;
            }
            //取值(接口输入)
            String engName = row.getCell(1).toString();//英文名称
            String mustIn = row.getCell(2).toString();//是否必输
            String type = row.getCell(3).toString();//类型
            String fieldLen = row.getCell(4).toString().replace(".0","").split(",")[0];//字段长度
            String colMapName = engName; // 对应平台字段代码
            String colFlage = "F";  //字段处理标识 F-单字段类型 A-数组类型 R-引用类型
            if(type.equals("AM-A&M节点")){
                if(nodeName.equals("")){
                    nodeName = engName.replace(".","");  //记录节点名称
                }else if(!nodeName.contains("/") && engName.contains(".")){ //不为空且不包含斜杠且节点名称包含 . (节点字段)增加二级节点
                    nodeName = nodeName+"/"+(engName.replace(".",""));  //记录节点名称
                }else if(nodeName.contains("/") || !engName.contains(".")){ //包含斜杠说明已经有二级节点了
                    nodeName = engName.replace(".","");  //记录节点名称
                }
                continue; //跳出本次循环
            }
            //拼接SQL
            if(engName.contains(".")){//单字段
                colMapName = engName.replace(".","");
                engName = nodeName+"/"+engName.replace(".","");
                colFlage = "A";
            }
            if(engName.equals("PltfrmPcsgCD")){
                colMapName = "p_dealcode";
            }
            if(engName.equals("PltfrmPcsgInf")){
                colMapName = "p_dealmsg";
            }
            String sqlStr = "insert into T_ARSM_INTERFACECOLMAP (PRODUCTCODE, MSGTYPE, COLNUM, COLNAME, COLFLAGE, COLDEFAULT, COLTYPE, COLLENGTH, COLSCALE, COLDESC, COLMUST, COLMAPNAME, PACKTYPE) " +
                    "values ('"+templateCode+"', '"+transCode+".rsp', \'"+sqlRspIndex+"\', \'/body/response/"+engName+"\', \'"+colFlage+"\', null, 'Max"+fieldLen+"Char', \'9999\', \'0\', \'"+zhName+"\', \'"+mustIn+"\', \'"+colMapName.toLowerCase()+"\', \'esbxml\');";
            sqlRspIndex ++;
//                System.out.println(sqlStr);
            writeData.outPutData(sqlStr,exportFilePath);
        }
        //弹出提示框

    }
}
