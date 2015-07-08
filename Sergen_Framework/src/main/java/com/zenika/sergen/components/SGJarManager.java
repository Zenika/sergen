package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.components.pojo.SGComponentMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Zenika on 30/06/2015.
 */

// Il reste à déterminer comment la version
public class SGJarManager {

    public List<SGComponent> allSGComponentsMap = new ArrayList<>();

    public List<SGComponent> getAllSGComponentsFromAjar(String JarName) throws IOException {

        JarInputStream sgJarFile = new JarInputStream(new FileInputStream(JarName));

        SGExtractInfoFromJar sgExtractInfoFromJar = new SGExtractInfoFromJar();

        SGComponent sgComponent = null;

        String componentName;
        String classPackage;
        JarEntry sgJar;
        while (true) {
            sgJar = sgJarFile.getNextJarEntry();
            if (sgJar == null) {
                break;
            }
            if (sgJar.getName().endsWith(".class")) {

                sgComponent = new SGComponent();
                classPackage = sgJar.getName().replaceAll("/", "\\.");
                componentName = classPackage.substring(0, classPackage.indexOf("."));


                //Name of the component
                sgComponent.setName(componentName);
                // Donc il faut mettre la description du composant sous forme


                try {

                    //to set description of a componengt
                    sgComponent.setDescription(sgExtractInfoFromJar.getComponentDescription(componentName));

                    //to set Version of a component
                    sgComponent.setVersion(sgExtractInfoFromJar.getComponentVersion(componentName));
                    //to set ComponentType of a component
                    sgComponent.setType(sgExtractInfoFromJar.getComponentType(componentName));
                    // to setworkflows of a component
                    sgComponent.setSgComponentMethods((ArrayList<SGComponentMethod>) sgExtractInfoFromJar.getSGComponentMethods(componentName));


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
            allSGComponentsMap.add(sgComponent);

        }

        return allSGComponentsMap;
    }


}
