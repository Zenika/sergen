package com.zenika.sergen.configuration;

import com.zenika.sergen.resourceManager.pojo.SGResourceMethodPathParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 29/05/2015.
 */
public interface SGConfigurationRestAPI {

    public void registerAll(List<Class<?>> resources);

    public void register(Class<?> resource);

    public void unregisterAll();

    public void unregister(String resourceName);

    public String getConsumeDeclaration();

    public String getProduceDeclaration();

    public String getHTTPMethodDeclaration(String method); //GET, POST, ..

    public String getPathDeclaration();

    public String[] getParametersDeclaration(List<SGResourceMethodPathParams> params);
}
