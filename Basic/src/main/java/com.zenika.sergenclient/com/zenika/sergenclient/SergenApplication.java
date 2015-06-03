package com.zenika.sergenclient;


import com.zenika.sergen.pojo.SGConfiguration;
import com.zenika.sergen.resourceManager.SGResourceGenerator;
import com.zenika.sergen.resourceManager.SGRestAPIJersey;
import com.zenika.sergen.resourceManager.SGRestAPIManager;
import com.zenika.sergenclient.security.CORSFilter;
import dao.SGBDManager;
import dao.SGConfigurationDAOMongoDB;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gwennael on 22/02/2015.
 */
@ApplicationPath("resources")
@Slf4j
public class SergenApplication extends ResourceConfig {


    public SergenApplication() {

        log.info("POC started!");

        // Turn on Jersey classpath scanning for providers and resources in the given package directories
        packages("com.zenika.com.zenika.sergen");

        // Jackson JSON marshalling
        register(JacksonFeature.class);

        // Register for Cross Origin Resource Sharing
        register(CORSFilter.class);

    }


    @PostConstruct
    public void generateClasses() throws IllegalAccessException, InstantiationException, IOException, CannotCompileException, NotFoundException, NoSuchMethodException, ClassNotFoundException {

        //init la config de l'accès aux données
        SGConfigurationDAOMongoDB configDAO = (SGConfigurationDAOMongoDB) SGBDManager.init(SGConfigurationDAOMongoDB.class);
        configDAO.init("localhost", 27017, "ConfigurationDB", "resources");

        //init l'API REST
        SGRestAPIJersey restAPI = (SGRestAPIJersey) SGRestAPIManager.init(SGRestAPIJersey.class);
        restAPI.init(this);

        //récupère les configuration de la BDD
        ArrayList<SGConfiguration> configurations = configDAO.loadAll();

        //génère les classes à partir des configurations en BDD
        ArrayList<Class<?>> allGeneratedClass = SGResourceGenerator.generateAllResources(configurations);

        //enregistre les classes générées
        restAPI.registerAll(allGeneratedClass);
    }
}


