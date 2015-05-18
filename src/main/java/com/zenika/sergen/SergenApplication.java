package com.zenika.sergen;

import com.zenika.sergen.classgenerator.ResourceGenerator;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.jsonParser.SG_Configuration;
import com.zenika.sergen.jsonParser.SG_ConfigurationManager;
import com.zenika.sergen.security.CORSFilter;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;

/**
 * Created by Gwennael on 22/02/2015.
 */
@ApplicationPath("resources")
@Slf4j
public class SergenApplication extends ResourceConfig {


    public SergenApplication() {

        log.info("POC started!");

        // Turn on Jersey classpath scanning for providers and resources in the given package directories
        packages("com.zenika.sergen");

        // Jackson JSON marshalling
        register(JacksonFeature.class);

        // Register for Cross Origin Resource Sharing
        register(CORSFilter.class);

    }


    @PostConstruct
    public void generateClasses() throws IllegalAccessException, InstantiationException, IOException, CannotCompileException, NotFoundException, NoSuchMethodException, ClassNotFoundException {


        // Transform Json to class Config

        SG_ConfigurationManager sg_configurationManager = new SG_ConfigurationManager();

        SG_Configuration sg_config = null;
        try {
            sg_config = sg_configurationManager.Configuration();
        } catch (SGConfigurationNotFound sgConfigurationNotFound) {
            sgConfigurationNotFound.printStackTrace();
        }

        //Generated class

        Class<?> generatedClass = ResourceGenerator.generate(sg_config);


        //Registering the class generated

        this.register(generatedClass);


    }
}


