package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by Zenika on 22/06/2015.
 */
public enum SGComponentManager {
    INSTANCE;

    /**
     * Key : name of the JAR
     * Value : Description of the component
     */
    private HashMap<String, SGComponent> sgComponentHashMap;

    /**
     * Load all components (as JARs files) from the components directory, as defined in configuration.
     */
    public void loadAllComponents() {
        //for each JAR file, first check whether it's already in the sgComponentHashMap (hence it's already loaded).
        //If false, so load it via an URLClassLoader and add it's description in the map

    }

    public void loadComponent(URL componentPath) {

    }

    public void unloadComponent(String name) {

    }

    public HashMap<String, SGComponent> getSgComponentHashMap() {
        return sgComponentHashMap;
    }

    public void setSgComponentHashMap(HashMap<String, SGComponent> sgComponentHashMap) {
        this.sgComponentHashMap = sgComponentHashMap;
    }

}
