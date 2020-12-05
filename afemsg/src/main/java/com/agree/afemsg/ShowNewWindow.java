package com.agree.afemsg;

import javax.swing.*;
import java.awt.*;

/**
 * @author 王鹏
 * @date 2020/11/19 15:29
 */
public class ShowNewWindow {
    public  JFrame  showNewWindow(JFrame relativeWindow) {
        // 创建一个新窗口
        JFrame newJFrame = new JFrame("温馨提示");

        newJFrame.setSize(250, 100);
//        newJFrame.setBounds(0,0,20,20);
        // 把新窗口的位置设置到 relativeWindow 窗口的中心
        newJFrame.setLocationRelativeTo(relativeWindow);

        // 点击窗口关闭按钮, 执行销毁窗口操作（如果设置为 EXIT_ON_CLOSE, 则点击新窗口关闭按钮后, 整个进程将结束）
//        newJFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // 窗口设置为不可改变大小
        newJFrame.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(1, 1));

        // 在新窗口中显示一个标签
        JTextField jTextField = new JTextField();
        panel.add(new JLabel("处理中,请稍候..."));

        newJFrame.setContentPane(panel);
        newJFrame.setVisible(true);
        return newJFrame;
    }
}
