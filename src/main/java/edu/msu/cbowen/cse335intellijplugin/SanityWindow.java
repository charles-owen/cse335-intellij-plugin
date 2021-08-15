package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SanityWindow {

    private JTextPane textPane = null;
    private final ToolWindow toolWindow;


    public SanityWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        SanityCheck checker = SanityCheck.getInstance();
        checker.setWindow(this);
    }


    public JPanel getContent() {
        var panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.gridx = 0;
        cons.gridy = 0;

        textPane = new JTextPane();
        textPane.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JBScrollPane scrollPane = new JBScrollPane(textPane);

        panel.add(scrollPane, cons);

        textPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clicked(textPane.viewToModel2D(e.getPoint()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        return panel;
    }

    private void clicked(int ch) {
        for(ErrorLine errorLine : errors) {
            if(ch >= errorLine.start && ch < errorLine.end) {
                int line = errorLine.line > 0 ? errorLine.line - 1 : 0;
                new OpenFileDescriptor(errorLine.project, errorLine.file,
                        line, 0).navigate(true);
                return;
            }
        }
    }

    public void show() {
        toolWindow.show();
    }

    public void clear() {
        textPane.setText("");
        errors.clear();
    }

    private static class ErrorLine {
        public Project project;
        public VirtualFile file;
        public String path;
        public int line;
        public int start;
        public int end;
    }

    private final ArrayList<ErrorLine> errors = new ArrayList<>();

    public void error(Project project, VirtualFile file, String path, int line, String message, JBColor errorColor) {
        var errorLine = new ErrorLine();
        errorLine.project = project;
        errorLine.file = file;
        errorLine.path = path;
        errorLine.line = line;

        String msg = "\n" + path;
        if(line > 0) {
            msg += "[" + line + "]";
        }

        msg += ": ";

        StyledDocument doc = textPane.getStyledDocument();
        try {
            errorLine.start = doc.getLength();

            var attributeSet = new SimpleAttributeSet();
            StyleConstants.setForeground(attributeSet, errorColor);

            doc.insertString(doc.getLength(), msg, attributeSet);
            errorLine.end = doc.getLength();
            errors.add(errorLine);

            doc.insertString(doc.getLength(), message, null);
        } catch (BadLocationException ignored) {

        }

    }

    public void add(String text) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException ignored) {

        }
    }

    public void add(String text, JBColor color) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            var attributeSet = new SimpleAttributeSet();
            StyleConstants.setForeground(attributeSet, color);

            doc.insertString(doc.getLength(), text, attributeSet);
        } catch (BadLocationException ignored) {

        }
    }


}
