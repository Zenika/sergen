package com.zenika.sergen.classgenerator;

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
        final String autowiredAnnotation = "org.springframework.beans.factory.annotation.Autowired";
        final String pathAnnotation = "javax.ws.rs.Path";
        final String slf4jAnnotation = "javax.ws.rs.Slf4j";
        final String producesAnnotation = "javax.ws.rs.Produces";
        final String consumesAnnotation = "javax.ws.rs.Consumes";

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(sg_configClass.getResourceName());
        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();




        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // creation de l'annotation autowired pour l'attribut
        Annotation annotAutowired = new Annotation(autowiredAnnotation, constPool);
        attrAutowired.addAnnotation(annotAutowired);

        CtField ctFieldProductService;

        for (Map.Entry<String, String> entry : sg_configClass.getResourceAttributs().entrySet()) {

            //pour recuperer le nom de l'attribut
            String[] fieldPath = entry.getValue().split("\\.");
            try {
                pool.insertClassPath(new ClassClassPath(Class.forName(entry.getValue())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            ctFieldProductService = new CtField(resolveCtClass(entry.getValue().toString()), fieldPath[fieldPath.length - 1].toLowerCase(), cc);

            // ajouter l'annotation à l'attribut cré
            ctFieldProductService.getFieldInfo().addAttribute(attrAutowired);

            cc.addField(ctFieldProductService);
        }

        CtMethod sayHelloMethod;
        String returnType = sg_configClass.getResourceFonctions().getReturnType();
        String fonctionName = sg_configClass.getResourceFonctions().getFonctionName();
        String fonctionBody = sg_configClass.getResourceFonctions().getFonctionBody();
        CtClass[] fonctionparameters = null;
        CtClass[] fonctionExceptions = null;
        sayHelloMethod = CtNewMethod.make(resolveCtClass(returnType), fonctionName, fonctionparameters, fonctionExceptions, fonctionBody, cc);
        cc.addMethod(sayHelloMethod);

        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation annotPath = new Annotation(pathAnnotation, constPool);
        annotPath.addMemberValue("value", new StringMemberValue(sg_configClass.getResourceFonctions().getFonctionPath(), constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation(sg_configClass.getResourceFonctions().getTypeOfRequete(), constPool);
        attrMethod.addAnnotation(annotGet);

        // Produces values
        MemberValue[] annotatonProducesValues = new StringMemberValue[1];
        //  annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_PLAIN, constPool);
        annotatonProducesValues[0] = new StringMemberValue(sg_configClass.getResourceFonctions().getFonctionProduces(), constPool);
        //annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_XML, constPool);

        Annotation annotProduces = new Annotation(producesAnnotation, constPool);
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        arrayMemberValue.setValue(annotatonProducesValues);
        annotProduces.addMemberValue("value", arrayMemberValue);

        attrMethod.addAnnotation(annotProduces);

        sayHelloMethod.getMethodInfo().addAttribute(attrMethod);

        //Class annotation
        AnnotationsAttribute attrClasse = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // Path Annotation for class
        Annotation annotClassPath = new Annotation(pathAnnotation, constPool);
        annotClassPath.addMemberValue("value", new StringMemberValue(sg_configClass.getResourcePath(), constPool));
        attrClasse.addAnnotation(annotClassPath);
        ccFile.addAttribute(attrClasse);

        // Slf4J Annotation for class
        Annotation annotSlf4j = new Annotation(slf4jAnnotation, constPool);
        attrClasse.addAnnotation(annotSlf4j);
        ccFile.addAttribute(attrClasse);

        // cc.writeFile();
        return cc.toClass();
    }

    private static CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }

   /* private static CtField createField(String ClassName, ClassPool pool, CtClass declaringClass) throws NotFoundException {
        CtField ctField = null;

        try {
            pool.insertClassPath(new ClassClassPath(Class.forName(ClassName)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ctField = new CtField(resolveCtClass(ClassName), "productResource", declaringClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return ctField;
    }
   */

}
