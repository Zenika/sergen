package com.zenika.sergen.classgenerator;

import com.zenika.sergen.jsonParser.ResourceFonctions;
import com.zenika.sergen.jsonParser.SG_ConfigClass;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Zenika on 07/04/2015.
 */
public class ResourceGenerator {

    public static Class generate(SG_ConfigClass sg_configClass) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(sg_configClass.getResourceName());
        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();

        //Create Fields with Autowired Annotation

        for (Map.Entry<String, String> entry : sg_configClass.getResourceAttributs().entrySet()) {
            createField(entry.getValue().toString(), pool, constPool, cc);
        }

        // Create Methode with annotations

        createMethod(sg_configClass.getResourceFonctions(), constPool, cc);

        //Class annotation

        addClassAnnotations(sg_configClass, constPool, ccFile);


        return cc.toClass();
    }


    // To get CtClass
    private static CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }


    //Fonction for creating fields  with Autowired Annotation

    private static void createField(String ClassName, ClassPool classPool, ConstPool constpool, CtClass declaringClass) throws NotFoundException {
        CtField ctField = null;
        final String autowiredAnnotation = "org.springframework.beans.factory.annotation.Autowired";
        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        // creation de l'annotation autowired pour l'attribut
        Annotation annotAutowired = new Annotation(autowiredAnnotation, constpool);
        attrAutowired.addAnnotation(annotAutowired);

        String[] fieldPath = ClassName.split("\\.");

        try {
            classPool.insertClassPath(new ClassClassPath(Class.forName(ClassName)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ctField = new CtField(resolveCtClass(ClassName), fieldPath[fieldPath.length - 1].toLowerCase(), declaringClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        ctField.getFieldInfo().addAttribute(attrAutowired);

        try {
            declaringClass.addField(ctField);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }


    //Creating Methodes with annotions

    private static void createMethod(ResourceFonctions config, ConstPool constPool, CtClass declaringClass) throws NotFoundException {
        final String pathAnnotation = "javax.ws.rs.Path";
        final String producesAnnotation = "javax.ws.rs.Produces";
        final String consumesAnnotation = "javax.ws.rs.Consumes";


        CtMethod newMethod = null;
        try {
            newMethod = CtNewMethod.make(resolveCtClass(config.getReturnType()), config.getFonctionName(), null, null, config.getFonctionBody(), declaringClass);
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
        annotPath.addMemberValue("value", new StringMemberValue(config.getFonctionPath(), constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation(config.getTypeOfRequete(), constPool);
        attrMethod.addAnnotation(annotGet);

        // Produces values And Consumes Annotations
        MemberValue[] mediaTypeProduces = new StringMemberValue[1];
        MemberValue[] mediaTypeConsumes = new StringMemberValue[1];


        mediaTypeProduces[0] = new StringMemberValue(config.getFonctionProduces(), constPool);
        mediaTypeConsumes[0] = new StringMemberValue(config.getFonctionConsumes(), constPool);


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


    // To add Class Annotations
    private static void addClassAnnotations(SG_ConfigClass classConfig, ConstPool constPool, ClassFile ccFile) throws NotFoundException {

        final String pathAnnotation = "javax.ws.rs.Path";
        final String slf4jAnnotation = "javax.ws.rs.Slf4j";

        AnnotationsAttribute attrClasse = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // Path Annotation for class
        Annotation annotClassPath = new Annotation(pathAnnotation, constPool);
        annotClassPath.addMemberValue("value", new StringMemberValue(classConfig.getResourcePath(), constPool));
        attrClasse.addAnnotation(annotClassPath);
        ccFile.addAttribute(attrClasse);

        // Slf4J Annotation for class
        Annotation annotSlf4j = new Annotation(slf4jAnnotation, constPool);
        attrClasse.addAnnotation(annotSlf4j);
        ccFile.addAttribute(attrClasse);

    }

}
