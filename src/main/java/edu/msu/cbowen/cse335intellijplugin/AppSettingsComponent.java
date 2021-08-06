package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

    public JPanel panel;
    public JBTextField nameTextField;
    public JBTextField userIdTextField;
    public JBTextField serverTextField;

    public AppSettingsComponent() {
//        myMainPanel = FormBuilder.createFormBuilder()
//                .addLabeledComponent(new JBLabel("Enter your name: "), nameTextField, 1, false)
//                .addLabeledComponent(new JBLabel("Enter your MSU user ID: "), userIdTextField, 1, false)
//                .addLabeledComponent(new JBLabel("Server: "), serverTextField, 1, false)
//
//                .addComponentFillVertically(new JPanel(), 0)
//                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public JComponent getPreferredFocusedComponent() {
        return nameTextField;
    }

    @NotNull
    public String getName() {
        return nameTextField.getText();
    }

    public void setName(@NotNull String newText) {
        nameTextField.setText(newText);
    }

    @NotNull
    public String getUserId() {
        return userIdTextField.getText();
    }

    public void setUserId(@NotNull String userId) {
        userIdTextField.setText(userId);
    }

    @NotNull
    public String getServer() {
        return serverTextField.getText();
    }

    public void setServer(@NotNull String server) {
        serverTextField.setText(server);
    }
}