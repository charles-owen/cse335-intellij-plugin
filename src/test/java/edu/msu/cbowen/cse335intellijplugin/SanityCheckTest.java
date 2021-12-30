package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.vfs.VirtualFile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        var info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.cpp",
                noErrors, info);

        assertEquals(0, sanity.getErrors().size());

        // Check with missing PCH
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.cpp",
                missingPCH, info);

        assertEquals(1, sanity.getErrors().size());
        assertEquals(Errors.MissingPCH, sanity.getErrors().get(0).message);

        // Check PCH not first
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.cpp",
                pchNotFirst, info);

        assertEquals(1, sanity.getErrors().size());
        assertEquals(Errors.PCHNotFirst, sanity.getErrors().get(0).message);

        // pch.h should not be included in headers
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Aquarium/AquariumLib/Aquarium.h",
                noErrors, info);

        assertEquals(3, sanity.getErrors().size());
        assertEquals(Errors.PCHIncludedInHeader, sanity.getErrors().get(0).message);

        // valid header file
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Triangles/Vertex.h",
                goodHeader, info);

        assertEquals(0, sanity.getErrors().size());

        // extra qualifier
        sanity = new SanityCheckMock();
        sanity.setHasPch(true);
        info = new SanityCheck.FileInfo();
        sanity.checkFileContent(null, "/project/Triangles/Vertex.h",
                qualifierError, info);

        assertEquals(1, sanity.getErrors().size());
        assertEquals(Errors.ExtraQualifier, sanity.getErrors().get(0).message);
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


    private static String pchNotFirst = "/**\n" +
            " * @file Aquarium.cpp\n" +
            " * @author Student\n" +
            " */\n" +
            "\n" +
            "#include <wx/dcbuffer.h>\n" +
            "#include \"pch.h\"\n" +
            "#include \"Aquarium.h\"\n" +
            "\n" +
            "using namespace std;\n";

    private static String goodHeader = "/**\n" +
            " * @file Vertex.h\n" +
            " *\n" +
            " * @author Charles B. Owen\n" +
            " *\n" +
            " * \n" +
            " */\n" +
            "\n" +
            "#pragma once\n" +
            "\n" +
            "class TriangleMesh;\n" +
            "\n" +
            "class Vertex\n" +
            "{\n" +
            "public:\n" +
            "    Vertex(int x, int y) { mX = x; mY = y; }\n" +
            "\n" +
            "    void SetMesh(TriangleMesh* mesh) { mMesh = mesh; }\n" +
            "\n" +
            "    int GetX() { return mX; }\n" +
            "\n" +
            "    int GetY()\n" +
            "\n" +
            "private:\n" +
            "    int mX;\n" +
            "    int mY;\n" +
            "\n" +
            "    /// The owning mesh\n" +
            "    TriangleMesh* mMesh = nullptr;\n" +
            "};" +
            "\n" +
            "// Extra qualifier is valid here\n" +
            "inline Vertex::GetY() {return mY; }\n";

    private static String qualifierError = "/**\n" +
            " * @file Vertex.h\n" +
            " *\n" +
            " * @author Charles B. Owen\n" +
            " *\n" +
            " * \n" +
            " */\n" +
            "\n" +
            "#pragma once\n" +
            "\n" +
            "class TriangleMesh;\n" +
            "\n" +
            "class Vertex\n" +
            "{\n" +
            "public:\n" +
            "    // Invalid extra qualifier\n" +
            "    Vertex::Vertex(int x, int y) { mX = x; mY = y; }\n" +
            "\n" +
            "    void SetMesh(TriangleMesh* mesh) { mMesh = mesh; }\n" +
            "\n" +
            "    int GetX() { return mX; }\n" +
            "\n" +
            "    int GetY()\n" +
            "\n" +
            "private:\n" +
            "    int mX;\n" +
            "    int mY;\n" +
            "\n" +
            "    /// The owning mesh\n" +
            "    TriangleMesh* mMesh = nullptr;\n" +
            "};";
}
