package com.zenika.sergen.resourceManager;


import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.configuration.SGConfigurationRestAPI;
import com.zenika.sergen.resourceManager.pojo.SGResourceMethod;
import com.zenika.sergen.resourceManager.pojo.SGWorkflows;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SGSetMethods {
    /**
     * @param method
     * @param constPool
     * @param declaringClass
     * @param objectName     getting from some annotation from the called com.zenika.sergen.components.component
     * @throws javassist.NotFoundException
     */
    public static void createMethod(SGResourceMethod method, ConstPool constPool, CtClass declaringClass, String objectName, String httpMethod) throws NotFoundException {

        SGConfigurationRestAPI restAPI = SGConfiguration.INSTANCE.getConfigurationRestAPI();

        List<SGWorkflows> methodWorkflows = method.getWorkflows();


        // Making the method body
        String methodBody = getMethodBody(methodWorkflows, objectName);


        //To get methods parameters
        String[] paramInfo = SGConfiguration.INSTANCE.getConfigurationRestAPI().getParametersDeclaration(method.getPathParameters());


        makeMethod(restAPI, method, objectName, paramInfo, methodBody, declaringClass, constPool, httpMethod);


    }

    //function for testing if the workflows has returnType or Not


    public static boolean workflowsHasReturnType() {
        return true;
    }


    //For making method body

    /**
     * @param sgWorkflowses
     * @param objectName
     * @return
     */
    public static String getMethodBody(List<SGWorkflows> sgWorkflowses, String objectName) {
        String methodBody = null;
        for (SGWorkflows workflow : sgWorkflowses) {
           List<String> workFlowParameters = workflow.getParameters();


            StringBuilder workflowToString = new StringBuilder();
            if (workflowsHasReturnType()) {
                workflowToString.append("return");
                workflowToString.append("   ");
            }
            workflowToString.append(objectName);
            workflowToString.append(".");
            workflowToString.append(workflow.getMethod());
            workflowToString.append(".");
            workflowToString.append("(");
            // If there are some parameters
            if (!workFlowParameters.isEmpty()) {

                for (int i = 0; i < workFlowParameters.size() - 1; i++) {
                    workflowToString.append(workFlowParameters.get(i) + ", ");

                }
                //if this the last parameter
                workflowToString.append(workFlowParameters.get(workFlowParameters.size() - 1));
            }
            workflowToString.append(");");

            methodBody = methodBody + workflowToString.toString();

        }
        return methodBody;
    }


    //for making Method

    /**
     * @param restAPI
     * @param method
     * @param objectName
     * @param paramInfo
     * @param methodBody
     * @param declaringClass
     * @param constPool
     */
    public static void makeMethod(SGConfigurationRestAPI restAPI, SGResourceMethod method, String objectName, String[] paramInfo, String methodBody, CtClass declaringClass, ConstPool constPool, String httpMethod) {
        CtMethod newMethod = null;

        try {
            newMethod = CtNewMethod.make("public  " + method.getReturnType() + "   " + objectName + "(" + paramInfo[1] + ") {" + methodBody + "}", declaringClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        try {
            declaringClass.addMethod(newMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        newMethod.getMethodInfo().addAttribute(SetAnnotationsMethod(restAPI, constPool, method, paramInfo, httpMethod));


    }

    /**
     * @param restAPI
     * @param constPool
     * @param method
     * @param paramInfo
     * @return
     */
    public static AnnotationsAttribute SetAnnotationsMethod(SGConfigurationRestAPI restAPI, ConstPool constPool, SGResourceMethod method, String[] paramInfo, String httpMethod) {
        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation annotPath = new Annotation(restAPI.getPathDeclaration(), constPool);
        annotPath.addMemberValue("value", new StringMemberValue(method.getPath() + paramInfo[0], constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation(restAPI.getHTTPMethodDeclaration("GET"), constPool);

        attrMethod.addAnnotation(annotGet);

        // Produces values And Consumes Annotations
        MemberValue[] mediaTypeProduces = new StringMemberValue[1];
        MemberValue[] mediaTypeConsumes = new StringMemberValue[1];


        mediaTypeProduces[0] = new StringMemberValue(method.getProduces(), constPool);
        mediaTypeConsumes[0] = new StringMemberValue(method.getConsumes(), constPool);


        Annotation annotProduces = new Annotation(restAPI.getProduceDeclaration(), constPool);
        ArrayMemberValue arrayMemberValueProduces = new ArrayMemberValue(constPool);
        arrayMemberValueProduces.setValue(mediaTypeProduces);
        annotProduces.addMemberValue("value", arrayMemberValueProduces);

        Annotation annotConsumes = new Annotation(restAPI.getConsumeDeclaration(), constPool);
        ArrayMemberValue arrayMemberValueConsumes = new ArrayMemberValue(constPool);
        arrayMemberValueConsumes.setValue(mediaTypeProduces);
        annotConsumes.addMemberValue("value", arrayMemberValueConsumes);

        attrMethod.addAnnotation(annotProduces);
        attrMethod.addAnnotation(annotConsumes);
        return attrMethod;
    }
}




