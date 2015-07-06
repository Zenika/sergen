package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.exceptions.SGComponentAlreadyLoading;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * Created by Zenika on 22/06/2015.
 */

public class SGConfigurationComponentJar {


    /**
     * Key : name of the JAR
     * Value : Description of the component
     */


    //All loaded components (in memory)
    private Map<String, SGComponent> sgAllLoadedComponentsHashMap;
    private Map<String, SGComponent> sgAllComponentsHashMap;
    private URLClassLoader urlClassLoader;

    private List<String> allComponentNames = null;

    public void init(String componentsPath) throws IOException {
        this.urlClassLoader = new URLClassLoader(new URL[]{new URL(componentsPath)});
        SGJarManager sgJarManager = new SGJarManager();
        // for initializing allComponentNames and sgAllComponentsHashMap
        for (File f : (new File(componentsPath)).listFiles()) {
            for (SGComponent sgComponent : sgJarManager.getAllSGComponentsFromAjar(f.getName())) {
                sgAllComponentsHashMap.put(sgComponent.getName(), sgComponent);
                allComponentNames.add(sgComponent.getName());
            }
        }

    }

    /**
     * Load all components (as JARs files) from the components directory, as defined in configuration.
     */
    public void loadAllComponents() throws ClassNotFoundException {
        //for each JAR file, first check whether it's already in the sgComponentHashMap (hence it's already loaded).
        //If false, so load it via an URLClassLoader and add it's description in the map

        for (String resoureName : allComponentNames) {
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

}
