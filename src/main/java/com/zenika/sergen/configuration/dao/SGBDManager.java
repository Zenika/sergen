package com.zenika.sergen.configuration.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;

/**
 * Created by Zenika on 27/05/2015.
 */
public class SGBDManager {


    public DBCollection getCollection(String SGServer, int SGServerPort, String BDName, String CollectionName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new ServerAddress(SGServer, SGServerPort));
        DB database = mongoClient.getDB(BDName);
        DBCollection collection = database.getCollection(CollectionName);
        return collection;
    }

    public Object ConfigurationInitialisation(String SGBDName) {


        if (SGBDName.equals("PostGresSQL")) {
            //TODO
        } else if (SGBDName.equals("MySQL")) {
            //TODO
        } else if (SGBDName.equals("Oracle")) {
            //TODO
        } else if (SGBDName.equals("MongoDB") || SGBDName.equals("Default")) {
            try {
                return getCollection("localhost", 27017, "ConfigurationDB", "resources");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
     return null;

    }

}