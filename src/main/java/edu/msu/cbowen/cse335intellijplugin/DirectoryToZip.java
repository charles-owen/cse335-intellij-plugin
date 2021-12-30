package edu.msu.cbowen.cse335intellijplugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Convert a directory to a zip file/stream.
 */
public class DirectoryToZip {

    /// Path we exclude from the .zip file
    private final ArrayList<Pattern> excluded = new ArrayList<>();

    /**
     * Add a file regular expression to exclude
     * @param regex Regular expression
     */
    public void exclude(String regex) {
        excluded.add(Pattern.compile(regex));
    }

    /**
     * Exclude a standard set of files we do not upload
     * to the server.
     */
    public void excludeStandard() {
        exclude("[\\\\^/]\\.");
        exclude("[\\\\/](html|latex|rtf)[\\\\/]");
        exclude("[\\\\/]cmake-");
        exclude("(\\.zip$)|(\\.zip[\\\\/])");
        exclude("\\.(o|O)$");
    }

    /**
     * Test a file path to see if it is excluded
     * @param path Path to test
     * @return True if file path is excluded
     */
    public boolean toExclude(String path) {
        for(Pattern pattern : excluded) {
            Matcher matcher = pattern.matcher(path);
            if(matcher.find()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Zip a directory of content into a zip file.
     *
     * @param sourceDirPath Path to the directory
     * @param stream Stream for output of the zip data
     * @throws IOException on an input/output error
     */
    public void zip(String sourceDirPath, OutputStream stream) throws IOException {

        ZipOutputStream zs = new ZipOutputStream(stream);
        Path pp = Paths.get(sourceDirPath);
        Files.walk(pp)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    if(!toExclude(path.toString())) {

                        // System.out.println(path);

                        String relativePath = pp.relativize(path).toString();
                        if(File.separatorChar != '/') {
                            relativePath = relativePath.replace('\\', '/');
                        }
                        ZipEntry zipEntry = new ZipEntry(relativePath);
                        try {
                            zs.putNextEntry(zipEntry);
                            zs.write(Files.readAllBytes(path));
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e.getLocalizedMessage());
                        }
                    }

                });

        zs.flush();
        zs.finish();
    }
}
