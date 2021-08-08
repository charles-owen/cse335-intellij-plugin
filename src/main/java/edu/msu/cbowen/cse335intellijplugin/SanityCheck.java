package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SanityCheck {

    SanityWindow window = null;

    /// using namespace pattern, not allowed in .h files
    Pattern usingNamespacePattern;

    /// @file pattern, must exist in every file
    Pattern filePattern = null;
    boolean fileExists = false;

    /// @author pattern, must exist in every file
    Pattern authorPattern;
    boolean authorExists = false;

    public SanityCheck() {
        usingNamespacePattern = Pattern.compile("using\\s+namespace");
        authorPattern = Pattern.compile("@author\\s+[a-zA-Z]");

    }

    public void setWindow(SanityWindow window) {
        this.window = window;
    }

    public void check(Project project) {
        if(window == null) {
            return;
        }

        window.show();
        window.clear();
        window.add("Sanity checking...");

        //
        // Get all source files
        //
        VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for(VirtualFile file : vFiles) {
            String filePath = file.getPath();

            // System.out.println(filePath);
            checkFile(file, filePath);
        }

        window.add("\nCheck complete");
    }

    private void checkFile(VirtualFile file, String path) {
        Path wholePath = Paths.get(path);
        Path filename = wholePath.getFileName();
        filePattern = Pattern.compile("@file\\s+" + filename.toString());
        fileExists = false;

        int lineNumber = 1;
        boolean isH = path.endsWith(".h");
        boolean isCPP = path.endsWith(".cpp");

        authorExists = false;

        Document document = FileDocumentManager.getInstance().getDocument(file);
        CharSequence charSequence = document.getImmutableCharSequence();
        String[] lines = charSequence.toString().split("\\r?\\n");

        for(int i=0; i<lines.length; i++) {
            checkLine(path, i+1, lines[i]);
        }

//        try(BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
//            for(String line; (line = br.readLine()) != null; lineNumber++) {
//                // process the line.
//
//                checkLine(path, lineNumber, line);
//            }
//        } catch (IOException e) {
//            error(path, 0, "Error reading file");
//        }

        if((isH || isCPP) && !authorExists) {
            error(path, 0, "SC002 Missing @author Doxygen directive or author name");
        }

        if((isH || isCPP) && !fileExists) {
            error(path, 0, "SC003 Missing @file Doxygen directive or file name");
        }
    }

    private void checkLine(String path, int lineNumber, String line) {
        boolean isH = path.endsWith(".h");
        boolean isCPP = path.endsWith(".cpp");

        if(isH) {
            if(test(usingNamespacePattern, line)) {
                error(path, lineNumber, "SC001 Using namespace in a .h file is not allowed");
            }
        }

        if(test(authorPattern, line)) {
            authorExists = true;
        }

        if(test(filePattern, line)) {
            fileExists = true;
        }
    }

    private boolean test(Pattern pattern, String line) {
        Matcher m = pattern.matcher(line);
        return m.find();
    }

    private void error(String path, int line, String message) {
        String msg = "\n" + path;
        if(line > 0) {
            msg += "[" + line + "]";
        }

        msg += ": " + message;
        window.add(msg);
    }

    /**
     * Access the instance of Connection as a service.
     * @return Pointer to the connection service object
     */
    public static SanityCheck getInstance() {

        return ApplicationManager.getApplication().getService(SanityCheck.class);
    }
}
