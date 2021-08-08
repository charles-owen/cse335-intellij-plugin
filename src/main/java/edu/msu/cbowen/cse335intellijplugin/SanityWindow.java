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


    public SanityWindow(ToolWindow toolWindow) {

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

        for(int i=0; i<20; i++) {
            add("\n" + i + " New line added...");
        }

        return panel;
    }

    public void add(String text) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {

        }
    }
}
