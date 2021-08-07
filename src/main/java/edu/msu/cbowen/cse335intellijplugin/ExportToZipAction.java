package edu.msu.cbowen.cse335intellijplugin;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.components.impl.stores.IProjectStore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.ServiceKt;

import java.nio.file.Path;

/**
 * Action class for CSE335>Export>Export to ZIP menu option
 */
public class ExportToZipAction extends AnAction {

    /**
     * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
     * declarations. Only needed in {@link ExportToZipAction} class because a second constructor is overridden.
     *
     * @see AnAction#AnAction()
     */
    public ExportToZipAction() {
        super();
    }

//    /**
//     * This constructor is used to support dynamically added menu actions.
//     * It sets the text, description to be displayed for the menu item.
//     * Otherwise, the default AnAction constructor is used by the IntelliJ Platform.
//     *
//     * @param text        The text to be displayed as a menu item.
//     * @param description The description of the menu item.
//     * @param icon        The icon to be used with the menu item.
//     */
//    public ExportToZipAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
//        super(text, description, icon);
//    }

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

        IProjectStore projectStore = (IProjectStore)ServiceKt.getStateStore(currentProject);
        Path existingBaseDir = projectStore.getProjectBasePath();

        //
        // Get the user Id for the output file
        //
        AppSettingsState settings = AppSettingsState.getInstance();
        String userId = settings.userId;
        if(userId.isBlank()) {
            userId = "student";
        }

        //
        // User warning
        //
        // Prior to exporting or submitting, be sure to save and make sure your solution builds okay.
        //



        StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        // If an element is selected in the editor, add info about it.
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
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
