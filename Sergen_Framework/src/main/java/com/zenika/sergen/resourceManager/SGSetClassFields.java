package com.zenika.sergen.resourceManager;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SGSetClassFields {
    /**
     * @param ClassName      : Getting from some annotation from the com.zenika.sergen.components.component
     * @param classPool
     * @param constpool
     * @param declaringClass
     * @throws javassist.NotFoundException
     */
    public static void createField(String ClassName, ClassPool classPool, ConstPool constpool, CtClass declaringClass) throws NotFoundException {
        CtField ctField = null;
        final String autowiredAnnotation = "org.springframework.beans.factory.annotation.Autowired";
        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);

        // creation de l'annotation autowired pour l'attribut

        SGResourceGenerator resourceGenerator = new SGResourceGenerator();

        Annotation annotAutowired = new Annotation(autowiredAnnotation, constpool);
        attrAutowired.addAnnotation(annotAutowired);

        String[] fieldPath = ClassName.split("\\.");

        try {
            classPool.insertClassPath(new ClassClassPath(Class.forName(ClassName)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ctField = new CtField(resourceGenerator.resolveCtClass(ClassName), fieldPath[fieldPath.length - 1].toLowerCase(), declaringClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        if (ctField != null) {
            ctField.getFieldInfo().addAttribute(attrAutowired);
        }

        try {
            declaringClass.addField(ctField);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

}
