package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.ui.DialogWrapper;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * Server login dialog box
 */
public class LoginDlg extends DialogWrapper {

    private JPasswordField passwordField;
    private JCheckBox savePasswordCheckBox;
    private JLabel userIdField;
    private JPanel panel;

    public LoginDlg() {
        super(true);
        setTitle("Log In");

        AppSettingsState settings = AppSettingsState.getInstance();
        if(settings != null) {
            userIdField.setText(settings.userId);
        }

        String password = settings.retrievePassword();
        savePasswordCheckBox.setSelected(password != null);
        if(password != null) {
            passwordField.setText(password);
        }

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        return panel;
    }


    public String getPassword() {
        char [] password = passwordField.getPassword();
        return new String(password);
    }

    public boolean isPasswordChecked() {
        return savePasswordCheckBox.isSelected();
    }
}
