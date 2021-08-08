package edu.msu.cbowen.cse335intellijplugin;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Action class for CSE335>Export>Export to ZIP menu option
 */
public class ExportToZipAction extends AnAction {

    /**
     * Handle the performed action
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //
        // Get a path to the current project
        //
        Project currentProject = event.getProject();
        if(currentProject == null) {
            return;
        }

        VirtualFile[] vFiles =
                ProjectRootManager.getInstance(currentProject).getContentRoots();
        if(vFiles.length < 1) {
            return;
        }

        String projectBaseDir = vFiles[0].getPath();

        //
        // Get the user Id for the output file
        //
        AppSettingsState settings = AppSettingsState.getInstance();
        if(settings == null) {
            return;
        }

        String userId = settings.userId;
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
            return;
        }


        FileChooserDescriptor fcd = FileChooserDescriptorFactory.createSingleFileDescriptor(".zip");
        fcd.setTitle("Output Zip File");
        fcd.setDescription("Select output Zip file to save project to.");


        FileSaverDescriptor fsd = new FileSaverDescriptor("Save .zip file",
                "Select output Zip file to save project to.", ".zip");


        FileSaverDialog fileSaverDialog = FileChooserFactory.getInstance().createSaveFileDialog(fsd, currentProject);
        VirtualFileWrapper result = fileSaverDialog.save(userId);
        if(result == null) {
            return;
        }

        try (OutputStream stream = Files.newOutputStream(result.getFile().toPath())) {
            DirectoryToZip d2z = new DirectoryToZip();
            d2z.excludeStandard();
            d2z.zip(projectBaseDir, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
//        String dlgTitle = event.getPresentation().getDescription();
//        // If an element is selected in the editor, add info about it.
//        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
//        if (nav != null) {
//            dlgMsg.append(String.format("\nSelected Element: %s", nav));
//        }
//        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
    }

    /**
     * Determines whether this menu item is available for the current context.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

}
