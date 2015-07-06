package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.components.pojo.SGComponentMethod;
import com.zenika.sergen.components.pojo.SGComponentMethodParams;
import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        SGComponent sgComponent = null;

        String className = null;
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
                className = classPackage.substring(0, classPackage.indexOf("."));

                //how to set the version

                //Name of the component
                sgComponent.setName(className);
                // Donc il faut mettre la description du composant sous forme
                sgComponent.setDescription(sgJar.getComment());


                try {
                    sgComponent.setSgComponentMethods((ArrayList<SGComponentMethod>) getSGComponentMethods(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            allSGComponentsMap.add(sgComponent);

        }

        return allSGComponentsMap;
    }


    public Method[] getClassMethod(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        Method[] classMethodes = clazz.getDeclaredMethods();
        return classMethodes;

    }

    public Class<?> getMethodReturnType(Method method) {
        return method.getReturnType();
    }

    public Class<?>[] getMethodPamametersType(Method method) {
        return method.getParameterTypes();

    }

    public String getMethodTechnicalName(Method method) {
        return method.getName();
    }

    public String getMethodBussinessName(Method method) {
        SGComponentMethodAnnotation annotation = method.getAnnotation(SGComponentMethodAnnotation.class);
        return annotation.businessName();
    }

    public List<SGComponentMethod> getSGComponentMethods(String className) throws ClassNotFoundException {
        Method[] methods = getClassMethod(className);
        List<SGComponentMethod> sgComponentMethodsList = new ArrayList<>();
        SGComponentMethod sgComponentMethod;
        for (int i = 0; i < methods.length; i++) {
            sgComponentMethod = new SGComponentMethod();
            sgComponentMethod.setOutput(getMethodReturnType(methods[i]));
            sgComponentMethod.setTechnicalName(getMethodTechnicalName(methods[i]));
            sgComponentMethod.setBusinessName(getMethodBussinessName(methods[i]));
            Class<?>[] parametersType = getMethodPamametersType(methods[i]);

            sgComponentMethod.setInputs((ArrayList<SGComponentMethodParams>) getSGComponentMethodParams(parametersType));
            sgComponentMethodsList.add(sgComponentMethod);

        }
        return sgComponentMethodsList;
    }

    public List<SGComponentMethodParams> getSGComponentMethodParams(Class<?>[] parametersType) {
        List<SGComponentMethodParams> sgComponentMethodParamsList = new ArrayList<>();
        SGComponentMethodParams sgComponentMethodParams;
        for (int j = 0; j < parametersType.length; j++) {
            sgComponentMethodParams = new SGComponentMethodParams();
            sgComponentMethodParams.setType(parametersType[j]);
            sgComponentMethodParamsList.add(sgComponentMethodParams);
        }
        return sgComponentMethodParamsList;
    }
}
