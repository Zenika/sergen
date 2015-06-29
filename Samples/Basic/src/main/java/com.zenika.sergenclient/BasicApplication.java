package com.zenika.sergenclient;


import com.zenika.sergen.components.SGComponentManager;
import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.SGResourceManager;
import com.zenika.sergen.sgConfiguration.sgConfigurationDAO.sgConfigurationDAOMongoDB.SGConfigurationDAOMongoDB;
import com.zenika.sergen.sgConfiguration.sgConfigurationRestAPI.sgConfigurationRestAPIJersey.SGConfigurationRestAPIJersey;
import com.zenika.sergenclient.security.CORSFilter;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Gwennael on 22/02/2015.
 */
@ApplicationPath("src/main/webapp/resources")
@Slf4j
@Data
public class BasicApplication extends ResourceConfig {

    public BasicApplication() {

        log.info("POC started!");

        // Turn on Jersey classpath scanning for providers and com.zenika.sergen.resources in the given package directories
        packages("com.zenika.sergen");

        // Jackson JSON marshalling
        register(JacksonFeature.class);

        // Register for Cross Origin Resource Sharing
        register(CORSFilter.class);
    }

    @PostConstruct
    public void generateClasses() throws IllegalAccessException, InstantiationException, IOException, CannotCompileException, NotFoundException, NoSuchMethodException, ClassNotFoundException {

        //init la config de l'accès aux données
        SGConfigurationDAOMongoDB configDAO = (SGConfigurationDAOMongoDB) SGConfiguration.INSTANCE.setConfigurationDAO(SGConfigurationDAOMongoDB.class);
        configDAO.init("localhost", 27017, "ConfigurationDB", "resources");

        //init l'API REST
        SGConfigurationRestAPIJersey restAPI = (SGConfigurationRestAPIJersey) SGConfiguration.INSTANCE.setConfigurationRestAPI(SGConfigurationRestAPIJersey.class);
        restAPI.init(this);

        //init path to the components
        SGConfiguration.INSTANCE.setComponentsPath(new URL("c:/components"));

        //load all components from hard drive
        SGComponentManager.INSTANCE.loadAllComponents();

        try {
            /*ArrayList<Class<?>> allGeneratedClass =*/
            SGResourceManager.INSTANCE.generateAllResources();
        } catch (SGConfigurationNotFound sgConfigurationNotFound) {
            sgConfigurationNotFound.printStackTrace();
        }
    }
}


