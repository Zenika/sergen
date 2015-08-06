package com.zenika.sergen.resourceManager;

import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Created by Zenika on 22/06/2015.
 */
public enum SGResourceManager {
    INSTANCE;

    public ArrayList<Class<?>> generateAllResources() throws SGConfigurationNotFound {
        if (null == SGConfiguration.INSTANCE.getConfigurationDAO())
            throw new SGConfigurationNotFound("SGConfigurationDAO not set.");

        try {
            SGConfiguration.INSTANCE.getConfigurationRestAPI().register(CRUDGenerator());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        //récupère les configuration de la BDD
        ArrayList<SGResourceConfiguration> configurations = SGConfiguration.INSTANCE.getConfigurationDAO().loadAll();

        //génère les classes à partir des configurations en BDD
        ArrayList<Class<?>> generatedClasses = SGResourceGenerator.generateAllResources(configurations);

        //enregistre les classes générées
        SGConfiguration.INSTANCE.getConfigurationRestAPI().registerAll(generatedClasses);

        return generatedClasses;
    }


    public Class CRUDGenerator() throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("CRUDGenerator");
        //pour inserer les classes utilisées dans cette class

       // pool.insertClassPath(new ClassClassPath(SGConfiguration.class));
       // pool.insertClassPath(new ClassClassPath(java.util.ArrayList.class));
      //  pool.importPackage("java.util.ArrayList");

        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();


        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // creation de l'annotation autowired pour l'attribut
        Annotation annotAutowired = new Annotation("org.springframework.beans.factory.annotation.Autowired", constPool);
        attrAutowired.addAnnotation(annotAutowired);
        CtField ctFieldProductService;

        // for (Map.Entry<String, Object> entry : properties.entrySet()) {
        //    System.out.println( entry.getValue().toString());
        SGResourceGenerator sgResourceGenerator = new SGResourceGenerator();
        ctFieldProductService = new CtField(sgResourceGenerator.resolveCtClass("com.zenika.sergen.configuration.SGConfiguration"), "sgConfiguration", cc);
        cc.addField(ctFieldProductService);
        // ajouter l'annotation à l'attribut cré
        ctFieldProductService.getFieldInfo().addAttribute(attrAutowired);


        //  }

        CtMethod getAllConfigurationMethod = CtNewMethod.make(sgResourceGenerator.resolveCtClass("java.util.ArrayList"),"getAll",null,null,"return com.zenika.sergen.configuration.SGConfiguration.INSTANCE.getConfigurationDAO().loadAll();",cc);
        cc.addMethod(getAllConfigurationMethod);


        Annotation annotPath = new Annotation("javax.ws.rs.Path", constPool);
        annotPath.addMemberValue("value", new StringMemberValue("/all", constPool));
        attrMethod.addAnnotation(annotPath);

        Annotation annotGet = new Annotation("javax.ws.rs.GET", constPool);
        attrMethod.addAnnotation(annotGet);

        // Produces values
        MemberValue[] annotatonProducesValues = new StringMemberValue[1];
        //  annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_PLAIN, constPool);
        annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_PLAIN, constPool);
        //annotatonProducesValues[0] = new StringMemberValue(MediaType.TEXT_XML, constPool);

        Annotation annotProduces = new Annotation("javax.ws.rs.Produces", constPool);
        ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
        arrayMemberValue.setValue(annotatonProducesValues);
        annotProduces.addMemberValue("value", arrayMemberValue);

        attrMethod.addAnnotation(annotProduces);

        getAllConfigurationMethod.getMethodInfo().addAttribute(attrMethod);

        //Class annotation
        AnnotationsAttribute attrClasse = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // Path Annotation for class
        Annotation annotClassPath = new Annotation("javax.ws.rs.Path", constPool);
        annotClassPath.addMemberValue("value", new StringMemberValue("config", constPool));
        attrClasse.addAnnotation(annotClassPath);
        ccFile.addAttribute(attrClasse);

        // Slf4J Annotation for class
        Annotation annotSlf4j = new Annotation("lombok.extern.slf4j.Slf4j", constPool);
        attrClasse.addAnnotation(annotSlf4j);
        ccFile.addAttribute(attrClasse);


        return cc.toClass();
    }




  /*  public static void main(String[] args){
        try {
            System.out.println( SGResourceManager.INSTANCE.CRUDGenerator());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
*/
}
