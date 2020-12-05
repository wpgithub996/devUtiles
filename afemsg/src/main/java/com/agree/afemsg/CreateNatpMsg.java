package com.agree.afemsg;

import com.agree.afemsg.pojo.JsonEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王鹏
 * @date 2020/11/18 15:48
 */
public class CreateNatpMsg {
    private HashMap<String, Object> listJsonmap = new HashMap<>();  //存放集合数据
    private ArrayList<JsonEntity> filedsList = new ArrayList<>();
    private WriteData writeData = new WriteData();
    private ShowMessageDialog messageDialog = new ShowMessageDialog();

    /*
     * @desc   创建Natp报文格式
     * @author wangpeng
     * @date 2020/11/17 10:49
     * @param [sheet]
     * @return void
     */
    public void creatNatpMsg(Sheet sheet, String exportFilePath) {
        //初始化报文内容
        StringBuilder msgStr = new StringBuilder();
        msgStr.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<MessageMap>\n" +
                "  <Message ClassName=\"NatpAnalyzer\" Type=\"NATP报文\">\n");
        //获得工作表名称(报文文件名称)
        String sheetName = sheet.getSheetName();
//        String character = sheet.getRow(2).getCell(1).toString();  //字符编码
        String prefix=""; //根路径  表达式前缀  data.name
        String nodeName="";
        for (int i = 5; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            String zhName = row.getCell(0).toString();  //中文名称
            String engName;  //字段名称
            String natpName;   //natp变量名
            String strType;  //字符类型
            String defaultValue; //默认值
            String conditionExp; //表达式

            boolean incPrefix;
            if (null != zhName && !zhName.equals("")) {
                try {
                    engName = row.getCell(1).toString();
                    natpName = null!= row.getCell(4).toString() && !row.getCell(4).toString().equals("") ? row.getCell(4).toString() : engName.toLowerCase();
                    strType = row.getCell(3).toString();
                    incPrefix = row.getCell(7).toString().equals("是") ? true : false;
                    defaultValue = null != row.getCell(5).toString() && !row.getCell(5).toString().equals("") ? row.getCell(5).toString() : "&quot;&quot;";
                    //包含 "  是字符常量
                    defaultValue = !defaultValue.contains("\"") ? defaultValue : "&quot;"+defaultValue+"&quot;";
                    //表达式为空默认英文名称小写
                    conditionExp = null != row.getCell(6).toString() && !row.getCell(6).toString().equals("") ? row.getCell(6).toString() : engName.toLowerCase();
                    conditionExp = !conditionExp.contains("\"") ? conditionExp : "&quot;"+conditionExp+"&quot;";

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    messageDialog.showWarningDialog(sheetName + "  第" + i + 1 + "行缺失数据请确认");
                    return;
                }
                switch (strType){
                    case "AM-A&M节点":
                        nodeName = engName;
                        listJsonmap.put("listNode", engName); //集合路径
                        continue;
                    case "X-集合循环次数":
                        listJsonmap.put("listsize", engName); //集合循环次数
                        continue; //跳出本次循环
                    case "AM-根路径 ":
                        prefix = engName+".";
                        listJsonmap.put("listPath", prefix); //集合路径
                        continue;
                    case "AD-循环字段":
                        //存放集合字段信息
                        JsonEntity jsonEntity = new JsonEntity();
                        jsonEntity.setZhName(zhName);
                        jsonEntity.setEngName(engName.replace(".",""));
                        jsonEntity.setDefaultValue(defaultValue);
                        jsonEntity.setNatpName(natpName.toLowerCase());
                        jsonEntity.setFliedTyep(strType);
                        jsonEntity.setConditionExp(incPrefix ? prefix+nodeName+"[i]."+ engName : nodeName+"[i]."+engName);
                        jsonEntity.setIncPrefix(incPrefix);
                        filedsList.add(jsonEntity);
                        continue;
                }
                if(incPrefix){
                    conditionExp = prefix+"."+engName.toLowerCase();
                }
                //拼接单字段
                msgStr.append("    <Field FieldDefaultValue=\"" + defaultValue + "\" FieldDescription=\"" + zhName + "\" FieldName=\"#" + engName + "\" FieldType=\"" + strType + "\">\n" +
                              "      <Parameter Name=\"NATP变量名\" Value=\""+ engName.toLowerCase() +"\"/>\n" +
                              "      <PackExpr Expr=\""+conditionExp+"\"/>\n" +
                              "    </Field>\n");
            } else {
                //如果中文名称为空说明跳出循环
                break;
            }
        }
        //拼集合字段
        StringBuilder listJson = createListJson(listJsonmap,filedsList);
        msgStr.append(listJson);
        //拼报文尾
        msgStr.append("    <Parameter Name=\"是否自定义变量名\" Value=\"否\"/>\n" +
                      "    <Parameter Name=\"载入起始位置\" Value=\"0\"/>\n" +
                      "    <Parameter Name=\"是否拆成列表形式\" Value=\"否\"/>\n" +
                      "    <Parameter Name=\"载入长度\" Value=\"0\"/>\n" +
                      "    <Parameter Name=\"自动拆包变量类型\" Value=\"String\"/>\n" +
                      "    <Parameter Name=\"afa版本\" Value=\"3.0\"/>");
        writeData.outPutData(msgStr.toString(), exportFilePath + "/" + sheetName + ".rcd");
    }

    public StringBuilder createListJson(Map map, List<JsonEntity> jsonEntityList) {
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
            stringBuilder.append("    <Switch ConditionExp=\"" + loopCount + "\">\n" +
                                 "      <Case Value=\"==1\">\n");
            //拼接 ==1
            for (JsonEntity jsonEntity : jsonEntityList) {
                stringBuilder.append("        <Field FieldDefaultValue=\""+ jsonEntity.getDefaultValue() +"\" FieldDescription=\""+ jsonEntity.getZhName() +"\" FieldName=\"#"+ jsonEntity.getEngName() +"\" FieldType=\""+jsonEntity.getFliedTyep() +"\">\n" +
                                     "          <Parameter Name=\"NATP变量名\" Value=\""+ jsonEntity.getNatpName() +"\"/>\n" +
                                     "          <PackExpr Expr=\""+ jsonEntity.getConditionExp().replace("[i]","[0]") +"\"/>\n" +
                                     "        </Field>");
            }
            stringBuilder.append("      </Case>\n" +
                                 "      <Case Value=\">1\">\n" +
                                 "       <Loop Count=\""+ loopCount +"\" LoopVarName=\"i\" Name=\""+ listPath +"\">");
            //拼接 >1
            for (JsonEntity jsonEntity : jsonEntityList) {
                stringBuilder.append("          <Field FieldDefaultValue=\""+ jsonEntity.getDefaultValue() +"\" FieldDescription=\""+ jsonEntity.getZhName() +"\" FieldName=\"#"+ jsonEntity.getEngName() +"\" FieldType=\""+ jsonEntity.getFliedTyep() +"\">\n" +
                                     "          <Parameter Name=\"NATP变量名\" Value=\""+ jsonEntity.getNatpName() +"\"/>\n" +
                                     "          <PackExpr Expr=\""+ jsonEntity.getConditionExp() +"\"/>\n" +
                                     "          </Field>");
            }
            stringBuilder.append("        </Loop>\n" +
                                 "      </Case>\n" +
                                 "    </Switch>\n");
        } else {
            return stringBuilder;
        }
        return stringBuilder;
    }
}
