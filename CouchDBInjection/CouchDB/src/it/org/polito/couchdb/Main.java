package it.org.polito.couchdb;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CouchDBInjection db = new CouchDBInjection("http://192.168.1.161:3000", "1.6.1");
        switch (db.getVersion()) {
            case "1.6.1":
                // do something
                db.injection161();
                break;
            case "3.1.1":
                // do something
                db.injection311passwordBypass();
                break;
            default:
                System.out.println("Version of CouchDB not supported, try with 1.6.1 or 3.1.1\n");
        }
    }
}