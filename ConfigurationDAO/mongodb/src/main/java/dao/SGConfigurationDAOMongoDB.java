package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.*;
import com.mongodb.util.JSON;
import com.zenika.sergen.configuration.SGConfigurationDAO;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.pojo.SGConfiguration;

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
        DBCollection collection = database.getCollection(CollectionName);
        return collection;
    }

    @Override
    /**
     *
     */
    public void save(SGConfiguration configuration) {

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
    public SGConfiguration load(String name) throws SGConfigurationNotFound {

        BasicDBObject whereQuery = new BasicDBObject();


        whereQuery.put("resourceName", name);


        DBCursor result;

        result = collection.find(whereQuery);

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SGConfiguration sg_config = null;
        while (result.hasNext())
            try {


                sg_config = objectMapper.readValue(result.next().toString(), SGConfiguration.class);
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
    public ArrayList<SGConfiguration> loadAll() {
        ArrayList<SGConfiguration> configurations = new ArrayList<>();
        DBCursor dbcursor;
        dbcursor = collection.find();
        while (dbcursor.hasNext()) {
            configurations.add((SGConfiguration) dbcursor.next());
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
        SGConfiguration sgConfiguration;
        while (dbcursor.hasNext()) {
            sgConfiguration = (SGConfiguration) dbcursor.next();
            allConfigurationNames.add(sgConfiguration.getName());
        }
        return allConfigurationNames;
    }
}
