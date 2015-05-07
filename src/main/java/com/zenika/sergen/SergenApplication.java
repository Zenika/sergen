package com.zenika.sergen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.sergen.classgenerator.ResourceGenerator;
import com.zenika.sergen.classgenerator.TransformToMap;
import com.zenika.sergen.jsonParser.SG_ConfigClass;
import com.zenika.sergen.security.CORSFilter;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Gwennael on 22/02/2015.
 */
@ApplicationPath("resources")
@Slf4j
public class SergenApplication extends ResourceConfig {
    ApplicationContext ctx;

    public SergenApplication(/*@Context ServletContext servletContext*/) {
        log.info("POC started!");
        // Turn on Jersey classpath scanning for providers and resources in the given package directories
        packages("com.zenika.sergen");


        // Jackson JSON marshalling
        register(JacksonFeature.class);

        // Register for Cross Origin Resource Sharing
        register(CORSFilter.class);

        // ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

    }


    @PostConstruct
    public void generateClasses() throws IllegalAccessException, InstantiationException, IOException, CannotCompileException, NotFoundException, NoSuchMethodException, ClassNotFoundException {
        //Json containing attributs for new class
        String json = "{\"productService\": com.zenika.sergen.product.ProductService\n" +
                "}";

        //Transform json  to map
        Map<String, Object> props = TransformToMap.parse(json);
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            System.out.println(entry.getKey());

            System.out.println(entry.getValue());
        }

       //
        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get("C:\\Users\\Zenika\\Documents\\sergen\\src\\main\\resources\\testResourceFile.json"));
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        //convert json string to object
        SG_ConfigClass sg_configClass = objectMapper.readValue(jsonData, SG_ConfigClass.class);
        //Generated class
        Class<?> generatedClasse = ResourceGenerator.generate(sg_configClass);

        //  Object obj=  ctx.getAutowireCapableBeanFactory().autowire(generatedClasse, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,true);

        //Registing the class generated

        this.register(generatedClasse);


    }
}


