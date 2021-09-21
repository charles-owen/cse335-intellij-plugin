package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.vfs.VirtualFile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SanityCheckTest {

    private static class ErrorItem {
        ErrorItem(VirtualFile file, String path, int line, String message) {
            this.path = path;
            this.line = line;
            this.message = message;
        }

        public String path;
        public int line;
        public String message;
    }

    private static class SanityCheckMock extends SanityCheck {

        public SanityCheckMock() {
            super();
        }

        @Override
        protected void error(VirtualFile file, String path, int line, String message) {
            errors.add(new ErrorItem(file, path, line, message));
        }

        public void clear() {
            errors.clear();
        }

        public List<ErrorItem> getErrors() {
            return errors;
        }

        private ArrayList<ErrorItem> errors = new ArrayList<>();
    }

    @Test
    void check() {

        // Check with no error
        var sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.cpp",
                noErrors, false);

        assertEquals(0, sanity.getErrors().size());

        // Check with missing PCH
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.cpp",
                missingPCH, false);

        assertEquals(1, sanity.getErrors().size());
        assertEquals(Errors.MissingPCH, sanity.getErrors().get(0).message);
    }

    private static String noErrors = "/**\n" +
            " * @file Aquarium.cpp\n" +
            " * @author Student\n" +
            " */\n" +
            "\n" +
            "#include \"pch.h\"\n" +
            "#include <wx/dcbuffer.h>\n" +
            "#include \"Aquarium.h\"\n" +
            "\n" +
            "using namespace std;\n";


    private static String missingPCH = "/**\n" +
            " * @file Aquarium.cpp\n" +
            " * @author Student\n" +
            " */\n" +
            "\n" +
            "#include <wx/dcbuffer.h>\n" +
            "#include \"Aquarium.h\"\n" +
            "\n" +
            "using namespace std;\n";
}
