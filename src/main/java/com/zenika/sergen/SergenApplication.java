package com.zenika.sergen;

import com.zenika.sergen.classgenerator.PojoGenerator;
import com.zenika.sergen.classgenerator.TransformToMap;
import com.zenika.sergen.product.Product;
import com.zenika.sergen.product.ProductDAO;
import com.zenika.sergen.product.ProductResource;
import com.zenika.sergen.product.ProductService;
import com.zenika.sergen.security.CORSFilter;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;
import java.io.IOException;
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
        String json = "{\"produceService\": String,\n" +
                "\"bar\": \"String\"\n" +
                "}";

        //Transform json  to map
        Map<String, Object> props = TransformToMap.parse(json);

        //Generated class
        Class<?> generatedClasse = PojoGenerator.generate(
                "com.zenika.sergen.testRessource", props);

    //  Object obj=  ctx.getAutowireCapableBeanFactory().autowire(generatedClasse, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,true);

        //Registing the class generated

       this.register(generatedClasse);











    }
}


