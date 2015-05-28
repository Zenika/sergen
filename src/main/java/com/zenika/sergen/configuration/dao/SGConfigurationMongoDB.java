package com.zenika.sergen.configuration.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.zenika.sergen.configuration.pojo.SGResource;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SGConfigurationMongoDB implements SGConfigurationDAO {

    private SGMongoManager sgMongoManager;

    /**
     * @return
     * @throws UnknownHostException
     */

    SGBDManager sgbdManager = new SGBDManager();

    DBCollection collection = (DBCollection) sgbdManager.ConfigurationInitialisation("MongoDB");

    @Override
    /**
     *
     */
    public void save(SGResource configuration) {


        sgMongoManager = new SGMongoManager();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        String json = null;
        try {
            json = ow.writeValueAsString(configuration);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        sgMongoManager.save(json);
    }

    @Override
    /**
     *
     */
    public void delete(String name) {
        sgMongoManager.remove(name);
    }

    @Override
    /**
     *
     */
    public SGResource load(String name) {
        SGResource sgConfiguration = new SGResource();
        try {
            sgConfiguration = sgMongoManager.load(name);
        } catch (SGConfigurationNotFound sgConfigurationNotFound) {
            sgConfigurationNotFound.printStackTrace();
        }
        return sgConfiguration;
    }

    @Override
    /**
     *
     */
    public ArrayList<SGResource> loadAll() {
        ArrayList<SGResource> configurations = new ArrayList<>();
        DBCursor dbcursor;
        dbcursor = collection.find();
        while (dbcursor.hasNext()) {
            configurations.add((SGResource) dbcursor.next());
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
        SGResource sgConfiguration = new SGResource();
        while (dbcursor.hasNext()) {
            sgConfiguration = (SGResource) dbcursor.next();
            allConfigurationNames.add(sgConfiguration.getName());
        }
        return allConfigurationNames;
    }
}
