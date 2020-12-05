package com.agree.afemsg;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 进度条
 * @author 王鹏
 * @date 2020/11/19 14:27
 */
public class ShowJProgressBar {
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    volatile Integer value;
    public JProgressBar showJProgressBar(Integer max){
        this.value = max;
        JFrame jf = new JFrame("测试窗口");
        jf.setSize(250, 250);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        // 创建一个进度条
        final JProgressBar progressBar = new JProgressBar();

        // 设置进度的 最小值 和 最大值
        progressBar.setMinimum(MIN_PROGRESS);
        progressBar.setMaximum(MAX_PROGRESS);
        progressBar.setValue(value);
        // 添加到内容面板
        panel.add(progressBar);

        jf.setContentPane(panel);
        jf.setVisible(true);
        // 模拟延时操作进度, 每隔 0.5 秒更新进度
        new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                value++;
                if (value > MAX_PROGRESS) {
                    value = MIN_PROGRESS;
                }
                progressBar.setValue(value);
            }
        }).start();
        return progressBar;
    }

    public void setValue(Integer value,JProgressBar jProgressBar){
//        jProgressBar.setValue(value);
        this.value = value;
    }
}
