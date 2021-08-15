package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SanityCheck {

    /// Include cycle checker
    private final SanityIncludeCycleCheck includeCycleCheck = new SanityIncludeCycleCheck();

    private SanityWindow window = null;

    /// using namespace pattern, not allowed in .h files
    private final Pattern usingNamespacePattern;

    /// @file pattern, must exist in every file
    private Pattern filePattern = null;
    private boolean fileExists = false;

    /// @author pattern, must exist in every file
    private final Pattern authorPattern;
    private boolean authorExists = false;

    /// <pch.h> or "pch.h" included
    private final Pattern pchPattern;
    private final Pattern pchPatternTest;
    private boolean pchExists = false;

    /// Quoted includes in tests
    private final Pattern quotedIncludes;

    /// Does this project has a pch.h file?
    private boolean hasPch = false;

    /// How many errors?
    private int errorCnt = 0;

    private Project project = null;

    private JBColor jbErrorColor = null;

    public SanityCheck() {
        usingNamespacePattern = Pattern.compile("using\\s+namespace");
        authorPattern = Pattern.compile("@author\\s+[a-zA-Z]");
        pchPattern = Pattern.compile("#include\\s+\"pch.h\"");
        pchPatternTest = Pattern.compile("#include\\s+<pch.h>");
        quotedIncludes = Pattern.compile("#include\\s\"");
    }

    public void setWindow(SanityWindow window) {
        this.window = window;
    }

    public void check(Project project) {
        if(window == null) {
            return;
        }

        var errorColor = JBUI.CurrentTheme.Focus.errorColor(true);
        jbErrorColor = new JBColor(errorColor, new Color(255, 112, 110));

        this.project = project;
        includeCycleCheck.start(project);

        window.show();
        window.clear();
        window.add("Sanity checking...");

        hasPch = false;
        errorCnt = 0;

        //
        // Get all source files
        //
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for(VirtualFile file : vFiles) {
            String filePath = file.getPath();

            // Determine if this project uses precompiled headers in pch.h
            if(filePath.endsWith("pch.h")) {
                hasPch = true;
            }
        }

        for(VirtualFile file : vFiles) {
            String filePath = file.getPath();

            // System.out.println(filePath);
            checkFile(file, filePath);
        }

        var cycle = includeCycleCheck.check();
        if(cycle != null) {
            // We have an "include cycle"!
            window.add("\nYou have an include cycle!", jbErrorColor);
            StringBuilder msg = new StringBuilder("\n" + cycle.get(0));
            for(int i=1; i<cycle.size(); i++) {
                msg.append(" includes ").append(cycle.get(i));
            }

            window.add(msg.toString(), jbErrorColor);
            errorCnt++;
        }

        if(errorCnt == 0) {
            window.add("\nCheck complete with no errors");
        } else if(errorCnt == 1) {
            window.add("\nCheck complete with " + errorCnt + " error");
        } else {
            window.add("\nCheck complete with " + errorCnt + " errors");
        }

    }

    private void checkFile(VirtualFile file, String path) {
        Path wholePath = Paths.get(path);
        Path filename = wholePath.getFileName();
        var filenameStr = filename.toString();

        //
        // Files we ignore
        //
        String[] ignores = new String[]{"gtest_main.cpp", "EmptyTest.cpp"};
        for(var ignore : ignores) {
            if(filenameStr.equals(ignore)) {
                return;
            }
        }

        if(wholePath.getNameCount() < 2) {
            return;
        }

        // Is this in a test?
        var parent = wholePath.subpath(wholePath.getNameCount()-2, wholePath.getNameCount()-1);
        boolean isTest = parent.toString().equals("Tests") || parent.toString().equals("Test");

        boolean isH = path.endsWith(".h");
        boolean isCPP = path.endsWith(".cpp");
        boolean isSource = isH || isCPP;

        filePattern = Pattern.compile("@file\\s+" + filename);
        fileExists = false;

        authorExists = false;
        pchExists = false;

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if(document == null) {
            return;
        }
        CharSequence charSequence = document.getImmutableCharSequence();
        String[] lines = charSequence.toString().split("\\r?\\n");

        includeCycleCheck.file(file, path, lines);

        for(int i=0; i<lines.length; i++) {
            checkLine(file, path, i+1, lines[i], isTest);
        }

        if(!isTest && isSource && !authorExists) {
            error(file, path, 0, Errors.MissingAuthor);
        }

        if(!isTest && isSource && !fileExists) {
            error(file, path, 0, Errors.MissingFile);
        }

        if(hasPch && isCPP && !pchExists) {
            if(isTest) {
                error(file, path, 0, Errors.MissingPCHTest);
            } else {
                error(file, path, 0, Errors.MissingPCH);
            }
        }

    }

    private void checkLine(VirtualFile file, String path, int lineNumber, String line, boolean isTest) {
        boolean isH = path.endsWith(".h");
        boolean isCPP = path.endsWith(".cpp");

        if(isH) {
            if(test(usingNamespacePattern, line)) {
                error(file, path, lineNumber, Errors.UsingNamespace);
            }
        }

        if(test(authorPattern, line)) {
            authorExists = true;
        }

        if(test(filePattern, line)) {
            fileExists = true;
        }

        if(isCPP) {
            if(isTest) {
                // Test-specific .cpp files
                if(test(pchPatternTest, line)) {
                    pchExists = true;
                }

                // Test for quoted includes
                if(test(quotedIncludes, line)) {
                    if(!line.contains("gtest/")) {
                        error(file, path, lineNumber, Errors.QuotedIncludesTest);
                    }
                }
            } else {
                if(test(pchPattern, line)) {
                    pchExists = true;
                }
            }

        }
    }

    private boolean test(Pattern pattern, String line) {
        Matcher m = pattern.matcher(line);
        return m.find();
    }

    private void error(VirtualFile file, String path, int line, String message) {
        window.error(project, file, path, line, message, jbErrorColor);
        errorCnt++;
    }

    /**
     * Access the instance of Connection as a service.
     * @return Pointer to the connection service object
     */
    public static SanityCheck getInstance() {

        return ApplicationManager.getApplication().getService(SanityCheck.class);
    }
}
