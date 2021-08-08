package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class SanityWindow {

    private JPanel panel;
    private JTextPane textPane = null;
    private ToolWindow toolWindow;


    public SanityWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        SanityCheck checker = SanityCheck.getInstance();
        checker.setWindow(this);
    }


    public JPanel getContent() {
        panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridx = 0;
        cons.gridy = 0;

        textPane = new JTextPane();

        JBScrollPane scrollPane = new JBScrollPane(textPane);

        panel.add(scrollPane, cons);

        return panel;
    }

    public void show() {
        toolWindow.show();
    }

    public void clear() {
        textPane.setText("");
    }

    public void add(String text) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {

        }
    }
}
