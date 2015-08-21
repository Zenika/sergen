package com.zenika.sergen.resourceManager;


import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;
import javassist.*;
import javassist.bytecode.*;
import javassist.bytecode.annotation.*;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by Zenika on 22/06/2015.
 */
public enum SGResourceManager {
    INSTANCE;

    public List<Class<?>> generateAllResources() throws SGConfigurationNotFound {
        if (null == SGConfiguration.INSTANCE.getConfigurationDAO())
            throw new SGConfigurationNotFound("SGConfigurationDAO not set.");

        try {
            SGConfiguration.INSTANCE.getConfigurationRestAPI().register(SGResourceManager.INSTANCE.CRUDGenerator());

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        //récupère les configuration de la BDD
        List<SGResourceConfiguration> configurations = SGConfiguration.INSTANCE.getConfigurationDAO().loadAll();

        //génère les classes à partir des configurations en BDD
        List<Class<?>> generatedClasses = SGResourceGenerator.generateAllResources(configurations);

        //enregistre les classes générées
        //  SGConfiguration.INSTANCE.getConfigurationRestAPI().registerAll(generatedClasses);

        return generatedClasses;
    }


    public Class CRUDGenerator() throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.zenika.sergen.CRUDGenerator");
        cc.defrost();

        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();

        //pool.insertClassPath(new ClassClassPath(SGConfigurationDAOMongoDB.class));
        pool.insertClassPath(new ClassClassPath(SGResourceManager.class));
        pool.importPackage("org.json.simple");
        pool.importPackage("com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB");
        pool.importPackage("com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration");
        pool.importPackage("com.zenika.sergen.configuration.SGConfiguration");
        pool.importPackage("javax.ws.rs");
        pool.importPackage("com.zenika.sergen.exceptions");
        AnnotationsAttribute attrMethod = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrMethodGetByName = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrMethodDelete = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        AnnotationsAttribute attrMethodSave = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // creation de l'annotation autowired pour l'attribut
        Annotation annotAutowired = new Annotation("org.springframework.beans.factory.annotation.Autowired", constPool);
        attrAutowired.addAnnotation(annotAutowired);
        CtField ctFieldSConfiguration;


        SGResourceGenerator sgResourceGenerator = new SGResourceGenerator();

        ctFieldSConfiguration = new CtField(sgResourceGenerator.resolveCtClass("com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB.SGConfigurationDAOMongoDB"), "sgConfigurationDAOMongoDB", cc);
        cc.addField(ctFieldSConfiguration);
        //  ajouter l'annotation à l'attribut cré
        ctFieldSConfiguration.getFieldInfo().addAttribute(attrAutowired);


        CtMethod getAllConfigurationMethod = CtNewMethod.make("public org.json.simple.JSONArray getAll() { " +
                "" + " SGConfigurationDAOMongoDB sgConfigurationDAOMongoDB = (SGConfigurationDAOMongoDB)SGConfiguration.INSTANCE.getConfigurationDAO();" +
                " JSONArray jsonArray = new JSONArray();" +
                " for (int i = 0; i < sgConfigurationDAOMongoDB.loadAll().size(); i++) {\n" +
                "                          jsonArray.add(sgConfigurationDAOMongoDB.loadAll().get(i));\n" +
                "                    }\n" +
                "                      return jsonArray;" +
                "}", cc);
        cc.addMethod(getAllConfigurationMethod);
        Annotation annotPathGetAll = new Annotation("javax.ws.rs.Path", constPool);
        annotPathGetAll.addMemberValue("value", new StringMemberValue("/all", constPool));
        attrMethod.addAnnotation(annotPathGetAll);

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

        Annotation annotConsumes = new Annotation("javax.ws.rs.Consumes", constPool);

        annotConsumes.addMemberValue("value", arrayMemberValue);

        attrMethod.addAnnotation(annotProduces);

        getAllConfigurationMethod.getMethodInfo().addAttribute(attrMethod);

        //Read Method

        CtMethod getByNameConfigurationMethod = CtNewMethod.make("public org.json.simple.JSONArray getByName(String resourceName) throws SGConfigurationNotFound {\n" +
                "        SGConfigurationDAOMongoDB sgConfigurationDAOMongoDB = (SGConfigurationDAOMongoDB)SGConfiguration.INSTANCE.getConfigurationDAO();\n" +
                "                 JSONArray jsonArray = new JSONArray();\n" +
                "                 for (int i = 0; i < sgConfigurationDAOMongoDB.load(resourceName).size(); i++) {\n" +
                "                                         jsonArray.add(sgConfigurationDAOMongoDB.load(resourceName).get(i));\n" +
                "                                    }\n" +
                "                     return jsonArray;\n" +
                "\n" +
                "\n" +
                "    }", cc);
        cc.addMethod(getByNameConfigurationMethod);
        Annotation annotPathGetByName = new Annotation("javax.ws.rs.Path", constPool);
        annotPathGetByName.addMemberValue("value", new StringMemberValue("/getOne/{resourceName}", constPool));
        attrMethodGetByName.addAnnotation(annotPathGetByName);

        Annotation annotGetOne = new Annotation("javax.ws.rs.GET", constPool);
        attrMethodGetByName.addAnnotation(annotGetOne);


        attrMethodGetByName.addAnnotation(annotProduces);

        getByNameConfigurationMethod.getMethodInfo().addAttribute(attrMethodGetByName);


        ///    Adding Annotation


        /**
         * get constpool
         */
        AttributeInfo paramAtrributeInfoGetByName = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag); // or inVisibleTag
        // ConstPool parameterConstPool = paramAtrributeInfo.getConstPool();
        /**
         * param annotation
         */
        Annotation parameterAnnotationGetByName = new Annotation("javax.ws.rs.PathParam", constPool);
        StringMemberValue parameterMemberValueGetByName = new StringMemberValue("resourceName", constPool);
        parameterAnnotationGetByName.addMemberValue("value", parameterMemberValueGetByName);
        /**
         * add annotation to dimensional array
         */
        ParameterAnnotationsAttribute parameterAtrributeGetByName = ((ParameterAnnotationsAttribute) paramAtrributeInfoGetByName);
        Annotation[][] paramAnnotation = new Annotation[1][1];
        paramAnnotation[0][0] = parameterAnnotationGetByName;

