package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

public class LogoutAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Connection connection = Connection.getInstance();
        connection.logout();
    }

    /**
     * Determines whether this menu item is available for the current context.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    @Override
    public void update(AnActionEvent e) {
        Connection connection = Connection.getInstance();

        Presentation presentation = e.getPresentation();
        presentation.setEnabledAndVisible(connection.getState() != Connection.States.DISCONNECTED);
    }

}
