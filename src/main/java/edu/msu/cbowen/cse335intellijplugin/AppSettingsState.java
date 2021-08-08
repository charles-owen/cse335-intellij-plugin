package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
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
        name = "edu.msu.cbowen.cse335intellijplugin.AppSettingsState",
        storages = {@Storage("cse335settings.xml")}
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    /// The user name (as in Charles B. Owen)
    public String name = "";

    /// The user ID (as in cbowen)
    public String userId = "";

    /// The remote server
    public String server = "https://facweb.cse.msu.edu/cbowen/cse335";

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


    private CredentialAttributes createCredentialAttributes() {
        return new CredentialAttributes(
                CredentialAttributesKt.generateServiceName("cse335-plugin", "cse335-plugin-key")
        );
    }

    /**
     * Save the password into the credentials store.
     * @param userId User ID
     * @param password Password, null to clear the store.
     */
    public void savePassword(String userId, @Nullable String password) {
        CredentialAttributes credentialAttributes = createCredentialAttributes();
        if(password != null) {
            Credentials credentials = new Credentials(userId, password);
            PasswordSafe.getInstance().set(credentialAttributes, credentials);
        } else {
            PasswordSafe.getInstance().set(credentialAttributes, null);
        }
    }

    /**
     * Retrieve password from credentials store.
     * @return Password if found or null otherwise.
     */
    public String retrievePassword() {
        CredentialAttributes credentialAttributes = createCredentialAttributes();

        String password = null;

        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
        if (credentials != null) {
            password = credentials.getPasswordAsString();
        } else {
            // or get password only
            password = PasswordSafe.getInstance().getPassword(credentialAttributes);
        }

        return password;
    }
}