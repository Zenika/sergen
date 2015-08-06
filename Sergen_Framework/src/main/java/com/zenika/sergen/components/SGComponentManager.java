package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.components.pojo.SGComponentMethod;
import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGComponentAlreadyLoading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
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
    private ClassLoader urlClassLoader;

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
    public void loadAllComponents() throws SGComponentAlreadyLoading, IOException, ClassNotFoundException {
        SGComponentManager.INSTANCE.loadUrlLoader();

        File dir = new File(SGConfiguration.INSTANCE.getComponentsPath());
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar");
            }
        });


        for (File file : files) {
            loadComponent(file.getAbsolutePath());
        }

    }

    /**
     * @param ComponentName
     * @throws ClassNotFoundException
     * @throws SGComponentAlreadyLoading
     * @throws IOException
     */
    public void loadComponent(String ComponentName) throws ClassNotFoundException, SGComponentAlreadyLoading, IOException {
        SGComponentManager.INSTANCE.loadUrlLoader();

        //check if the class is already loaded

        if (sgLoadedComponentsHashMap.containsKey(ComponentName))
            throw new SGComponentAlreadyLoading("This component is already loading");


        //charge les SGComponents dans la hashmap
        getAllSGComponentsFromAjar(ComponentName);

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
                    sgComponent.setDescription(sgExtractInfoFromJar.getComponentDescription("com.zenika.sergen.ComponentJarFiles.mathematicsJar.ReturnDix"));

                    //to set Version of a component
                    sgComponent.setVersion(sgExtractInfoFromJar.getComponentVersion("com.zenika.sergen.ComponentJarFiles.mathematicsJar.ReturnDix"));
                    //to set ComponentType of a component
                    sgComponent.setType(sgExtractInfoFromJar.getComponentType("com.zenika.sergen.ComponentJarFiles.mathematicsJar.ReturnDix"));
                    // to set workflows of a component


                    urlClassLoader.loadClass("com.zenika.sergen.ComponentJarFiles.mathematicsJar.ReturnDix");
                    sgComponent.setSgComponentMethods((ArrayList<SGComponentMethod>) sgExtractInfoFromJar.getSGComponentMethods("com.zenika.sergen.ComponentJarFiles.mathematicsJar.ReturnDix"));

                    sgLoadedComponentsHashMap.put(className, sgComponent);

                    //load le jar en mémoire
                    //todo : a valider / completer
                    // System.out.println(classPackage.substring(0, classPackage.length() - 6));


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private ClassLoader loadUrlLoader() {
        String componentsPath = "file://" + SGConfiguration.INSTANCE.getComponentsPath();

        try {
            this.urlClassLoader = new URLClassLoader(new URL[]{new URL(componentsPath)});

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this.urlClassLoader;
    }


}
