package com.zenika.sergen.components;

import com.zenika.sergen.components.pojo.SGComponentMethod;
import com.zenika.sergen.components.pojo.SGComponentMethodParams;
import com.zenika.sergen.components.sergen_annotations.SGComponentClassAnnotation;
import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 08/07/2015.
 */
public class SGExtractInfoFromJar {
    //To get methods of a class
    public Method[] getClassMethod(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        Method[] classMethodes = clazz.getDeclaredMethods();
        return classMethodes;

    }

    //To get Description of a component
    public String getComponentDescription(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        SGComponentClassAnnotation annotation = (SGComponentClassAnnotation) clazz.getAnnotation(SGComponentClassAnnotation.class);
        return annotation.description();
    }

    //To get Version of a component
    public String getComponentVersion(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        SGComponentClassAnnotation annotation = (SGComponentClassAnnotation) clazz.getAnnotation(SGComponentClassAnnotation.class);
        return annotation.version();

    }

    // to get the type of the component
    public SG_COMPONENT_TYPE getComponentType(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        SGComponentClassAnnotation annotation = (SGComponentClassAnnotation) clazz.getAnnotation(SGComponentClassAnnotation.class);
        return annotation.componentType();
    }

    // to get the of workflows of a component
    public List<SGComponentMethod> getSGComponentMethods(String className) throws ClassNotFoundException {
        Method[] methods = getClassMethod(className);
        List<SGComponentMethod> sgComponentMethodsList = new ArrayList<>();
        SGComponentMethod sgComponentMethod;
        for (int i = 0; i < methods.length; i++) {
            sgComponentMethod = new SGComponentMethod();
            sgComponentMethod.setOutput(getMethodReturnType(methods[i]));
            sgComponentMethod.setTechnicalName(getMethodTechnicalName(methods[i]));
            sgComponentMethod.setBusinessName(getMethodBussinessName(methods[i]));
            Class<?>[] parametersType = getMethodParametersType(methods[i]);

            sgComponentMethod.setInputs((ArrayList<SGComponentMethodParams>) getSGComponentMethodParams(parametersType));
            sgComponentMethodsList.add(sgComponentMethod);

        }
        return sgComponentMethodsList;
    }

    // to get return type of a method
    public Class<?> getMethodReturnType(Method method) {
        return method.getReturnType();
    }

    //to get the parametersType of a method
    public Class<?>[] getMethodParametersType(Method method) {
        return method.getParameterTypes();

    }

    // to get technical of a method
    public String getMethodTechnicalName(Method method) {
        return method.getName();
    }

    //  To getBusiness name of a method
    public String getMethodBussinessName(Method method) {
        SGComponentMethodAnnotation annotation = method.getAnnotation(SGComponentMethodAnnotation.class);
        return annotation.businessName();
    }


    // To getParameters of a workflow
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
