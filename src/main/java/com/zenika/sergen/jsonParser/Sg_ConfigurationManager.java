package com.zenika.sergen.jsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.mongodb.SG_MongoDB_Manager;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Zenika on 11/05/2015.
 */
public class SG_ConfigurationManager {
    public SG_Configuration Configuration() throws SGConfigurationNotFound {
        //read json file data to String
        // String json = "{  \"resourceName\" : \"com.zenika.sergen.testRessource22\" , " +
        //    "\"resourcePath\" : \"test\" , " +
        //   "\"resourceAttributs\" : { \"classOfAttributs\" : \"com.zenika.sergen.product.ProductService\"} ," +
        //  " \"resourceFonctions\" : { \"returnType\" : \"java.lang.Long\" , " +
        //   "\"fonctionName\" : \"sayHello\" , " +
        //    "\"fonctionBody\" : \"return productservice.getCount();\" ," +
        //  " \"fonctionPath\" : \"/hello\" , \"fonctionProduces\" : \"text/plain\" ," +
        //  " \"fonctionsConsumes\" : \"text/plain\" , \"typeOfRequete\" : \"javax.ws.rs.GET\"}}";

        //  SG_ConfigurationManager.save(json);

        return SG_ConfigurationManager.load("com.zenika.sergen.testRessource");
    }

    public static SG_Configuration load(String configName) throws SGConfigurationNotFound {

        SG_MongoDB_Manager sg_mongoDB_Manager = new SG_MongoDB_Manager();

        BasicDBObject whereQuery = new BasicDBObject();

        whereQuery.put("resourceName", configName);

        DBCollection collection;

        DBCursor result = null;
        try {
            collection = sg_mongoDB_Manager.sg_getCollection();
            result = collection.find(whereQuery);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SG_Configuration sg_config = null;
        while (result.hasNext())
            try {


                sg_config = objectMapper.readValue(result.next().toString(), SG_Configuration.class);
                //readValue(jsonData, SG_Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return sg_config;
    }

    public static void save(String config) {
        //Transform Json to DBObject
        DBObject resourceConfig = (DBObject) JSON.parse(config);
        // Get MoongoDB Config
        SG_MongoDB_Manager sg_mongoDB_Manager = new SG_MongoDB_Manager();
        try {
            // Get Collection from MongoDB
            DBCollection collection = sg_mongoDB_Manager.sg_getCollection();
            //Insert in MongoDB
            collection.insert(resourceConfig);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
