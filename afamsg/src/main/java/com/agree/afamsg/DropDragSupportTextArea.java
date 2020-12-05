package com.agree.afamsg;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author 王鹏
 * @date 2020/11/19 12:35
 */
public class DropDragSupportTextArea extends JTextField implements DropTargetListener {
    private DropTarget dropTarget;

    public DropDragSupportTextArea() {
        //注册DropTarget，并将它与组件相连，处理哪个组件的相连
        //即连通组件（第一个this）和Listener(第二个this)
        dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, this, true);
    }

    /**
     * 拖入文件或字符串,这里只说明能拖拽，并未打开文件并显示到文本区域中
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
        if(dataFlavors[0].match(DataFlavor.javaFileListFlavor)){
            try {
                Transferable tr = dtde.getTransferable();
                Object obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
                List<File> files = (List<File>)obj;
//                for(int i = 0; i < files.size(); i++){
//                    append(files.get(i).getAbsolutePath());
//                }
                setText(files.get(0).getAbsolutePath());
            } catch (UnsupportedFlavorException ex) {

            } catch (IOException ex) {

            }
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

    }
}
