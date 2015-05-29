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




    public Object ConfigurationInitialisation(String SGBDName) {


        if (SGBDName.equals("PostGresSQL")) {
            //TODO
        } else if (SGBDName.equals("MySQL")) {
            //TODO
        } else if (SGBDName.equals("Oracle")) {
            //TODO
        } else if (SGBDName.equals("MongoDB") || SGBDName.equals("Default")) {

               return new SGConfigurationMongoDB();
               // return getCollection("localhost", 27017, "ConfigurationDB", "resources");

        }
        return null;
    }
}
