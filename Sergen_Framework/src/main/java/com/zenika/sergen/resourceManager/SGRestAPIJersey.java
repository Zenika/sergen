package com.zenika.sergen.resourceManager;

import com.zenika.sergen.pojo.SGMethodPathParams;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.ArrayList;

/**
 * Created by Zenika on 29/05/2015.
 */
public class SGRestAPIJersey implements SGRestAPI {

    private ArrayList<Class<?>> registeredResources = null;
    private ResourceConfig resourceConfig = null;
    private ClassPool pool = ClassPool.getDefault();

    public void init(ResourceConfig resourceConfig) {
        this.resourceConfig = resourceConfig;
    }

    /**
     * @param generatedResource : generated Resource at runtime
     */
    public void register(Class<?> generatedResource) {
        this.resourceConfig.register(generatedResource);
        if (!registeredResources.contains(generatedResource)) {
            this.registeredResources.add(generatedResource);
        }

    }

    /**
     * @param resources : Array of Generated Resources at runtime
     */

    @Override
    public void registerAll(ArrayList<Class<?>> resources) {
        for (Class<?> resource : resources) {
            this.resourceConfig.register(resource);
            if (!registeredResources.contains(resource)) {
                registeredResources.add(resource);
            }
        }

    }

    /**
     *
     */
    public void unregisterAll() {

        CtClass ctClass;
        for (Class<?> resource : registeredResources) {
            try {
                ctClass = pool.get(resource.getName());
                ctClass.detach();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Unregister a generated Resource
     *
     * @param resourceName : name of the register
     */
    public void unregister(String resourceName) {

        try {
            pool.get(resourceName).detach();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param mediaType : the consumed mediatype
     * @return
     */
    public String getConsumeDeclaration(String mediaType) {
        return "@Consumes(" + mediaType + ")";
    }

    /**
     * @param mediaType : produced mediatype
     * @return
     */
    public String getProduceDeclaration(String mediaType) {
        return "@Produces(" + mediaType + ")";
    }

    /**
     * @param method
     * @return : return HttpMethods
     */
    public String getHTTPMethodDeclaration(String method) { //GET, POST, ...
        return "javax.ws.rs." + method;
    }

    /**
     * @param path
     * @return the method Path
     */
    public String getPathDeclaration(String path) {
        return "@Path(" + path + ")";
    }

    /**
     * @param params : methods parameters
     * @return : return a Map
     * the key is the method Path
     * the value is method parameter, it contructs a string you can call where calling method
     */
    public String[] getParametersDeclaration(ArrayList<SGMethodPathParams> params) {
        String[] returnedTab = new String[2];
        StringBuilder path = new StringBuilder();
        StringBuilder parameters = new StringBuilder();
        int i = 0;
        for (SGMethodPathParams sgMethodPathParams : params) {
            path.append("{");
            path.append(sgMethodPathParams.getName());
            path.append("}");

            parameters.append("@PathParam(");
            parameters.append("\"");
            parameters.append(sgMethodPathParams.getName());
            parameters.append("\"");
            parameters.append("   ");
            parameters.append(sgMethodPathParams.getType());
            parameters.append(" ");
            parameters.append(sgMethodPathParams.getType());
            if (!(i == params.size())) {
                path.append("/");
                parameters.append(", ");
            }
            i++;
        }
        returnedTab[0] = path.toString();
        returnedTab[1] = path.toString();

        return returnedTab;

    }
}
