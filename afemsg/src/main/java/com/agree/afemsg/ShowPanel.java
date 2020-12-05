package com.agree.afemsg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王鹏
 * @date 2020/11/18 15:42
 */
public class ShowPanel {
    JFrame newJFrame;
    private ShowChooseFilePanel showChooseFilePanel = new ShowChooseFilePanel();
    public Map showPanel(){
        HashMap map = new HashMap<>();
        JFrame jf = new JFrame("AFE报文生成器v1.0");
        jf.setPreferredSize(new Dimension(500,330));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 创建一个 5 行 10 列的文本区域  tips
        final JTextArea textArea = new JTextArea(5, 10);
        // 设置自动换行
        textArea.setLineWrap(true);
        textArea.setForeground(Color.RED);
        textArea.setEditable(false);//不可编辑
        textArea.setText("初版\n");
        //tips滚动窗口
        ShowJScrollPane showJScrollPane = new ShowJScrollPane();
        JScrollPane jScrollPane = showJScrollPane.getJScrollPane(textArea);
        // 第 1 个 JPanel, 使用默认的浮动布局
        JPanel panel01 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel01.add(new JLabel("         \t\t\t请选择文档路径: "));
//        JTextField filetextField = new JTextField(10);
        JTextField filetextField = new DropDragSupportTextArea();
        filetextField.setColumns(9);
        panel01.add(filetextField);

        // 第 3 个 JPanel, 使用默认的浮动布局
        JPanel panel03 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel03.add(new JLabel("请选择报文文件输出路径: "));
        JTextField expotrTextField = new DropDragSupportTextArea();
        expotrTextField.setColumns(9);
        panel03.add(expotrTextField);

        // 第 4个 JPanel, 使用浮动布局, 并且容器内组件居中显示
        JPanel panel04 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // 创建按钮
        final JButton confirmBut = new JButton("确定");
        final JButton fileBut = new JButton("选择文件");
        final JButton exportBut = new JButton("选择文件路径");

        // 系统简称
        JPanel panel06 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel01.add(fileBut);
        panel03.add(exportBut);
        panel04.add(confirmBut);
        // title
        JPanel panel07 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("    欢迎使用          ");
        panel07.add(title);
        fileBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //调用文件选择器

                showChooseFilePanel.showChooseExcleFilePanel(jf,map);
                System.out.println("接口文档路径"+map.get("excleFilePath"));
                filetextField.setText(null!= map.get("excleFilePath") ? String.valueOf(map.get("excleFilePath"))  : "");
            }
        });
        exportBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //调用文件选择器
                showChooseFilePanel.showChooseExportFilePanel(jf,map);
                System.out.println("输出路径"+map.get("exportFilePath"));
                expotrTextField.setText(null!= map.get("exportFilePath") ? String.valueOf(map.get("exportFilePath"))  : "");
            }
        });

        // 创建一个垂直盒子容器, 把上面 3 个 JPanel 串起来作为内容面板添加到窗口
        Box vBox = Box.createVerticalBox();
        vBox.setBackground(Color.red);
        vBox.add(jScrollPane);
        vBox.add(panel07);
        vBox.add(panel06);
        vBox.add(panel01);
        vBox.add(panel03);
        vBox.add(panel04);
        jf.setContentPane(vBox);
        // 添加按钮的点击事件监听器
        confirmBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.put("excleFilePath",filetextField.getText());
                map.put("exportFilePath",expotrTextField.getText());
                //验证数据
                VerData verData = new VerData();
                List<String> keys = new ArrayList<>();
                keys.add("excleFilePath");
                keys.add("exportFilePath");

                if (verData.verData(map,keys)){
                    // 创建一个新窗口
                    newJFrame = new JFrame("温馨提示");
                    newJFrame.setSize(250, 100);
                    // 把新窗口的位置设置到 relativeWindow 窗口的中心
                    newJFrame.setLocationRelativeTo(jf);
                    // 窗口设置为不可改变大小
                    newJFrame.setResizable(false);
                    JPanel panel = new JPanel(new GridLayout(1, 1));
                    // 在新窗口中显示一个标签
                    JTextField jTextField = new JTextField();
                    panel.add(new JLabel("                            处理中,请稍候..."));
                    newJFrame.setContentPane(panel);
                    newJFrame.setVisible(true);
                    //异步加载  后台执行操作
                    new SwingWorker<Object,Void>(){
                        //另起新线程执行业务代码
                        @Override
                        protected Object doInBackground(){
                            CreateAFEMsg createAFEMsg = new CreateAFEMsg();
                            createAFEMsg.init(String.valueOf(map.get("excleFilePath")),String.valueOf(map.get("exportFilePath")));
                            return null;
                        }
                        //业务代码执行完成
                        @Override
                        protected void done() {
                            newJFrame.setVisible(false);
                        }
                    }.execute();
                }

            }
        });
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        return map;
    }
}
