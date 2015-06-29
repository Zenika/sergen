package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.exceptions.SGComponentAlreadyLoading;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Zenika on 22/06/2015.
 */
public enum SGComponentManager {
    INSTANCE;

    /**
     * Key : name of the JAR
     * Value : Description of the component
     */

    //All components in the hardware
    private Map<String, SGComponent> sgAllComponentsHashMap;

    //All loaded components
    private Map<String, SGComponent> sgAllLoadedComponentsHashMap;

    private URLClassLoader urlClassLoader;

    public void initURLClassloader(String urlName) throws MalformedURLException {
        this.urlClassLoader = new URLClassLoader(new URL[]{new URL(urlName)});

    }

    /**
     * Load all components (as JARs files) from the components directory, as defined in configuration.
     */
    public void loadAllComponents() throws ClassNotFoundException {
        //for each JAR file, first check whether it's already in the sgComponentHashMap (hence it's already loaded).
        //If false, so load it via an URLClassLoader and add it's description in the map
        Set<String> allResourcesName = sgAllComponentsHashMap.keySet();
        for (String resoureName : allResourcesName) {
            if (!sgAllLoadedComponentsHashMap.containsKey(resoureName)) {
                urlClassLoader.loadClass(resoureName);
                sgAllLoadedComponentsHashMap.put(resoureName, sgAllComponentsHashMap.get(resoureName));
            }
        }
    }

    public void loadComponent(String componentName) throws ClassNotFoundException, SGComponentAlreadyLoading {
        if (sgAllLoadedComponentsHashMap.containsKey(componentName))
            throw new SGComponentAlreadyLoading("This class is already loading");

        urlClassLoader.loadClass(componentName);
        sgAllLoadedComponentsHashMap.put(componentName, sgAllComponentsHashMap.get(componentName));

    }

    public void unloadComponent(String name) {
    }

    public Map<String, SGComponent> getSgComponentHashMap() {
        return sgAllComponentsHashMap;
    }

    public void setSgComponentHashMap(HashMap<String, SGComponent> sgAllComponentsHashMap) {
        this.sgAllComponentsHashMap = sgAllComponentsHashMap;
    }


    public Map<String, SGComponent> getSgAllLoadedComponentsHashMap() {
        return sgAllLoadedComponentsHashMap;
    }

    public void setSgAllLoadedComponentsHashMap(Map<String, SGComponent> sgAllLoadedComponentsHashMap) {
        this.sgAllLoadedComponentsHashMap = sgAllLoadedComponentsHashMap;
    }

}
