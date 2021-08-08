package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for the export and submit actions.
 * Retains common code between them.
 */
public abstract class ExportOrSubmitAction extends AnAction {

    private Project project = null;
    String projectBaseDir = null;
    String userId = null;

    public Project getProject() {
        return project;
    }

    public String getProjectBaseDir() {
        return projectBaseDir;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Handle common activity at the beginning of both submission actions.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    public boolean actionPre(@NotNull AnActionEvent event) {
        //
        // Get a path to the current project
        //
        project = event.getProject();
        if(project == null) {
            return false;
        }

        VirtualFile[] vFiles =
                ProjectRootManager.getInstance(project).getContentRoots();
        if(vFiles.length < 1) {
            return false;
        }

        projectBaseDir = vFiles[0].getPath();

        //
        // Get the user Id for the output file
        //
        AppSettingsState settings = AppSettingsState.getInstance();
        if(settings == null) {
            return false;
        }

        userId = settings.userId;
        if(userId.equals("")) {
            userId = "student";
        }

        //
        // User warning
        //
        // Prior to exporting or submitting, be sure to save and make sure your solution builds okay.
        //
        ExportWarningDlg dlg = new ExportWarningDlg();
        dlg.show();
        if(!dlg.isOK()) {
            return false;
        }

        return true;
    }

}
