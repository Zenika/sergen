package com.zenika.sergen.resourceManager;

import com.zenika.sergen.pojo.SGMethodPathParams;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Zenika on 29/05/2015.
 */
public interface SGRestAPI {

    public void registerAll(ArrayList<Class<?>> resources);

    public void register(Class<?> resource);

    public void unregisterAll();

    public void unregister(String resourceName);

    public String getConsumeDeclaration(String mediaType);

    public String getProduceDeclaration(String mediaType);

    public String getHTTPMethodDeclaration(String method); //GET, POST, ..

    public String getPathDeclaration(String path);

    public String[] getParametersDeclaration(ArrayList<SGMethodPathParams> params);
}
