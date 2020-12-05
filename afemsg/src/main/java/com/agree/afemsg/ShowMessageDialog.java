package com.agree.afemsg;

import javax.swing.*;

/**
 * @author 王鹏
 * @date 2020/11/18 15:47
 */
public class ShowMessageDialog {
    /*
     * @desc  弹出信息提示框
     * @author wangpeng
     * @date 2020/11/18 9:15
     * @param []
     * @return void
     */
    public void showMessageDialog(String msg){
        JOptionPane.showMessageDialog(
                null,
                msg,
                "提示",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * @desc  弹出警告提示框
     * @author wangpeng
     * @date 2020/11/18 9:15
     * @param []
     * @return void
     */
    public void showWarningDialog(String msg){
        JOptionPane.showMessageDialog(
                null,
                msg,
                "错误",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
