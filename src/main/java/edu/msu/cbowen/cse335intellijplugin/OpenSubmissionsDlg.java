package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenSubmissionsDlg extends DialogWrapper {

    private final APIValue open;

    private APIValue selection = null;

    /**
     * Constructor
     * @param open An array of open submissions
     */
    public OpenSubmissionsDlg(APIValue open) {
        super(true);
        this.open = open;
        setTitle("Select Assignment to Submit");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel();
        if(open.size() == 0) {

            return dialogPanel;
        }

        dialogPanel.setLayout(new GridLayout(open.size()+1, 1));

        JLabel label = new JLabel("Choose the submission you wish to make", SwingConstants.CENTER);
        dialogPanel.add(label);

        for(int i=0; i<open.size(); i++) {
            APIValue submission = open.get(i);
            String assignName = submission.getAsString("assignName");
            String submitName = submission.getAsString("submitName");

            JButton button = new JButton("    " + assignName + ": " + submitName + "    ");
            button.setVerticalTextPosition(AbstractButton.CENTER);
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selection = submission;
                    close(DialogWrapper.OK_EXIT_CODE);
                }
            });

            dialogPanel.add(button);
        }

        return dialogPanel;
    }

    public APIValue getSelection() {
        return selection;
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
    protected Action @NotNull [] createActions() {
        return new Action[]{getCancelAction()};
    }
}
