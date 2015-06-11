package component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.*;
import com.mongodb.util.JSON;
import pojo.SGComponent;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Zenika on 10/06/2015.
 */
public class SGComponentManagerMongoDB implements SGComponentManager {
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
    public ArrayList<SGComponent> load(String name) {

        ArrayList<SGComponent> resultToArray = null;

        BasicDBObject whereQuery = new BasicDBObject();


        whereQuery.put("componentName", name);


        ObjectMapper objectMapper = new ObjectMapper();
        SGComponent component = null;
        DBCursor result;
        result = collection.find(whereQuery);
        while (result.hasNext()) {
            try {
                component = objectMapper.readValue(result.next().toString(), SGComponent.class);
            } catch (IOException e) {
                e.printStackTrace();
            }


            resultToArray.add(component);
        }
        return resultToArray;
    }

    @Override
    public ArrayList<SGComponent> loadAll() {
        ArrayList<SGComponent> components = new ArrayList<>();
        DBCursor dbcursor;
        dbcursor = collection.find();
        while (dbcursor.hasNext()) {
            components.add((SGComponent) dbcursor.next());
        }

        return components;
    }

    @Override
    public void save(ArrayList<SGComponent> component) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String json = null;
        try {
            json = ow.writeValueAsString(component);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //Transform Json to DBObject
        DBObject componentJSON = (DBObject) JSON.parse(json);
        collection.insert(componentJSON);
    }


    public void delete(String name) {
        BasicDBObject whereQuery = new BasicDBObject();

        whereQuery.put("componentName", name);
        collection.findAndRemove(whereQuery);
    }
}
