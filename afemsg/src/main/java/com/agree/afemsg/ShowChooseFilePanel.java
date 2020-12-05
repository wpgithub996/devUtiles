package com.agree.afemsg;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Map;

/**
 * @author 王鹏
 * @date 2020/11/18 15:44
 */
public class ShowChooseFilePanel {
    /*
     * @desc 弹出接口文档选择菜单
     * @author wangpeng
     * @date 2020/11/18 9:13
     * @param [jFrame, map]
     * @return java.util.Map
     */
    public Map showChooseExcleFilePanel(JFrame jFrame, Map map){
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);

        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.xlsx", "xlsx"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(jFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            String fileAbsolutePath = file.getAbsolutePath();
            if(null != fileAbsolutePath && !fileAbsolutePath.equals("")){
                map.put("excleFilePath",file.getAbsolutePath());
            }
        }
        return  map;
    }


    /*
     * @desc 选择报文文件输出路径
     * @author wangpeng
     * @date 2020/11/18 9:13
     * @param [jFrame, map]
     * @return java.util.Map
     */
    public Map showChooseExportFilePanel(JFrame jFrame,Map map){
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();
        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));
        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(jFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            String fileAbsolutePath = file.getAbsolutePath();
            if (null != fileAbsolutePath && !fileAbsolutePath.equals("")){
                map.put("exportFilePath",file.getAbsolutePath());
            }
        }
        return  map;
    }
}
