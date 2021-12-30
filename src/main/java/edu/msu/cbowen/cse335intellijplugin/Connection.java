/*
 * @file
 * Connection class for communications with server
*/
package edu.msu.cbowen.cse335intellijplugin;

import com.intellij.openapi.application.ApplicationManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 * Connection class for communications with server
 * @author charl
 */
public class Connection {

    /// The connection status
    public enum States {
        /// Not connected (logged out)
        DISCONNECTED,

        /// Connected to the site, but not a course
        SITE,

        /// Connected to a course
        CONNECTED
    }

    //
    // Paths to the various scripts
    //
    private static final String LOGIN_PATH = "/cl/api/users/login";
    private static final String SECTION_SELECT_PATH = "/cl/api/course/members/sectionselect";
    public static final String SUBMIT_PATH = "/cl/api/course/submission/submit/";
    public static final String TEAM_SUBMIT_PATH = "/cl/api/team/submission/submit/";
    private static final String POLL_PATH = "/cl/api/poll";
    private static final String IDE_PATH = "/software/clion/ide";

    /// Error messages
    private String error = null;

    /// The current site state
    private States state = States.DISCONNECTED;

    // The sections array
    private APIValue sections = null;

    /**
     * Constructor
     */
    public Connection() {
        // Initialize the cookies system
        java.net.CookieManager cm = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(cm);
    }

    /**
     * Access the instance of Connection as a service.
     * @return Pointer to the connection service object
     */
    public static Connection getInstance() {
        return ApplicationManager.getApplication().getService(Connection.class);
    }

    /**
     * Get the current connection state
     * @return States enum value.
     */
    public States getState() {
        return state;
    }

    /**
     * Get the sections if we need to select among them.
     * @return The sections
     */
    public APIValue getSections() {
        return sections;
    }


    /**
     * Get the server URL from settings.
     * @return Server URL.
     */
    public String getServer() {
        AppSettingsState appSettingsState = AppSettingsState.getInstance();
        return appSettingsState.server;
    }

    /**
     * Get any error string that has been generated.
     * @return Any error string
     */
    public String getError() {
        return error;
    }
    
    /**
     * Attempt to log into the remote system
     * @param userId User ID to log on using
     * @param password Password to use for log on
     * @return true if successful
     */
    public boolean login(String userId, String password) {
        error = null;
        setState(States.DISCONNECTED);

        try {
            if(login_try(userId, password)) {
                setState(States.SITE);
                return true;
            }
        } catch(UnknownHostException ex) {
            error = "Unable to communicate with course server " + ex.getLocalizedMessage();
            return false;
        } catch(IOException ex) {
            error = "I/O exception: " + ex.getLocalizedMessage();
            return false;
        }

        return false;
    }
    
    private boolean login_try(String userId, String password) throws IOException {
        ArrayList<PostData> postData = new ArrayList<>();
        postData.add(new PostData("id", userId));
        postData.add(new PostData("password", password));
        postData.add(new PostData("keep", "false"));

        String result = connect(LOGIN_PATH, postData);
        if(result == null) {
            throw new IOException("Unable to communicate with course server - connect returned null");
        }
         
        APIResponse response = new APIResponse(result);
        if(response.hasError()) {
            // Logon failed
            error = response.getErrorTitle();
            return false;
        }
         
        return true;
    }

    /**
     * Disconnect from the server
     */
    public void logout() {
        setState(States.DISCONNECTED);
    }

    /**
     * Determine the section.
     *
     * If we disconnect for some reason, state is set to States.DISCONNECTED
     *
     * If the user is only in a single section, the connection state
     * is set to States.CONNECTED, and sections is set to null.
     *
     * If the user is more than one section, the connection state
     * is left as States.SITE and sections is set to the sections
     * from the server.
     *
     * @return the connection state
     */
    public States determineSection() {
        sections = null;
        ArrayList<PostData> postData = new ArrayList<>();
        postData.add(new PostData("auto", "true"));

        String json;

        try {
            json = connect(SECTION_SELECT_PATH, postData);
        } catch (UnknownHostException ex) {
            error = "Unable to communicate with course server " + ex.getLocalizedMessage();
            return setState(States.DISCONNECTED);
        } catch (IOException ex) {
            System.err.println("I/O exception: " + ex.getLocalizedMessage());
            return setState(States.DISCONNECTED);
        }

        if (json == null) {
            error = "I/O exception: Unable to communicate with course server - connect returned null";
            return setState(States.DISCONNECTED);
        }

        APIResponse response = new APIResponse(json);
        if (response.hasError()) {
            // Failed
            error = response.getErrorTitle();
            return setState(States.DISCONNECTED);
        }

        APIValue courseSections = response.getData("course-sections");
        if (courseSections != null) {
            // The user will have to choose the section
            sections = courseSections.get("attributes");
        } else {
            // If there is no section choice, we are done.
            setState(States.CONNECTED);
        }

        return getState();
    }

