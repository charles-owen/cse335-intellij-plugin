package edu.msu.cbowen.cse335intellijplugin;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class AppSettingsConfigurable implements Configurable {

    /// The user interface for editing the settings
    private AppSettingsComponent mySettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "CSE335 Plugin Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getName().equals(settings.name);
        modified |= mySettingsComponent.getUserId().equals(settings.userId);
        modified |= mySettingsComponent.getServer().equals(settings.server);
        return modified;
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.name = mySettingsComponent.getName();
        settings.userId = mySettingsComponent.getUserId();
        settings.server = mySettingsComponent.getServer();
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setName(settings.name);
        mySettingsComponent.setUserId(settings.userId);
        mySettingsComponent.setServer(settings.server);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}