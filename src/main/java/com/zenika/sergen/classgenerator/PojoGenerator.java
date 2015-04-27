package com.zenika.sergen.classgenerator;

import com.zenika.sergen.product.ProductService;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;


/**
 * Created by Zenika on 07/04/2015.
 */
public class PojoGenerator {
    public static Class generate(String className, Map<String, Object> properties) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(className);
        //pour inserer les classes utilisées dans cette class

        pool.insertClassPath(new ClassClassPath(ProductService.class));


        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();


        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // creation de l'annotation autowired pour l'attribut
        Annotation annotAutowired = new Annotation("javax.ws.rs.Autowired", constPool);
        attrAutowired.addAnnotation(annotAutowired);
        CtField ctFieldProductService;

        // for (Map.Entry<String, Object> entry : properties.entrySet()) {
        //    System.out.println( entry.getValue().toString());

        ctFieldProductService = new CtField(resolveCtClass(ProductService.class), "productService", cc);
        cc.addField(ctFieldProductService);
        // ajouter l'annotation à l'attribut cré
        ctFieldProductService.getFieldInfo().addAttribute(attrAutowired);


        //  }

        CtMethod sayHelloMethod;
        sayHelloMethod = CtNewMethod.make("public com.zenika.sergen.product.Product sayHello(){\n" +
                "         return new com.zenika.sergen.product.Product();\n" +

                "     }", cc);
        cc.addMethod(sayHelloMethod);


        Annotation annotPath = new Annotation("javax.ws.rs.Path", constPool);
        annotPath.addMemberValue("value", new StringMemberValue("/hello", constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation("javax.ws.rs.GET", constPool);
        attrMethod.addAnnotation(annotGet);

        // Produces values
        MemberValue[] annotatonProducesValues = new StringMemberValue[1];
        //  annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_PLAIN, constPool);
        annotatonProducesValues[0] = new StringMemberValue(MediaType.APPLICATION_JSON, constPool);
        //annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_XML, constPool);

        Annotation annotProduces = new Annotation("javax.ws.rs.Produces", constPool);
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        arrayMemberValue.setValue(annotatonProducesValues);
        annotProduces.addMemberValue("value", arrayMemberValue);

        attrMethod.addAnnotation(annotProduces);

        sayHelloMethod.getMethodInfo().addAttribute(attrMethod);

        //Class annotation
        AnnotationsAttribute attrClasse = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // Path Annotation for class
        Annotation annotClassPath = new Annotation("javax.ws.rs.Path", constPool);
        annotClassPath.addMemberValue("value", new StringMemberValue("test", constPool));
        attrClasse.addAnnotation(annotClassPath);
        ccFile.addAttribute(attrClasse);

        // Slf4J Annotation for class
        Annotation annotSlf4j = new Annotation("lombok.extern.slf4j.Slf4j", constPool);
        attrClasse.addAnnotation(annotSlf4j);
        ccFile.addAttribute(attrClasse);


        return cc.toClass();
    }

    private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(clazz.getName());


    }


}
