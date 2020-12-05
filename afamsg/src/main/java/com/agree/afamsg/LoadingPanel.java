package com.agree.afamsg;

/**
 * @author 王鹏
 * @date 2020/11/19 15:53
 */
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;

import javax.swing.*;

public class LoadingPanel {
    public static JXFrame showLoading(JFrame relativeWindow){
        JXFrame frame = new JXFrame("", true);
        frame.setLocationRelativeTo(relativeWindow);
        JXBusyLabel label = new JXBusyLabel();
        JLabel tipsText = new JLabel("处理中,请稍候.....");

        frame.add(tipsText);
        frame.setSize(200, 100);
        //...
        label.setBusy(true);
        frame.setVisible(true);
        return frame;
    }
}