    /**
     * Select the section.
     * @param section Section APIValue from the server.
     * @return State.
     */
    public States selectSection(APIValue section) {
        ArrayList<PostData> postData = new ArrayList<>();
        postData.add(new PostData("semester", section.getAsString("semester")));
        postData.add(new PostData("section", section.getAsString("section")));

        String json;

        try {
            json = connect(SECTION_SELECT_PATH, postData);
        } catch(UnknownHostException ex) {
            error = "Unable to communicate with course server " + ex.getLocalizedMessage();
            return setState(States.DISCONNECTED);
        } catch(IOException ex) {
            error = "I/O exception: " + ex.getLocalizedMessage();
            return setState(States.DISCONNECTED);
        }

        if(json == null) {
            error = "I/O exception: Unable to communicate with course server - connect returned null";
            return setState(States.DISCONNECTED);
        }

        return setState(States.CONNECTED);
    }

    public static class IDE {
        public IDE(APIValue data) {
            version = data.getAsString("version");
        }

        private String version;

        String getVersion() {return version;}
    }

    public IDE queryIDE() {
        String response;

        String https_url = getServer() + IDE_PATH;
        URL url;
        try {
            url = new URL(https_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            int responseCode=conn.getResponseCode();

            response = "";
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                try(BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
            } else {
                throw new IOException("Invalid server response code " + responseCode);
            }
        } catch (MalformedURLException e) {
            return null;
        } catch(UnknownHostException ex) {
            error = "Unable to communicate with course server " + ex.getLocalizedMessage();
            return null;
        } catch(IOException ex) {
            error = "I/O exception: " + ex.getLocalizedMessage();
            return null;
        }

        APIResponse json = new APIResponse(response);
        if (json.hasError()) {
            // Failed
            error = json.getErrorTitle();
            return null;
        }

        var data = json.getData("ide");
        return new IDE(data.get("attributes"));
    }

    /**
     * Nested PostData class
     */
    public static class PostData {
        public PostData(String key, String value) {
            this.key = key;
            this.value = value;
        }
        
        public String key;
        public String value;
    }

    /**
     * Thread the runs to keep the connection to the server alive.
     * Polls the server once per second.
     */
    private class KeepAwakeThread implements Runnable {

        private boolean running = true;

        ArrayList<PostData> postData = new ArrayList<>();

        public synchronized void stop() {
            running = false;
        }

        private synchronized boolean isRunning() {
            return running;
        }

        @Override
        public void run() {
            running = true;

            while(true) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    return;
                }

                if (!isRunning()) {
                    break;
                }

                // System.out.println("Keep awake");

                String json;

                try {
                    json = connect(POLL_PATH, postData);
                } catch(UnknownHostException ex) {
                    error = "Unable to communicate with course server " + ex.getLocalizedMessage();
                    setState(States.DISCONNECTED);
                    return;
                } catch(IOException ex) {
                    error = "I/O exception: " + ex.getLocalizedMessage();
                    setState(States.DISCONNECTED);
                    return;
                }

                if(json == null) {
                    error = "I/O exception: Unable to communicate with course server - connect returned null";
                    setState(States.DISCONNECTED);
                    return;
                }
            }
        }
    }

    private KeepAwakeThread keepAwakeThread = null;


    private synchronized States setState(States state) {
        if(state == States.CONNECTED && this.state != States.CONNECTED) {
            // We just became connected
            keepAwakeThread = new KeepAwakeThread();
            Thread thread = new Thread(keepAwakeThread);
            thread.start();
        }

        if(state != States.CONNECTED && this.state == States.CONNECTED) {
            // Just became disconnected
            keepAwakeThread.stop();
            keepAwakeThread = null;
        }

        this.state = state;
        return this.state;
    }

    /**
     * Connect to the server (POST)
     * @param path Path no including the SERVER URL
     * @param postData Post data to add to the POST message
     * @return Server response
     * @throws IOException On input/output errors
     */
    public synchronized String connect(String path, List<PostData> postData) throws IOException {
        String response;
                   
        String https_url = getServer() + path;
        URL url;
        try {
            url = new URL(https_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            sendPost(conn, postData);
    
            int responseCode=conn.getResponseCode();
                       
            response = "";
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                try(BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
            } else {
                throw new IOException("Invalid server response code " + responseCode);
            }
        } catch (MalformedURLException e) {
            return null;
        }

        return response;
    }
    
    public String sendFile(String assign, String tag, File file, String type) throws IOException {
        String https_url = getServer() + SUBMIT_PATH + assign + "/" + tag;

        MultipartUtility multipart = new MultipartUtility(https_url, "UTF-8");
        multipart.addFormField("assign", assign);
        multipart.addFormField("tag", tag);
        multipart.addFormField("type", type);
        multipart.addFilePart("file", file);
        String response = multipart.finish(); // response from server.
        
        return response;
    }

    
    private void sendPost(HttpURLConnection conn, List<PostData> postData) throws UnsupportedEncodingException, IOException {
        /*
         * Build post arguments list
        */
        StringBuilder post = new StringBuilder();
        boolean first = true;
        for(PostData data : postData) {
            if(first) {
                first = false;
            } else {
                post.append("&");
            }

            post.append(URLEncoder.encode(data.key, "UTF-8"));
            post.append("=");
            post.append(URLEncoder.encode(data.value, "UTF-8"));
        }

        /*
         * And send it
        */
        try(OutputStream os = conn.getOutputStream()) {
            try(BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"))) {

                writer.write(post.toString());
                writer.flush();
            }
        }
    }
}
