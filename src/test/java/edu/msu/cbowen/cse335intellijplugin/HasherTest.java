package edu.msu.cbowen.cse335intellijplugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HasherTest {

    @Test
    void hashTest() {
        var hash = Hasher.hash("student-1.0.3");
        assertEquals(597624669, hash);

        hash = Hasher.hash("student-1.0.1");
        assertEquals(1452291392, hash);

        hash = Hasher.hash("cbowen-1.0.3");
        assertEquals(674232713, hash);
    }
}
