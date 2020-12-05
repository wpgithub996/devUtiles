package com.agree.afemsg.pojo;

/**
 * @author 王鹏
 * @date 2020/11/26 8:56
 */
public class JsonEntity {
    private String zhName;
    private String engName;
    private String natpName;
    private String isMust;
    private String fliedTyep;
    private String conditionExp;
    private String defaultValue;
    private String prefix;  //根路径
    private boolean incPrefix; //表达式是否包含根路径
    public JsonEntity() {
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getNatpName() {
        return natpName;
    }

    public void setNatpName(String natpName) {
        this.natpName = natpName;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getFliedTyep() {
        return fliedTyep;
    }

    public void setFliedTyep(String fliedTyep) {
        this.fliedTyep = fliedTyep;
    }

    public String getConditionExp() {
        return conditionExp;
    }

    public void setConditionExp(String conditionExp) {
        this.conditionExp = conditionExp;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isIncPrefix() {
        return incPrefix;
    }

    public void setIncPrefix(boolean incPrefix) {
        this.incPrefix = incPrefix;
    }
}
