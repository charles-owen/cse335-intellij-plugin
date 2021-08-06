package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name
 * of the data and the file name where these persistent application settings are stored.
 */
@State(
        name = "edu.msu.cbowen.cse335intellijplugin.settings.AppSettingsState",
        storages = {@Storage("SdkSettingsPlugin.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    /// The user name (as in Charles B. Owen)
    public String name = "";

    /// The user ID (as in cbowen)
    public String userId = "";

    /// The remote server
    public String server = "https://facweb.cse.msu.edu/cbowen/cse335";

    /// Storage for the password if saved, null if not saved
    public String password = null;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}