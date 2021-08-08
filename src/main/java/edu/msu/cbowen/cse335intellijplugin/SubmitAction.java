package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * CSE335>Export>Submit...
 * Action class that submits assignment to server
 */
public class SubmitAction extends ExportOrSubmitAction {
    /**
     * Handle menu action
     * @param event Action event
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        //
        // Common to both exporting and submission
        //
        if(!actionPre(event)) {
            return;
        }

        Connection connection = Connection.getInstance();

        //
        // Determine open submissions
        //
        OpenSubmissions open = new OpenSubmissions(connection);
        APIValue choices = open.getOpen();
        if (choices == null) {
            Messages.showMessageDialog((Project)null,
                    open.getError(),
                    "Unable to submit", null);

            return;
        }

        if(choices.size() == 0) {
            Messages.showMessageDialog((Project)null,
                    "There are no open submissions at this time",
                    "Unable to submit", null);
            return;
        }

        OpenSubmissionsDlg submissionSelectDlg = new OpenSubmissionsDlg(choices);
        submissionSelectDlg.show();
        if(!submissionSelectDlg.isOK()) {
            // If we cancelled...
            return;
        }

        APIValue selection = submissionSelectDlg.getSelection();
        String assignTag = selection.getAsString("assign");
        String submitTag = selection.getAsString("submitTag");
        String team = selection.getAsString("teaming");

        String json;

        try {
            String https_url = team == null ?
                    connection.getServer() + Connection.SUBMIT_PATH + assignTag + "/" + submitTag :
                    connection.getServer() + Connection.TEAM_SUBMIT_PATH + assignTag + "/" + submitTag;

            MultipartUtility multipart = new MultipartUtility(https_url, "UTF-8");
            multipart.addFormField("assign", assignTag);
            multipart.addFormField("tag", submitTag);
            multipart.addFormField("type", "application/zip");
            multipart.addFilePart("file", getUserId() + ".zip");

            DirectoryToZip d2z = new DirectoryToZip();
            d2z.excludeStandard();
            d2z.zip(getProjectBaseDir(), multipart.getStream());

            multipart.addFilePartEnd();
            json = multipart.finish(); // response from server.

        } catch (IOException e) {
            Messages.showMessageDialog(getProject(),
                    "Error communicating with server: " + e.getLocalizedMessage(),
                    "Unable to submit", null);
            return;
        }

        APIResponse response = new APIResponse(json);
        if(response.hasError()) {
            // Submission failed
            Messages.showMessageDialog(getProject(),
                    response.getErrorTitle(),
                    "Assignment failed to upload to course server", null);
            return;
        }

        Messages.showMessageDialog(getProject(),
                "Submission successfully uploaded to the CSE335 server",
                "Submission Successful", null);
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
