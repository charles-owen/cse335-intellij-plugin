package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;

/**
 * Convert a user ID and version number for the plugin into a hash.
 */
public class Hasher {

    private static final long m = 2147483647;
    private static final long p = 127;

    public static long hash(String str) {
        long h = 0;
        long pProduct = 1;

        for(int i=0; i<str.length(); i++) {
            var c = (int)str.charAt(i);
            h = (h + c * pProduct) % m;
            pProduct = (pProduct * p) % m;
        }

        return h;
    }
}
