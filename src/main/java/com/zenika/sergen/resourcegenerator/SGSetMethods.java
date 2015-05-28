package com.zenika.sergen.resourcegenerator;


import com.zenika.sergen.configuration.pojo.SGMethod;
import com.zenika.sergen.configuration.pojo.SGWorkflows;
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
     *
     * @param method
     * @param constPool
     * @param declaringClass
     * @param objectName   object getting from a workflow
     * @throws javassist.NotFoundException
     */
    public static void createMethod(SGMethod method, ConstPool constPool, CtClass declaringClass, String objectName) throws NotFoundException {

        final String pathAnnotation = "javax.ws.rs.Path";
        final String producesAnnotation = "javax.ws.rs.Produces";
        final String consumesAnnotation = "javax.ws.rs.Consumes";

        SGResourceGenerator resourceGenerator = new SGResourceGenerator();

        ArrayList<SGWorkflows> methodWorkflows = method.getWorkflows();


        ArrayList<String> workFlowParameters;

        // Using the workFlows
        String methodBody = null;
        for (SGWorkflows workflow : methodWorkflows) {
            workFlowParameters = workflow.getParameters();


            StringBuilder workflowToString = new StringBuilder();
            if (workflow.isWithReturn()) {
                workflowToString.append("return");
                workflowToString.append("   ");

            }
            workflowToString.append(objectName);
            workflowToString.append(".");
            workflowToString.append(workflow.getMethode());
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
        String parameters = null;
        for (int i = 0; i < method.getPathParameters().size(); i++) {
            StringBuilder methodParameters = new StringBuilder();
            methodParameters.append("@PathParam(");
            methodParameters.append("\"" + method.getPathParameters().get(i).getName() + "\"");
            methodParameters.append(" ");
            methodParameters.append(method.getPathParameters().get(i).getType());
            methodParameters.append(" ");
            methodParameters.append(method.getPathParameters().get(i).getName());
            // adding comma separating parameters
            while (i != method.getPathParameters().size()) {
                methodParameters.append(", ");
            }
            parameters = parameters + methodParameters.toString();
        }

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
        Annotation annotPath = new Annotation(pathAnnotation, constPool);
        annotPath.addMemberValue("value", new StringMemberValue(method.getPath(), constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation(method.getTypeOfRequete(), constPool);

        attrMethod.addAnnotation(annotGet);

        // Produces values And Consumes Annotations
        MemberValue[] mediaTypeProduces = new StringMemberValue[1];
        MemberValue[] mediaTypeConsumes = new StringMemberValue[1];


        mediaTypeProduces[0] = new StringMemberValue(method.getProduces(), constPool);
        mediaTypeConsumes[0] = new StringMemberValue(method.getConsumes(), constPool);


        Annotation annotProduces = new Annotation(producesAnnotation, constPool);
        ArrayMemberValue arrayMemberValueProduces = new ArrayMemberValue(constPool);
        arrayMemberValueProduces.setValue(mediaTypeProduces);
        annotProduces.addMemberValue("value", arrayMemberValueProduces);

        Annotation annotConsumes = new Annotation(consumesAnnotation, constPool);
        ArrayMemberValue arrayMemberValueConsumes = new ArrayMemberValue(constPool);
        arrayMemberValueConsumes.setValue(mediaTypeProduces);
        annotConsumes.addMemberValue("value", arrayMemberValueConsumes);

        attrMethod.addAnnotation(annotProduces);
        attrMethod.addAnnotation(annotConsumes);

        newMethod.getMethodInfo().addAttribute(attrMethod);

    }
}
