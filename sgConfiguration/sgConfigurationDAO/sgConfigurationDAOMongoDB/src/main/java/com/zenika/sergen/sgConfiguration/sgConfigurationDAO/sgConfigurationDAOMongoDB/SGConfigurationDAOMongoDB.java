package com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.zenika.sergen.configuration.SGConfigurationDAO;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 13/05/2015.
 */
@Component
@Data
public class SGConfigurationDAOMongoDB implements SGConfigurationDAO {

    ObjectMapper objectMapper;

    SGResourceConfiguration sgResourceConfiguration = null;
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
        collection.save(resourceConfig);
    }

    @Override
    /**
     *
     */
    public void delete(String name) {
        BasicDBObject whereQuery = new BasicDBObject();

        whereQuery.put("name", name);

        collection.remove(whereQuery);

    }

    @Override
    /**
     *
     */
    public List<SGResourceConfiguration> load(String name) throws SGConfigurationNotFound {

        BasicDBObject whereQuery = new BasicDBObject();


        whereQuery.put("name", name);


        DBCursor result;

        result = collection.find(whereQuery);


        SGResourceConfiguration sgResourceConfiguration = null;
        List<SGResourceConfiguration> configurations = new ArrayList<>();
        while (result.hasNext()) {


            sgResourceConfiguration = new Gson().fromJson(result.next().toString(), SGResourceConfiguration.class);
            configurations.add(sgResourceConfiguration);
        }
      return configurations;
   }



    @Override
    /**
     *
     */
    public List<SGResourceConfiguration> loadAll() {

        List<SGResourceConfiguration> configurations = new ArrayList<>();

       // Type collectionType = new TypeToken<List<SGResourceConfiguration>>(){}.getType();

        DBCursor dbcursor;
        dbcursor = collection.find();
       // List<SGResourceConfiguration> ConfigurationList = new Gson().fromJson(String.valueOf(dbcursor),collectionType);

        while(dbcursor.hasNext()) {

            SGResourceConfiguration sgResourceConfiguration1 = new Gson().fromJson(dbcursor.next().toString(),SGResourceConfiguration.class);
        //  System.out.println(dbcursor.next().toString());
            configurations.add(sgResourceConfiguration1);
        }
        return configurations;
    }

    @Override
    /**
     *
     */
    public List<String> loadAllNames() {
        ArrayList<String> allConfigurationNames = new ArrayList<>();

        DBCursor dbcursor;
        dbcursor = collection.find();
        System.out.println(dbcursor.toString());
        SGResourceConfiguration sgResourceConfiguration;
        while (dbcursor.hasNext()) {
            sgResourceConfiguration = (SGResourceConfiguration) dbcursor.next();
            allConfigurationNames.add(sgResourceConfiguration.getName());
        }
        return allConfigurationNames;
    }


}
