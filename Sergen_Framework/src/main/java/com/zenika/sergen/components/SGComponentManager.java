package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.components.pojo.SGComponentMethod;
import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGComponentAlreadyLoading;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Zenika on 07/07/2015.
 */
public enum SGComponentManager {
    INSTANCE;

    //All loaded components (in memory)
    private Map<String, SGComponent> sgLoadedComponentsHashMap = new HashMap<>();
    private URLClassLoader urlClassLoader;

    /**
     * @param componentName
     * @return SGComponent from the already loaded sgLoadedComponentsHashMap or {@code null}
     */
    public SGComponent getComponent(String componentName) {
        return sgLoadedComponentsHashMap.get(componentName);
    }

    /**
     * Load all components (as JARs files) from the components directory, as defined in configuration.
     */
    public void loadAllComponents() throws ClassNotFoundException, SGComponentAlreadyLoading, IOException {
        //for each JAR file, first check whether it's already in the sgComponentHashMap (hence it's already loaded).
        //If false, so load it via an URLClassLoader and add it's description in the map

        String componentsPath = SGConfiguration.INSTANCE.getComponentsPath();
        File[] files = new File(componentsPath).listFiles();

        for (File file : files) {
            loadFilesRecursive(file);
        }


    }

    public void loadComponent(String filename) throws ClassNotFoundException, SGComponentAlreadyLoading, IOException {
        loadUrlLoader();

        //check it's a JAR file
        if (filename.endsWith(".jar")) {
            if (sgLoadedComponentsHashMap.containsKey(filename))
                throw new SGComponentAlreadyLoading("This class is already loading");

            //charge les SGComponents dans la hashmap
            getAllSGComponentsFromAjar(filename);
        }
    }

    public void unloadComponent(String name) {
    }

    private void getAllSGComponentsFromAjar(String JarName) throws IOException {

        JarInputStream sgJarFile = new JarInputStream(new FileInputStream(JarName));

        SGExtractInfoFromJar sgExtractInfoFromJar = new SGExtractInfoFromJar();


        SGComponent sgComponent = null;

        String className;
        String classPackage;
        JarEntry jarEntry;
        while (true) {
            jarEntry = sgJarFile.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().endsWith(".class")) {

                sgComponent = new SGComponent();
                classPackage = jarEntry.getName().replaceAll("/", "\\.");
                className = classPackage.substring(0, classPackage.indexOf("."));


                //Name of the component
                sgComponent.setName(className);
                //package of the component
                sgComponent.setComponentPackage(classPackage);
                // Donc il faut mettre la description du composant sous forme

                try {

                    //to set description of a componengt
                    sgComponent.setDescription(sgExtractInfoFromJar.getComponentDescription(className));

                    //to set Version of a component
                    sgComponent.setVersion(sgExtractInfoFromJar.getComponentVersion(className));
                    //to set ComponentType of a component
                    sgComponent.setType(sgExtractInfoFromJar.getComponentType(className));
                    // to set workflows of a component
                    sgComponent.setSgComponentMethods((ArrayList<SGComponentMethod>) sgExtractInfoFromJar.getSGComponentMethods(className));

                    sgLoadedComponentsHashMap.put(className, sgComponent);

                    //load le jar en mémoire
                    //todo : a valider / completer
                    urlClassLoader.loadClass(className);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // load Recursively component in an url
    private void loadFilesRecursive(File pFile) throws SGComponentAlreadyLoading, IOException, ClassNotFoundException {
        for (File file : pFile.listFiles()) {
            if (file.isDirectory()) {
                loadFilesRecursive(file);
            } else {
                loadComponent(file.getName());
            }
        }
    }

    private void loadUrlLoader() {
        String componentsPath = SGConfiguration.INSTANCE.getComponentsPath();
        try {
            this.urlClassLoader = new URLClassLoader(new URL[]{new URL(componentsPath)});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

   /* public static void main(String args[]) throws SGComponentAlreadyLoading, IOException, ClassNotFoundException {

        SGComponentManager.INSTANCE.loadComponent("C:\\Users\\Zenika\\Documents\\sergen\\Sergen_Framework\\src\\main\\java\\com\\zenika\\sergen\\components\\testJar.jar");
    }
    */

}
