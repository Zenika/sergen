package com.zenika.sergen.jsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Zenika on 11/05/2015.
 */
public class TransformJsonToSG_Config {
    public static SG_ConfigClass getSGConfigClass(String path){
        //read json file data to String
        byte[] jsonData = new byte[0];
        try {
            jsonData = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SG_ConfigClass sg_configClass = null;
        try {
            sg_configClass = objectMapper.readValue(jsonData, SG_ConfigClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  sg_configClass;
    }
}
