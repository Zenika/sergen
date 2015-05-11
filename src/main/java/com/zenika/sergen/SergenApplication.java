package com.zenika.sergen;

import com.zenika.sergen.classgenerator.ResourceGenerator;
import com.zenika.sergen.jsonParser.SG_ConfigClass;
import com.zenika.sergen.jsonParser.TransformJsonToSG_Config;
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

        SG_ConfigClass sg_configClass = TransformJsonToSG_Config.getSGConfigClass("src\\main\\resources\\testResourceFile.json");

        //Generated class

        Class<?> generatedClass = ResourceGenerator.generate(sg_configClass);


        //Registering the class generated

        this.register(generatedClass);


    }
}


