package com.agree.afemsg;

import com.agree.afemsg.pojo.JsonEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/**
 * @author 王鹏
 * @date 2020/11/18 15:48
 */
public class CreateJsonMsg {
    private HashMap<String, Object> listJsonmap = new HashMap<>();  //存放集合数据
    private ArrayList<JsonEntity> filedsList = new ArrayList<>();
    private WriteData writeData = new WriteData();
    private ShowMessageDialog messageDialog = new ShowMessageDialog();

    /*
     * @desc   创建JSON报文格式
     * @author wangpeng
     * @date 2020/11/17 10:49
     * @param [sheet]
     * @return void
     */
    public void creatJsonMsg(Sheet sheet, String exportFilePath) {
        //初始化报文内容
        StringBuilder msgStr = new StringBuilder();
        msgStr.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                      "<MessageMap>\n" +
                      "  <Message ClassName=\"JsonAnalyzer\" Type=\"JSON报文\">\n");
        //获得工作表名称(报文文件名称)
        String sheetName = sheet.getSheetName();
        String character = sheet.getRow(2).getCell(1).toString();  //字符编码
        //系统交易码
        for (int i = 5; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            String zhName = row.getCell(0).toString();  //中文名称
            String engName;  //字段名称
            String isMust;   //是否必输
            String strType;  //字符类型
            String defaultValue; //默认值
            String conditionExp; //表达式

            if (null != zhName && !zhName.equals("")) {
                try {
                    engName = row.getCell(1).toString();
                    isMust = row.getCell(2).toString().equals("M") ? "必送" : "非必送";
                    strType = row.getCell(3).toString();
                    defaultValue = null != row.getCell(5).toString() && !row.getCell(5).toString().equals("") ? row.getCell(5).toString() : "&quot;&quot;";
                    conditionExp = null != row.getCell(6).toString() && !row.getCell(6).toString().equals("") ? row.getCell(6).toString() : engName.replace(".","");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    messageDialog.showWarningDialog(sheetName + "  第" + i + 1 + "行缺失数据请确认");
                    return;
                }

                switch (strType){
                    case "AM-A&M节点":
                        listJsonmap.put("listPath", engName); //集合路径
                        continue;
                    case "X-集合循环次数":
                        listJsonmap.put("listsize", engName); //集合循环次数
                        continue; //跳出本次循环
                    case "AD-循环字段":
                        //存放集合字段信息
                        JsonEntity jsonEntity = new JsonEntity();
                        jsonEntity.setZhName(zhName);
                        jsonEntity.setEngName(engName.replace(".",""));
                        jsonEntity.setDefaultValue(defaultValue);
                        jsonEntity.setIsMust(isMust);
                        jsonEntity.setFliedTyep(strType);
                        jsonEntity.setConditionExp(conditionExp);
                        filedsList.add(jsonEntity);
                        continue;
                }

                //拼接单字段
                msgStr.append("    <Field FieldDefaultValue=\"" + defaultValue + "\" FieldDescription=\"" + zhName + "\" FieldName=\"#" + engName + "\" FieldType=\"" + strType + "\">\n" +
                        "            <Parameter Name=\"路径\" Value=\"/" + engName + "\"/>\n" +
                        "            <Parameter Name=\"字段类型\" Value=\"字符\"/>\n" +
                        "            <Parameter Name=\"数据拼包类型\" Value=\"" + isMust + "\"/>\n" +
                        "            <PackExpr Expr=\"" + engName + "\"/>\n" +
                        "          </Field>\n");
            } else {
                //如果中文名称为空说明跳出循环
                break;
            }
        }
        //拼集合字段
        StringBuilder listJson = createListJson(listJsonmap,filedsList);
        msgStr.append(listJson);
        //拼报文尾
        msgStr.append("  <Parameter Name=\"是否自定义变量名\" Value=\"否\"/>\n" +
                      "  <Parameter Name=\"字符编码\" Value=\"" + character + "\"/>\n" +
                      "</Message>\n");
        writeData.outPutData(msgStr.toString(), exportFilePath + "/" + sheetName + ".rcd");
    }

    public StringBuilder createListJson(Map map,List<JsonEntity> jsonEntityList) {
        StringBuilder stringBuilder = new StringBuilder();
        String loopCount;  //循环次数
        String listPath = null;
        try {
            listPath = String.valueOf(map.get("listPath"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return  stringBuilder;
        }
        //是否手动指定循环次数
        try {
            loopCount = String.valueOf(map.get("listsize"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            loopCount = listPath + ".size()";
        }
        //循环根节点

        if (jsonEntityList.size()>0) {
            //初始化集合字段
            stringBuilder.append("<Switch ConditionExp=\"" + loopCount + "\">\n" +
                                 "  <Case Value=\"==1\">\n" +
                                 "    <Loop Count=\"1\" LoopVarName=\"i\" Name=\"" + listPath + "\">\n");
            //拼接 ==1
            for (JsonEntity jsonEntity : jsonEntityList) {
                stringBuilder.append("      <Field FieldDefaultValue=\"" + jsonEntity.getDefaultValue() + "\" FieldDescription=\"" + jsonEntity.getZhName() + "\" FieldName=\"# " + jsonEntity.getEngName() + "\" FieldType=\"" + jsonEntity.getFliedTyep() + "\">\n" +
                                     "        <Parameter Name=\"路径\" Value=\"/" + listPath + "/" + jsonEntity.getEngName() + "\"/>\n" +
                                     "          <Parameter Name=\"字段类型\" Value=\"字符\"/>\n" +
                                     "          <Parameter Name=\"数据拼包类型\" Value=\"" + jsonEntity.getIsMust() + "\"/>\n" +
                                     "          <PackExpr Expr=\"" + jsonEntity.getConditionExp() + "\"/>\n" +
                                     "       </Field>\n");
            }
            stringBuilder.append("    </Loop>\n" +
                    "  </Case>\n" +
                    "  <Case Value=\">1\">\n" +
                    "    <Loop Count=\"" + loopCount + "\" LoopVarName=\"i\" Name=\"" + listPath + "\">");
            //拼接 >1
            for (JsonEntity jsonEntity : jsonEntityList) {
                stringBuilder.append("      <Field FieldDefaultValue=\"" + jsonEntity.getDefaultValue() + "\" FieldDescription=\"" + jsonEntity.getZhName() + "\" FieldName=\"# " + jsonEntity.getEngName() + "\" FieldType=\"" + jsonEntity.getFliedTyep() + "\">\n" +
                        "        <Parameter Name=\"路径\" Value=\"/" + listPath + "/" + jsonEntity.getEngName() + "\"/>\n" +
                        "        <Parameter Name=\"字段类型\" Value=\"字符\"/>\n" +
                        "        <Parameter Name=\"数据拼包类型\" Value=\"" + jsonEntity.getIsMust() + "\"/>\n" +
                        "        <PackExpr Expr=\"" + jsonEntity.getConditionExp() + "[i+1]\"/>\n");
            }
            stringBuilder.append("    </Loop>\n" +
                    "  </Case>\n" +
                    "</Switch>\n");
        } else {
            return stringBuilder;
        }
        return stringBuilder;
    }
}
