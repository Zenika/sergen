package com.zenika.sergen.classgenerator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Zenika on 27/03/2015.
 */
public class TransformToMap {


    public static Map<String, Object> parse(String json) {

        JsonObject object = (JsonObject) new com.google.gson.JsonParser().parse(json);
        Set<Map.Entry<String, JsonElement>> set = object.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (!value.isJsonPrimitive()) {
                map.put(key, value.getClass());
            } else {
                map.put(key, value.getAsString());
            }
        }
        return map;
    }
}