        parameterAtrributeGetByName.setAnnotations(paramAnnotation);
        getByNameConfigurationMethod.getMethodInfo().addAttribute(parameterAtrributeGetByName);


        /// Fin annotations


        //Delete Method

        CtMethod deleteConfigurationMethod = CtNewMethod.make("public void delete( String resourceName) throws SGConfigurationNotFound {\n" +
                "        SGConfigurationDAOMongoDB sgConfigurationDAOMongoDB = (SGConfigurationDAOMongoDB)SGConfiguration.INSTANCE.getConfigurationDAO();\n" +
                "          sgConfigurationDAOMongoDB.delete(resourceName);\n" +
                "\n" +
                "    }", cc);
        cc.addMethod(deleteConfigurationMethod);
        Annotation annotPathDelete = new Annotation("javax.ws.rs.Path", constPool);
        annotPathDelete.addMemberValue("value", new StringMemberValue("/delete/{resourceName}", constPool));
        attrMethodDelete.addAnnotation(annotPathDelete);

        Annotation annotDelete = new Annotation("javax.ws.rs.GET", constPool);
        attrMethodDelete.addAnnotation(annotDelete);


        attrMethodDelete.addAnnotation(annotConsumes);

        deleteConfigurationMethod.getMethodInfo().addAttribute(attrMethodDelete);


        ///    Adding Annotation


        /**
         * get constpool
         */
        AttributeInfo paramAtrributeInfoDelete = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag); // or inVisibleTag
        // ConstPool parameterConstPool = paramAtrributeInfo.getConstPool();
        /**
         * param annotation
         */
        Annotation parameterAnnotationDelete = new Annotation("javax.ws.rs.PathParam", constPool);
        StringMemberValue parameterMemberValueDelete = new StringMemberValue("resourceName", constPool);
        parameterAnnotationDelete.addMemberValue("value", parameterMemberValueDelete);
        /**
         * add annotation to dimensional array
         */
        ParameterAnnotationsAttribute parameterAtrributeDelete = ((ParameterAnnotationsAttribute) paramAtrributeInfoDelete);
        Annotation[][] paramAnnotationDelete = new Annotation[1][1];
        paramAnnotationDelete[0][0] = parameterAnnotationDelete;

        parameterAtrributeDelete.setAnnotations(paramAnnotationDelete);
        deleteConfigurationMethod.getMethodInfo().addAttribute(parameterAtrributeDelete);


        /// Fin annotations



        /////Delete Method

        CtMethod saveConfigurationMethod = CtNewMethod.make("public void save( SGResourceConfiguration sgResourceConfiguration) throws SGConfigurationNotFound {\n" +
                "        SGConfigurationDAOMongoDB sgConfigurationDAOMongoDB = (SGConfigurationDAOMongoDB)SGConfiguration.INSTANCE.getConfigurationDAO();\n" +
                "          sgConfigurationDAOMongoDB.save(sgResourceConfiguration);\n" +
                "\n" +
                "    }", cc);
        cc.addMethod(saveConfigurationMethod);
        Annotation annotPathSave = new Annotation("javax.ws.rs.Path", constPool);
        annotPathSave.addMemberValue("value", new StringMemberValue("/save", constPool));
        attrMethodSave.addAnnotation(annotPathSave);

        Annotation annotSave = new Annotation("javax.ws.rs.POST", constPool);
        attrMethodSave.addAnnotation(annotSave);


        attrMethodSave.addAnnotation(annotConsumes);

        saveConfigurationMethod.getMethodInfo().addAttribute(attrMethodSave);


        ///    Adding Annotation


        /**
         * get constpool
         */
        AttributeInfo paramAtrributeInfoSave = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag); // or inVisibleTag
        // ConstPool parameterConstPool = paramAtrributeInfo.getConstPool();
        /**
         * param annotation
         */
        Annotation parameterAnnotationSave = new Annotation("javax.ws.rs.PathParam", constPool);
        ClassMemberValue parameterMemberValueSave = new ClassMemberValue("com.zenika.sergen.resourceManager.pojo.ResourceConfiguration", constPool);
        parameterAnnotationSave.addMemberValue("value", parameterMemberValueSave);
        /**
         * add annotation to dimensional array
         */
        ParameterAnnotationsAttribute parameterAtrributeSave = ((ParameterAnnotationsAttribute) paramAtrributeInfoSave);
        Annotation[][] paramAnnotationSave = new Annotation[1][1];
        paramAnnotationSave[0][0] = parameterAnnotationSave;

        parameterAtrributeSave.setAnnotations(paramAnnotationSave);
        saveConfigurationMethod.getMethodInfo().addAttribute(parameterAtrributeSave);


        /// Fin annotations


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
        try {
            cc.writeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cc.toClass();

    }

   /* public void delete(String resourceName) throws SGConfigurationNotFound {
        SGConfigurationDAOMongoDB sgConfigurationDAOMongoDB = (SGConfigurationDAOMongoDB) SGConfiguration.INSTANCE.getConfigurationDAO();
        sgConfigurationDAOMongoDB.delete(resourceName);

    }*/

    public static void main(String args[]) {
        try {
            System.out.println(SGResourceManager.INSTANCE.CRUDGenerator());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

}
