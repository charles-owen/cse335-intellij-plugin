package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.ui.DialogWrapper;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog box presented prior to export or submission warning about
 * ensuring the project builds, etc.
 */
public class ExportWarningDlg extends DialogWrapper {

    public ExportWarningDlg() {
        super(true); // use current window as parent
        setTitle("Important!");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        String msg = "<html>Prior to exporting or submitting, be sure to save and " +
                "make sure your solution builds okay.</html>";

        JLabel label = new JLabel(msg);
        label.setPreferredSize(new Dimension(400, 50));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }


}
