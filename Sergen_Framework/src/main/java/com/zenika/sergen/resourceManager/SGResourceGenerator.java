package com.zenika.sergen.resourceManager;

import com.zenika.sergen.pojo.SGConfiguration;
import com.zenika.sergen.pojo.SGMethod;
import com.zenika.sergen.pojo.SGWorkflows;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.io.IOException;
import java.util.ArrayList;


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
    public static Class generate(SGConfiguration resource) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(resource.getName());
        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();


        // to get all methodes for the resource
        ArrayList<SGMethod> methods = resource.getMethods();

        ArrayList<SGWorkflows> allMethodWorkFlows;

        String[] fieldPath;
        String objectWorkFlowName = null;

        for (SGMethod m : methods) {

            //to get all workflows for a method
            allMethodWorkFlows = m.getWorkflows();

            for (SGWorkflows w : allMethodWorkFlows) {

                // to create autowired Fields
                SGSetClassFields.createField(getWorkflowPackage(), pool, constPool, cc);

                fieldPath = getWorkflowPackage().split("\\.");

                objectWorkFlowName = fieldPath[fieldPath.length - 1].toLowerCase();
            }
            // to Create Methode with annotations

            SGSetMethods.createMethod(m, constPool, cc, objectWorkFlowName);

        }


        // To set Annotatations to resource
        SGSetClassAnnotations.addClassAnnotations(resource, constPool, ccFile);

        return cc.toClass();

    }

    /**
     * @param configurations : all resources 'configuration
     * @return : all generated resources
     */
    public static ArrayList<Class<?>> generateAllResources(ArrayList<SGConfiguration> configurations) {
        ArrayList<Class<?>> allGeneratedResources = new ArrayList<>();
        Class<?> generatedClass;
        for (SGConfiguration configuration : configurations) {
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

    // To get CtClass

    // function to define, this must return the workflow package getting from appropriate annotation.
    public static String getWorkflowPackage() {
        return null;
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
