package com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.zenika.sergen.configuration.SGConfigurationDAO;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.pojo.SGResourceConfiguration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SGConfigurationDAOMongoDB implements SGConfigurationDAO {

    private DBCollection collection;

    public void init(String SGServer, int SGServerPort, String BDName, String CollectionName) {
        try {
            collection = getCollection(SGServer, SGServerPort, BDName, CollectionName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @throws java.net.UnknownHostException
     */

    public DBCollection getCollection(String SGServer, int SGServerPort, String BDName, String CollectionName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new ServerAddress(SGServer, SGServerPort));
        DB database = mongoClient.getDB(BDName);
        return database.getCollection(CollectionName);
    }

    @Override
    /**
     *
     */
    public void save(SGResourceConfiguration configuration) {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String json = null;
        try {
            json = ow.writeValueAsString(configuration);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //Transform Json to DBObject
        DBObject resourceConfig = (DBObject) JSON.parse(json);
        collection.insert(resourceConfig);
    }

    @Override
    /**
     *
     */
    public void delete(String name) {
        BasicDBObject whereQuery = new BasicDBObject();

        whereQuery.put("resourceIdentity", name);
        collection.findAndRemove(whereQuery);
    }

    @Override
    /**
     *
     */
    public SGResourceConfiguration load(String name) throws SGConfigurationNotFound {

        BasicDBObject whereQuery = new BasicDBObject();


        whereQuery.put("resourceName", name);


        DBCursor result;

        result = collection.find(whereQuery);

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SGResourceConfiguration sg_config = null;
        while (result.hasNext())
            try {


                sg_config = objectMapper.readValue(result.next().toString(), SGResourceConfiguration.class);
                //readValue(jsonData, SG_Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return sg_config;


    }

    @Override
    /**
     *
     */
    public ArrayList<SGResourceConfiguration> loadAll() {
        ArrayList<SGResourceConfiguration> configurations = new ArrayList<>();
        DBCursor dbcursor;
        dbcursor = collection.find();
        while (dbcursor.hasNext()) {
            configurations.add((SGResourceConfiguration) dbcursor.next());
        }

        return configurations;
    }

    @Override
    /**
     *
     */
    public ArrayList<String> loadAllNames() {
        ArrayList<String> allConfigurationNames = new ArrayList<>();

        DBCursor dbcursor;
        dbcursor = collection.find();
        SGResourceConfiguration sgResourceConfiguration;
        while (dbcursor.hasNext()) {
            sgResourceConfiguration = (SGResourceConfiguration) dbcursor.next();
            allConfigurationNames.add(sgResourceConfiguration.getName());
        }
        return allConfigurationNames;
    }
}
