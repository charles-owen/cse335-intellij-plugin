package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple class to check to include cycles.
 */
public class SanityIncludeCycleCheck {

    private Project project = null;

    private Pattern includePattern;

    private static class Node {
        VirtualFile file = null;
        String path = null;
        String filename = null;
        ArrayList<String> includes = new ArrayList<>();
        int visit = 0;
    }

    private HashMap<String, Node> nodes = new HashMap<>();

    public SanityIncludeCycleCheck() {
        includePattern = Pattern.compile("^\\s*#\\s*include\\s+[<\"]([^>\"]*)[>\"]\\s*");

    }

    public void start(Project project) {
        nodes.clear();
        this.project = project;
    }

    /**
     * Add files to the cycle checker
     * @param file Intellij Virtual File
     * @param path String path to the file
     * @param lines Lines of text from the file
     */
    public void file(VirtualFile file, String path, String[] lines) {
        Path wholePath = Paths.get(path);
        Path filename = wholePath.getFileName();

        var node = new Node();
        node.file = file;
        node.path = path;
        node.filename = filename.toString();
        nodes.put(filename.toString(), node);

        for (String line : lines) {
            Matcher matcher = includePattern.matcher(line);
            if (matcher.matches()) {
                node.includes.add(matcher.group(1));
                // System.out.println(path + " includes " + matcher.group(1));
            }
        }
    }

    /**
     * Check the graph for cycles.
     * @return null if none or list of files for the first cycle found.
     */
    public List<String> check() {
        // Clear the visited flags
        for(String fileToClear : nodes.keySet()) {
            nodes.get(fileToClear).visit = 0;
        }

        int visit = 0;
        for(String file : nodes.keySet()) {
            visit++;
            var node = nodes.get(file);
            // If the node has already been visited, ignore it
            if(node.visit > 0) {
                continue;
            }

            ArrayList<String> names = new ArrayList<>();
            if(visit(node, names, visit)) {
                // We found a cycle!
                // Remove anything from the front that is
                // before the actual cycle.
                var last = names.get(names.size()-1);
                while(names.size() > 0 && !names.get(0).equals(last)) {
                    names.remove(0);
                }

                return names;
            }
        }

        return null;
    }

    /**
     * Visit a node in the graph
     * @param node Node we are visiting
     * @param names Array of all nodes visited up to this point
     * @param visit The visit flag value
     * @return true if cycle found at this node
     */
    private boolean visit(Node node, ArrayList<String> names, int visit) {
        // Add this node to the list of filenames
        names.add(node.filename);

        // See if this node has already been visited
        if(node.visit > 0) {
            // We are visiting a node we have previously visited
            // at any point. If it is on the list of nodes that
            // got to here, we have a cycle
            for(int n=0; n<names.size()-1; n++) {
                if(names.get(n).equals(node.filename)) {
                    return true;
                }
            }

            // If we hit a node that has been previously
            // visited, but is not part of a cycle,
            // there there is no cycle along this path
            names.remove(names.size()-1);
            return false;
        }

        node.visit = visit;

        for(var includedFile : node.includes) {
            var included = nodes.get(includedFile);
            if(included != null) {
                if(visit(included, names, visit)) {
                    return true;
                }

            }
        }

        names.remove(names.size()-1);
        return false;
    }
}
