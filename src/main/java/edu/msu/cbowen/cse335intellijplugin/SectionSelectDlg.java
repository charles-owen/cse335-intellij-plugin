package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog box for selecting the section a course member is in.
 */
public class SectionSelectDlg extends DialogWrapper {


    private final APIValue sections;

    private APIValue selection = null;

    /**
     * Constructor
     * @param sections An array of sections
     */
    public SectionSelectDlg(APIValue sections) {
        super(true);
        this.sections = sections;
        setTitle("Selection Section");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(sections.size()+1, 1));

        JLabel label = new JLabel("Please Select a Section", SwingConstants.CENTER);
        dialogPanel.add(label);

        for(int i=0; i<sections.size(); i++) {
            APIValue section = sections.get(i);
            String course = section.getAsString("course");
            String desc = section.getAsString("desc");
            String nice = section.getAsString("nice");

            JButton button = new JButton("    " + course + " " + desc + " " + nice + "    ");
            button.setVerticalTextPosition(AbstractButton.CENTER);
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selection = section;
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
