package edu.msu.cbowen.cse335intellijplugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class DirectoryToZipTest {

    @Test
    void zip() throws Exception {
        //
        // Get an example source directory
        //
        String path = "src/test/resources/example-clion";
        File file = new File(path);
        String examplePath = file.getAbsolutePath();

       // System.out.println(examplePath);

        //
        // Create a temporary output file
        //
        file = File.createTempFile("temp", ".zip");
        String resultPath = file.getAbsolutePath();
      //  System.out.println(resultPath);

        OutputStream stream = Files.newOutputStream(file.toPath());

        DirectoryToZip d2z = new DirectoryToZip();
        d2z.excludeStandard();
        d2z.zip(examplePath, stream);

        Assertions.assertEquals(1, 1);
    }

    @Test
    void toExclude() {
        DirectoryToZip d2z = new DirectoryToZip();

//        d2z.exclude("[\\\\^/]\\.");
//        d2z.exclude("[\\\\/](html|latex|rtf)[\\\\/]");
//        d2z.exclude("[\\\\/]cmake-");
//        d2z.exclude("(\\.zip$)|(\\.zip[\\\\/])");
//        d2z.exclude("\\.(o|O)$");

        d2z.excludeStandard();

        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion/.idea\\.gitignore"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\.gitignore"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\Aquarium.iml"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\Aquarium.cpp"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\html\\something.css"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\html1\\something.css"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\misc.xml"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\modules.xml"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\vcs.xml"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\.idea\\workspace.xml"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\Aquarium.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\AquariumApp.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\AquariumApp.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\AquariumView.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\AquariumView.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\FishAngel.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\FishAngel.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\ids.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\AquariumLib\\pch.h"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\CMakeCache.txt"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\cmake_install.cmake"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\Makefile"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\_deps\\googletest-src\\.clang-format"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\_deps\\googletest-src\\ci\\linux-presubmit.sh"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\cmake-build-debug\\_deps\\googletest-src\\ci\\macos-presubmit.sh"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Doxyfile.doxy"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\images\\angelfish.png"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\images\\background1.png"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\images\\beta.png"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\images\\carp.png"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\images\\clown-fish.png"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\main.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\pch.h"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\AquariumTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\EmptyTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\FishBetaTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\gtest_main.cpp"));
        Assertions.assertFalse(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\ItemTest.cpp"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\ItemTest.zip"));
        Assertions.assertTrue(d2z.toExclude("D:\\courses\\cse335\\cse335-intellij-plugin\\src\\test\\resources\\example-clion\\Tests\\ItemTest.zip\\Something"));

        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/.gitignore"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/.gitignore"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/Aquarium.iml"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/Aquarium.cpp"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/html/something.css"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/html1/something.css"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/misc.xml"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/modules.xml"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/vcs.xml"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/.idea/workspace.xml"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/Aquarium.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/AquariumApp.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/AquariumApp.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/AquariumView.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/AquariumView.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/FishAngel.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/FishAngel.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/ids.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/AquariumLib/pch.h"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/CMakeCache.txt"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/cmake_install.cmake"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/Makefile"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/_deps/googletest-src/.clang-format"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/_deps/googletest-src/ci/linux-presubmit.sh"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/cmake-build-debug/_deps/googletest-src/ci/macos-presubmit.sh"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Doxyfile.doxy"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/images/angelfish.png"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/images/background1.png"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/images/beta.png"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/images/carp.png"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/images/clown-fish.png"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/main.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/pch.h"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/AquariumTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/CMakeLists.txt"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/EmptyTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/FishBetaTest.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/gtest_main.cpp"));
        Assertions.assertFalse(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/ItemTest.cpp"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/ItemTest.zip"));
        Assertions.assertTrue(d2z.toExclude("/courses/cse335/cse335-intellij-plugin/src/test/resources/example-clion/Tests/ItemTest.zip/Something"));

    }
}