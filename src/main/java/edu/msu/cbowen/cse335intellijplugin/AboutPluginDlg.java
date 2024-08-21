package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * The About dialog box
 */
public class AboutPluginDlg extends DialogWrapper {

    private JPanel panel;
    private JLabel image;
    private JLabel version;
    private JLabel hash;

    public AboutPluginDlg() {
        super(true);
        setTitle("About the CSE335 Plugin");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        // Set the version string
        String ver = PluginManagerCore.getPlugin(PluginId.getId("edu.msu.cbowen.cse335intellijplugin")).getVersion();
        if(ver != null) {
            version.setText("Version " + ver);
        }

        Connection connection = Connection.getInstance();
        if(connection.getState() == Connection.States.DISCONNECTED)
        {
            hash.setText("Plugin code is not available if not logged in.");
            return panel;
        }

        //
        // Get the user Id for the output file
        //
        AppSettingsState settings = AppSettingsState.getInstance();
        if(settings == null) {
            hash.setText("Unable to get application settings.");
            return panel;
        }

        String userId = settings.userId.toLowerCase().trim();
        if(userId.equals("")) {
            hash.setText("User ID has not been set in settings/preferences. Look under Tools/CSE 335 Plugin Settings.");
            return panel;
        }

        var toHash = userId + "-" + ver;
        var hashed = Hasher.hash(toHash);
        hash.setText("Plugin Code: " + hashed);
        return panel;
    }

    /**
     * Creates actions for dialog.
     * <p/>
     * By default "OK" and "Cancel" actions are returned. The "Help" action is automatically added if
     * {@link #getHelpId()} returns non-null value.
     * <p/>
     * Each action is represented by {@code JButton} created by {@link #createJButtonForAction(Action)}.
     * These buttons are then placed into {@link #createSouthPanel() south panel} of dialog.
     *
     * @return dialog actions
     */
    protected Action [] createActions() {
        return new Action[]{getOKAction()};
    }

    private void createUIComponents() {


        InputStream stream = getClass().getResourceAsStream("/logoicon.png");
        try {
            image = new JLabel(new ImageIcon(ImageIO.read(stream)));
        } catch (IOException e) {
            image = new JLabel();
        }
    }
}
