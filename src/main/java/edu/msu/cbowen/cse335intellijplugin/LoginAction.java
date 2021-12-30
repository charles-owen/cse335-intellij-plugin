package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Action class for CSE335>Log In to log into the server
 */
public class LoginAction extends AnAction {

    /**
     * Handle the performed action
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //
        // Get the user Id for the output file
        //
        AppSettingsState settings = AppSettingsState.getInstance();
        if(settings == null) {
            return;
        }

        String userId = settings.userId;
        if(userId.equals("")) {
            Messages.showMessageDialog((Project)null, "User ID has not been set. Please go to the settings/preferences for the CSE335 plugin to set.",
                    "User ID not set", null);

            return;
        }

        //
        // Get login password
        //
        LoginDlg loginDlg = new LoginDlg();
        loginDlg.show();
        if(!loginDlg.isOK()) {
            return;
        }

        String password = loginDlg.getPassword();
        if(loginDlg.isPasswordChecked()) {
            // Save the password
            settings.savePassword(userId, password);
        } else {
            settings.savePassword(userId, null);
        }

        //
        // Attempt to log in
        //
        Connection connection = Connection.getInstance();
        if(!connection.login(userId, password)) {
            Messages.showMessageDialog((Project)null,
                    connection.getError(),
                    "Unable to log in", null);

            return;
        }

        //
        // Determine the section
        //
        Connection.States state = connection.determineSection();
        switch(state) {
            case DISCONNECTED:
                Messages.showMessageDialog((Project)null,
                        connection.getError(),
                        "Unable to log in", null);

                return;

            case CONNECTED:
                // We are connected, so we are done!
                onConnected(connection);
                return;
        }

        SectionSelectDlg sectionSelectDlg = new SectionSelectDlg(connection.getSections());
        sectionSelectDlg.show();
        if(!sectionSelectDlg.isOK()) {
            // If we cancelled, cancel the entire login process
            connection.logout();
            return;
        }

        APIValue selectedSection = sectionSelectDlg.getSelection();
        state = connection.selectSection(selectedSection);
        if(state == Connection.States.DISCONNECTED) {
            Messages.showMessageDialog((Project)null,
                    connection.getError(),
                    "Unable to log in", null);
        }

        onConnected(connection);
    }

    private void onConnected(@NotNull Connection connection) {

        //
        // And we are connected!
        //

        var ide = connection.queryIDE();
        int x = 7;
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
        presentation.setEnabledAndVisible(connection.getState() == Connection.States.DISCONNECTED);
    }
}
