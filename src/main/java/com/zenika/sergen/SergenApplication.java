package com.zenika.sergen;

import com.zenika.sergen.classgenerator.PojoGenerator;
import com.zenika.sergen.classgenerator.TransformToMap;
import com.zenika.sergen.security.CORSFilter;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.util.Map;

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
        //Json containing attributs for new class
        String json = "{\"produceService\": String,\n" +
                "\"bar\": \"String\"\n" +
                "}";

        //Transform json  to map
        Map<String, Object> props = TransformToMap.parse(json);

        //Generated class
        Class<?> generatedClasse = PojoGenerator.generate(
                "com.zenika.sergen.testRessource", props);

        //Registing the class generated
        this.register(generatedClasse);
    }
}


