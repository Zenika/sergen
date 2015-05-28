package com.zenika.sergen.configuration.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.configuration.pojo.SGResource;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Zenika on 11/05/2015.
 */
public class SGMongoManager {
    public SGResource Configuration() throws SGConfigurationNotFound {
        //read json file data to String
        String json = "{ \"resourceIdentity \" : \"testId \" " +
                " \"name\" : \"com.zenika.sergen.testRessource22\" , " +
                "\"path\" : \"test\" , " +
                " \"methodes\" : { \"returnType\" : \"java.lang.Long\" , " +
                "\"name\" : \"sayHello\" , " +
                " \"path\" : \"/hello\" ," +
                " \"Produces\" : \"text/plain\" ," +
                " \"Consumes\" : \"text/plain\" , " +
                "\"typeOfRequete\" : \"javax.ws.rs.GET\"," +
                "\"parameters\" : [{\"name\" : \"id\" , \"type\" : \"String\" } ]," +
                "\"workflows\" :" +
                " [{\"name\", \"methode\" , " + " \"parameters\" : [\"param1\", \"param2\"]}," +
                " {\"name\", \"methode\" , \"params\" : [\"param1\", \"param2\"]}]\"}}";

        // SGMongoManager.save(json);

        //return SGMongoManager.load("com.zenika.sergen.testRessource");
        return null;
    }

    public SGResource load(String configName) throws SGConfigurationNotFound {

        SGConfigurationMongoDB sg_mongoDB_Manager = new SGConfigurationMongoDB();

        BasicDBObject whereQuery = new BasicDBObject();


        whereQuery.put("resourceName", configName);

        DBCollection collection;

        DBCursor result = null;
        try {
            collection = sg_mongoDB_Manager.getCollection();
            result = collection.find(whereQuery);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SGResource sg_config = null;
        while (result.hasNext())
            try {


                sg_config = objectMapper.readValue(result.next().toString(), SGResource.class);
                //readValue(jsonData, SG_Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return sg_config;
    }

    public void save(String config) {
        //Transform Json to DBObject
        DBObject resourceConfig = (DBObject) JSON.parse(config);
        // Get MoongoDB Config
        SGConfigurationMongoDB sg_mongoDB_Manager = new SGConfigurationMongoDB();
        try {
            // Get Collection from MongoDB
            DBCollection collection = sg_mongoDB_Manager.getCollection();
            //Insert in MongoDB
            collection.insert(resourceConfig);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void remove(String configName) {


        // Get MongoDB Config
        SGConfigurationMongoDB mongoDB = new SGConfigurationMongoDB();

        BasicDBObject whereQuery = new BasicDBObject();

        whereQuery.put("resourceIdentity", configName);

        try {
            DBCollection configCollection = mongoDB.getCollection();
            //Remove from MongoDB
            configCollection.findAndRemove(whereQuery);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
