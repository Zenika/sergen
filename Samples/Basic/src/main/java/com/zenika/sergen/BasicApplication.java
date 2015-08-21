package com.zenika.sergen;

/**
 * Created by matekordial on 30/07/2015.
 */


import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.resourceManager.SGResourceManager;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;
import com.zenika.sergen.resourceManager.pojo.SGResourceMethod;
import com.zenika.sergen.resourceManager.pojo.SGWorkflows;
import com.zenika.sergen.security.CORSFilter;
import com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB.SGConfigurationDAOMongoDB;
import com.zenika.sergen.sgConfiguration.sgConfigurationRestAPI.sgConfigurationRestAPIJersey.SGConfigurationRestAPIJersey;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gwennael on 22/02/2015.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class BasicApplication extends ResourceConfig {

    public BasicApplication() {

        //Log.log("POC started!");

        // Turn on Jersey classpath scanning for providers and com.zenika.sergen.resources in the given package directories
        packages("com.zenika.sergen");



        // Jackson JSON marshalling
        register(JacksonFeature.class);

        // Register for Cross Origin Resource Sharing
        register(CORSFilter.class);
    }

    @PostConstruct
    public void generateClasses() throws IllegalAccessException, InstantiationException, IOException, CannotCompileException, NotFoundException, NoSuchMethodException, ClassNotFoundException {


        /**** CONFIG : START ****/
        //init la config de l'accès aux données
        SGConfigurationDAOMongoDB configDAO = (SGConfigurationDAOMongoDB) SGConfiguration.INSTANCE.setConfigurationDAO(SGConfigurationDAOMongoDB.class);
        configDAO.init("localhost", 27017, "ConfigurationDB", "resources");

        //init l'API REST
        SGConfigurationRestAPIJersey restAPI = (SGConfigurationRestAPIJersey) SGConfiguration.INSTANCE.setConfigurationRestAPI(SGConfigurationRestAPIJersey.class);
        restAPI.init(this);

        //init path to the components:
        //    SGConfiguration.INSTANCE.setComponentsPath("/sergen_framework/src/main/java/com/zenika/sergen/ComponentJarFiles/");

        /**** CONFIG : END ****/

        /**** LOAD : START ****/
        //load all components from hard drive


        //  try {
        //      SGComponentManager.INSTANCE.loadAllComponents();
        //  } catch (SGComponentAlreadyLoading sgComponentAlreadyLoading) {
        //     sgComponentAlreadyLoading.printStackTrace();
        // }

        //generate REST resources from configurations saved in database

            /*ArrayList<Class<?>> allGeneratedClass =*/




           // configDAO.delete("MathResource");

        //System.out.println(SGResourceManager.INSTANCE.CRUDGenerator());

        SGResourceConfiguration sgResourceConfiguration = new SGResourceConfiguration();
        sgResourceConfiguration.setName("FruitsResource");
        sgResourceConfiguration.setPath("/");
        SGResourceMethod method1 = new SGResourceMethod();
        method1.setName("getResult");
        method1.setPath("/result");
        method1.setConsumes("sdsdsd");
        method1.setProduces("sqsqsq");
        method1.setReturnType("sqsqsqs");

        SGWorkflows workflows1 = new SGWorkflows();
        workflows1.setComponentName("component");
        workflows1.setMethod("method");
        List<String> parameters = new ArrayList<>();
        parameters.add("param1");
        parameters.add("param2");
        workflows1.setParameters(parameters);
        SGWorkflows workflows2 = new SGWorkflows();

        workflows2.setComponentName("component");
        workflows2.setMethod("method");

        workflows2.setParameters(parameters);

        List<SGWorkflows> listWorkFlows = new ArrayList<>();
        listWorkFlows.add(workflows1);
        listWorkFlows.add(workflows2);
        method1.setWorkflows(listWorkFlows);
        SGResourceMethod method2 ;
        method2 = method1;

        List<SGResourceMethod> listMethod = new ArrayList<>();
        listMethod.add(method1);
        listMethod.add(method2);
        sgResourceConfiguration.setMethods(listMethod);

        configDAO.save(sgResourceConfiguration);
        //register(SGResourceManager.INSTANCE.CRUDGenerator());


        /**** LOAD : END ****/

    }
}
