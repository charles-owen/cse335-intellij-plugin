package edu.msu.cbowen.cse335intellijplugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Request all open submissions from the course server.
 * Ask the user which to submit to.
 * @author Charles Owen
 */
public class OpenSubmissions {
    public static final String PATH = "/cl/api/course/open";
    
    private final Connection connection;

    private String error = null;

    public String getError() {
        return error;
    }
    
    public OpenSubmissions(Connection connection) {
        this.connection = connection;
    }
    
    public APIValue getOpen() {
        error = null;

        ArrayList<Connection.PostData> postData = new ArrayList<>();
        postData.add(new Connection.PostData("type", "program"));
        
        try {
            String json = connection.connect(PATH, postData);
            if(json == null) {
                error = "I/O exception: Unable to communicate with course server - connect returned null";
                return null;
            }

            APIResponse response = new APIResponse(json);
            if(response.hasError()) {
                // Failed
                error = response.getErrorTitle();
                return null;
            }

            APIValue submissions = response.getData("open-submissions");
            return submissions.get("attributes");
            
        } catch (IOException ex) {
            error = "Error communicating with course server";
            return null;
        }
    }
    
    
//    private APIValue choose(APIResponse response) throws IOException {
//        APIValue data = response.getData("open-submissions");
//        APIValue submissions = data.get("attributes");
//
//        System.out.println("Choose the submission you wish to make:");
//
//        TreeMap<Character, APIValue> map = new TreeMap<>();
//
//        if(submissions.size() == 0) {
//            System.out.println("There are no submissions open at this time");
//            return null;
//        }
//
//        for(int i=0; i<submissions.size(); i++) {
//            char key = (char)((int)'A' + i);
//
//            APIValue submission = submissions.get(i);
//            map.put(key, submission);
//
//            String assignName = submission.getAsString("assignName");
//            String submitName = submission.getAsString("submitName");
//
//            System.out.println(Character.toString(key) + ": " + assignName + ": " + submitName);
//        }
//
//        System.out.println("Z: Quit without submitting.");
//
//        while(true) {
//            String resp = console.readLine("Submission to make: ").toUpperCase();
//            if(resp.equals("Z")) {
//                return null;
//            }
//
//            APIValue submit = map.get(resp.charAt(0));
//            if(submit != null) {
//                return submit;
//            } else {
//                System.out.println("Do what?");
//            }
//        }
//    }

}
