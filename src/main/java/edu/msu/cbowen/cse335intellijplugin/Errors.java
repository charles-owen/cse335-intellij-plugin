package edu.msu.cbowen.cse335intellijplugin;

/**
 * All of the possible error messages
 */
public class Errors {
    public static String UsingNamespace = "SC001 Using namespace in a .h file is not allowed";
    public static String MissingAuthor = "SC002 Missing @author Doxygen directive or author name";
    public static String MissingFile = "SC003 Missing @file Doxygen directive or file name";
    public static String MissingPCHTest = "SC004 Missing #include <pch.h>";
    public static String MissingPCH = "SC005 Missing #include \"pch.h\"";
    public static String QuotedIncludesTest = "SC006 Included test headers should not be quoted";
    public static String PCHNotFirst = "SC007 pch.h must be included first.";
    public static String PCHIncludedInHeader = "SC008 pch.h must not be included in header files.";
    public static String ExtraQualifier = "SC009 Extraneous class qualifier in class declaration.";
}
