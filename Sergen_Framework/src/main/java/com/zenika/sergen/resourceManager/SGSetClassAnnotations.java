package com.zenika.sergen.resourceManager;

import com.zenika.sergen.pojo.SGConfiguration;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Created by Zenika on 13/05/2015.
 */

/**
 *
 */
public class SGSetClassAnnotations {
    // To add Class Annotations

    /**
     * @param classConfig
     * @param constPool
     * @param ccFile
     * @throws javassist.NotFoundException
     */
    public static void addClassAnnotations(SGConfiguration classConfig, ConstPool constPool, ClassFile ccFile) throws NotFoundException {

        final String pathAnnotation = "javax.ws.rs.Path";
        final String slf4jAnnotation = "javax.ws.rs.Slf4j";

        AnnotationsAttribute attrClass = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // Path Annotation for class
        Annotation annotClassPath = new Annotation(pathAnnotation, constPool);
        annotClassPath.addMemberValue("value", new StringMemberValue(classConfig.getPath(), constPool));
        attrClass.addAnnotation(annotClassPath);
        ccFile.addAttribute(attrClass);

        // Slf4J Annotation for class
        Annotation annotSlf4j = new Annotation(slf4jAnnotation, constPool);
        attrClass.addAnnotation(annotSlf4j);
        ccFile.addAttribute(attrClass);

    }

}
