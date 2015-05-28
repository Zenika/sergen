package com.zenika.sergen.resourcegenerator;

import com.zenika.sergen.configuration.pojo.SGMethod;
import com.zenika.sergen.configuration.pojo.SGResource;
import com.zenika.sergen.configuration.pojo.SGWorkflows;
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
     * @throws NotFoundException      : if the configuration is not found
     * @throws CannotCompileException : if Javassist cannot compile at runtime the class
     * @throws IOException
     */
    public static Class generate(SGResource resource) throws NotFoundException, CannotCompileException, IOException {
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
                SGSetClassFields.createField(w.getClassPath().toString(), pool, constPool, cc);

                fieldPath = w.getClassPath().toString().split("\\.");

                objectWorkFlowName = fieldPath[fieldPath.length - 1].toLowerCase();
            }
            // to Create Methode with annotations

            SGSetMethods.createMethod(m, constPool, cc, objectWorkFlowName);

        }


        // To set Annotatations to resource
        SGSetClassAnnotations.addClassAnnotations(resource, constPool, ccFile);


        return cc.toClass();
    }


    // To get CtClass
    public CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }


}
