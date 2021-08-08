package edu.msu.cbowen.cse335intellijplugin;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Works like System.console but also works in debug windows 
 * where no console is present.
 */
public class MyConsole {
    private final Console console;
    
    public MyConsole(Console console) {
        this.console = console;
    }
    
    public String readLine(String format, Object... args) throws IOException {
        if (console!= null) {
            return console.readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        return reader.readLine();
    }

    public String readPassword(String format, Object... args)
            throws IOException {
        if (console != null)
        {
            return new String(console.readPassword(format, args));
        }
        
        return this.readLine(format, args);
    }
}
