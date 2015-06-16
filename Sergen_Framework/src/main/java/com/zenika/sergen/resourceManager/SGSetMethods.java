package com.zenika.sergen.resourceManager;


import com.zenika.sergen.pojo.SGMethod;
import com.zenika.sergen.pojo.SGWorkflows;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.util.ArrayList;

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
    public static void createMethod(SGMethod method, ConstPool constPool, CtClass declaringClass, String objectName) throws NotFoundException {


        ArrayList<SGWorkflows> methodWorkflows = method.getWorkflows();


        ArrayList<String> workFlowParameters;

        // Making the workflow body
        String methodBody = null;
        for (SGWorkflows workflow : methodWorkflows) {
            workFlowParameters = workflow.getParameters();


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


        // For constructing


        SGRestAPIJersey sgRestAPIJersey = new SGRestAPIJersey();
        String[] paramInfo = sgRestAPIJersey.getParametersDeclaration(method.getPathParameters());
        String paramsInPath = paramInfo[0];
        String parameters = paramInfo[1];


        CtMethod newMethod = null;

        try {
            newMethod = CtNewMethod.make("public  " + method.getReturnType() + "   " + objectName + "(" + parameters + ") {" + methodBody + "}", declaringClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        try {
            declaringClass.addMethod(newMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation annotPath = new Annotation(sgRestAPIJersey.getPathDeclaration(), constPool);
        annotPath.addMemberValue("value", new StringMemberValue(method.getPath() + paramsInPath, constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation(sgRestAPIJersey.getHTTPMethodDeclaration("GET"), constPool);

        attrMethod.addAnnotation(annotGet);

        // Produces values And Consumes Annotations
        MemberValue[] mediaTypeProduces = new StringMemberValue[1];
        MemberValue[] mediaTypeConsumes = new StringMemberValue[1];


        mediaTypeProduces[0] = new StringMemberValue(method.getProduces(), constPool);
        mediaTypeConsumes[0] = new StringMemberValue(method.getConsumes(), constPool);


        Annotation annotProduces = new Annotation(sgRestAPIJersey.getProduceDeclaration(), constPool);
        ArrayMemberValue arrayMemberValueProduces = new ArrayMemberValue(constPool);
        arrayMemberValueProduces.setValue(mediaTypeProduces);
        annotProduces.addMemberValue("value", arrayMemberValueProduces);

        Annotation annotConsumes = new Annotation(sgRestAPIJersey.getConsumeDeclaration(), constPool);
        ArrayMemberValue arrayMemberValueConsumes = new ArrayMemberValue(constPool);
        arrayMemberValueConsumes.setValue(mediaTypeProduces);
        annotConsumes.addMemberValue("value", arrayMemberValueConsumes);

        attrMethod.addAnnotation(annotProduces);
        attrMethod.addAnnotation(annotConsumes);

        newMethod.getMethodInfo().addAttribute(attrMethod);

    }

    //function for testing if the workflows has returnType or Not
    public static boolean workflowsHasReturnType() {
        return true;
    }
}
