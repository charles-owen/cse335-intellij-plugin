package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class AboutAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AboutPluginDlg dlg = new AboutPluginDlg();
        dlg.show();
    }
}
