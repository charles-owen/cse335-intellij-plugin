package edu.msu.cbowen.cse335intellijplugin;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Action class for CSE335>Export>Export to ZIP menu option
 */
public class ExportToZipAction extends ExportOrSubmitAction {

    /**
     * Handle the performed action
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //
        // Common to both exporting and submission
        //
        if(!actionPre(event)) {
            return;
        }

        //
        // File save dialog box
        //
        String[] extensions = new String[]{"zip"} ;
        FileSaverDescriptor fsd = new FileSaverDescriptor("Save .zip file",
                "Select output Zip file to save project to.", extensions);

        FileSaverDialog fileSaverDialog = FileChooserFactory.getInstance().createSaveFileDialog(fsd, getProject());
        VirtualFileWrapper result = fileSaverDialog.save(userId + ".zip");
        if(result == null) {
            return;
        }

        //
        // Save to a file
        //

        try (OutputStream stream = Files.newOutputStream(result.getFile().toPath())) {
            DirectoryToZip d2z = new DirectoryToZip();
            d2z.excludeStandard();
            d2z.zip(getProjectBaseDir(), stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
