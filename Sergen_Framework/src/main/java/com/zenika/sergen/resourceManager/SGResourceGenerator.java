package com.zenika.sergen.resourceManager;

import com.zenika.sergen.components.SGComponentManager;
import com.zenika.sergen.components.pojo.SGComponent;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;
import com.zenika.sergen.resourceManager.pojo.SGResourceMethod;
import com.zenika.sergen.resourceManager.pojo.SGWorkflows;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zenika on 07/04/2015.
 */


public class SGResourceGenerator {
    /**
     * @param resource : the resource configuration
     * @return
     * @throws javassist.NotFoundException      : if the configuration is not found
     * @throws javassist.CannotCompileException : if Javassist cannot compile at runtime the class
     * @throws java.io.IOException
     */
    public static Class generate(SGResourceConfiguration resource) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(resource.getName());
        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();


        // to get all methodes for the resource
        List<SGResourceMethod> methods = resource.getMethods();

        List<SGWorkflows> allMethodWorkFlows;

        String[] fieldPath;
        String objectWorkFlowName = null;


        for (SGResourceMethod method : methods) {

            //to get all workflows for a method
            allMethodWorkFlows = method.getWorkflows();

            for (SGWorkflows w : allMethodWorkFlows) {

                //load the component of the workflow
                SGComponent sgComponent = SGComponentManager.INSTANCE.getComponent(w.getComponentName());

                // to create autowired Fields
                SGSetClassFields.createField(sgComponent.getComponentPackage(), pool, constPool, cc);


                fieldPath = sgComponent.getComponentPackage().split("\\.");

                objectWorkFlowName = fieldPath[fieldPath.length - 1].toLowerCase();
            }
            // to Create Methode with annotations

            SGSetMethods.createMethod(method, constPool, cc, objectWorkFlowName,"GET");

        }


        // To set Annotatations to resource
        SGSetClassAnnotations.addClassAnnotations(resource, constPool, ccFile);

        return cc.toClass();

    }

    /**
     * @param configurations : all com.zenika.sergen.resources 'configuration
     * @return : all generated com.zenika.sergen.resources
     */
    public static List<Class<?>> generateAllResources(List<SGResourceConfiguration> configurations) {
        ArrayList<Class<?>> allGeneratedResources = new ArrayList<>();
        Class<?> generatedClass;
        for (SGResourceConfiguration configuration : configurations) {
            try {
                generatedClass = generate(configuration);
                allGeneratedResources.add(generatedClass);
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allGeneratedResources;
    }


    /**
     * @param name : name of the  the we need the CtClass
     * @return
     * @throws NotFoundException
     */
    public CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }

}
