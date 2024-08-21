package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SanityAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //
        // Get a path to the current project
        //
        Project project = event.getProject();
        if(project == null) {
            return;
        }

        SanityCheck checker = SanityCheck.getInstance();
        checker.check(project);
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

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
