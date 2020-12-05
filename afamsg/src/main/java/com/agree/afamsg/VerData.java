package com.agree.afamsg;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 王鹏
 * @date 2020/11/19 13:36
 */
public class VerData {
    /*
     * @desc   数据校验
     * @author wangpeng
     * @date 2020/11/19 13:43
     * @param [map, keys]
     * @return boolean
     */
    public boolean verData(Map map, List<String> keys){
        Set<String> set = map.keySet();
        if(!set.containsAll(keys)){
            new ShowMessageDialog().showWarningDialog("必输项不能为空");
            return false;
        }
        for (String key: keys) {
            String value = String.valueOf(map.get(key));
            if(null == value || value.equals("")){
                new ShowMessageDialog().showWarningDialog("必输项不能为空");
                return false;
            }
        }
        return true;
    }
}
