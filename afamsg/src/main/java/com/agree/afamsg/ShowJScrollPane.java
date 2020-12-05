package com.agree.afamsg;

import javax.swing.*;

/**
 * @author 王鹏
 * @date 2020/11/19 13:56
 */
public class ShowJScrollPane {
    public JScrollPane getJScrollPane(JTextArea textArea){
        return new JScrollPane(
                textArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
    }
}
