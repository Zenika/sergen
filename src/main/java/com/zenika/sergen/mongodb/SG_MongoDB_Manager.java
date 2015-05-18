package com.zenika.sergen.mongodb;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SG_MongoDB_Manager {
    public  DBCollection sg_getCollection() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
        DB database = mongoClient.getDB("ConfigurationDB");
        DBCollection collection = database.getCollection("resources");
        //DBObject document = collection.findOne();
        return collection;
    }
}
