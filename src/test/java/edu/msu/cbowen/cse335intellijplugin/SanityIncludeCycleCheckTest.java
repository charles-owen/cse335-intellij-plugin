package edu.msu.cbowen.cse335intellijplugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SanityIncludeCycleCheckTest {



    @Test
    void check() {
        SanityIncludeCycleCheck checker = new SanityIncludeCycleCheck();

        // Test that should be good
        checker.start(null);
        checker.file(null, "/project/Aquarium/AquariumLib/pch.h", toLines(pch));
        checker.file(null, "/project/Aquarium/AquariumLib/Aquarium.h", toLines(aquarium));
        checker.file(null, "/project/Aquarium/AquariumLib/Item.h", toLines(item));
        checker.file(null, "/project/Aquarium/AquariumLib/Fish.h", toLines(fish));
        checker.file(null, "/project/Aquarium/AquariumLib/FishBeta.h", toLines(fishBeta));

        var result = checker.check();
        assertNull(result);

        // Test that should find a cycle
        checker.start(null);
        checker.file(null, "/project/Aquarium/AquariumLib/pch.h", toLines(pch));
        checker.file(null, "/project/Aquarium/AquariumLib/Aquarium.h", toLines(aquarium));
        checker.file(null, "/project/Aquarium/AquariumLib/Item.h", toLines(itemBad));

        result = checker.check();
        assertEquals(3, result.size());
        assertEquals("Aquarium.h", result.get(0));
        assertEquals("Item.h", result.get(1));
        assertEquals("Aquarium.h", result.get(2));

        // Test that should find a cycle
        checker.start(null);
        checker.file(null, "/project/Aquarium/AquariumLib/a.h", toLines(a));
        checker.file(null, "/project/Aquarium/AquariumLib/b.h", toLines(b));
        checker.file(null, "/project/Aquarium/AquariumLib/c.h", toLines(c));
        checker.file(null, "/project/Aquarium/AquariumLib/d.h", toLines(d));
        checker.file(null, "/project/Aquarium/AquariumLib/pch.h", toLines(pch));
        checker.file(null, "/project/Aquarium/AquariumLib/Aquarium.h", toLines(aquarium));
        checker.file(null, "/project/Aquarium/AquariumLib/Item.h", toLines(item));
        checker.file(null, "/project/Aquarium/AquariumLib/Fish.h", toLines(fish));
        checker.file(null, "/project/Aquarium/AquariumLib/FishBeta.h", toLines(fishBeta));

        result = checker.check();
        result = checker.check();
        assertEquals(4, result.size());
        assertEquals("b.h", result.get(0));
        assertEquals("c.h", result.get(1));
        assertEquals("d.h", result.get(2));
        assertEquals("b.h", result.get(3));
    }

    private String[] toLines(String text) {
        return text.split("\\r?\\n");
    }

    private static final String pch = "/**\n" +
            " * @file pch.h\n" +
            " * @author Charles B. Owen\n" +
            " */\n" +
            "\n" +
            "#ifndef AQUARIUMLIB_PCH_H\n" +
            "#define AQUARIUMLIB_PCH_H\n" +
            "\n" +
            "#include <wx/wxprec.h>\n" +
            "#ifndef WX_PRECOMP\n" +
            "#include <wx/wx.h>\n" +
            "#endif\n" +
            "\n" +
            "#include <wx/xml/xml.h>\n" +
            "\n" +
            "#endif //AQUARIUMLIB_PCH_H\n";

    private static final String aquarium = "/**\n" +
            " * @file Aquarium.h\n" +
            " * @author Charles B. Owen\n" +
            " *\n" +
            " * The main aquarium class.\n" +
            " */\n" +
            "\n" +
            "#ifndef AQUARIUM_AQUARIUM_H\n" +
            "#define AQUARIUM_AQUARIUM_H\n" +
            "\n" +
            "#include <memory>\n" +
            "#include <vector>\n" +
            "#include <random>\n" +
            "\n" +
            "#include \"Item.h\"\n" +
            "";

    private static final String item = "/**\n" +
            " * @file Item.h\n" +
            " * @author Charles B. Owen\n" +
            " *\n" +
            " * Base class for items in the aquarium\n" +
            " */\n" +
            "\n" +
            "#ifndef AQUARIUM_ITEM_H\n" +
            "#define AQUARIUM_ITEM_H\n" +
            "\n" +
            "class Aquarium;\n";

    private static final String itemBad = "/**\n" +
            " * @file Item.h\n" +
            " * @author Charles B. Owen\n" +
            " *\n" +
            " * Base class for items in the aquarium\n" +
            " */\n" +
            "\n" +
            "#ifndef AQUARIUM_ITEM_H\n" +
            "#define AQUARIUM_ITEM_H\n" +
            "\n" +
            "#include \"Aquarium.h\"\n";

    private static final String fish = "\n" +
            "#include \"Item.h\"\n" +
            "\n";

    private static final String fishBeta = "\n" +
            "#include <memory>\n" +
            "#include \"Fish.h\"\n" +
            "\n";


    private static final String fishBetaBad = "\n" +
            "#include <memory>\n" +
            "#include \"Fish.h\"\n" +
            "#include \"Aquarium.h\"\n";

    private static final String a = "\n" +
            "#include <memory>\n" +
            "#include \"b.h\"\n" +
            "#include \"Aquarium.h\"\n";

    private static final String b = "\n" +
            "#include <memory>\n" +
            "#include \"c.h\"\n" +
            "#include \"Item.h\"\n";

    private static final String c = "\n" +
            "#include <memory>\n" +
            "#include \"d.h\"\n" +
            "#include \"Item.h\"\n";

    private static final String d = "\n" +
            "#include <memory>\n" +
            "#include \"b.h\"\n" +
            "#include \"Item.h\"\n";


}