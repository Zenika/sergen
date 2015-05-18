package com.zenika.sergen.classgenerator;

import com.zenika.sergen.jsonParser.ResourceFonctions;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Created by Zenika on 13/05/2015.
 */
public class SG_Methods_Setting {
    public static void createMethod(ResourceFonctions config, ConstPool constPool, CtClass declaringClass) throws NotFoundException {
        final String pathAnnotation = "javax.ws.rs.Path";
        final String producesAnnotation = "javax.ws.rs.Produces";
        final String consumesAnnotation = "javax.ws.rs.Consumes";


        CtMethod newMethod = null;
        try {
            newMethod = CtNewMethod.make(ResourceGenerator.resolveCtClass(config.getReturnType()), config.getFonctionName(), null, null, config.getFonctionBody(), declaringClass);
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
}
