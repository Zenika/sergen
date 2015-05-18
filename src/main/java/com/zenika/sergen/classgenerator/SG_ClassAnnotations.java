package com.zenika.sergen.classgenerator;

import com.zenika.sergen.jsonParser.SG_Configuration;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SG_ClassAnnotations {
    // To add Class Annotations
    public static void addClassAnnotations(SG_Configuration classConfig, ConstPool constPool, ClassFile ccFile) throws NotFoundException {

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
