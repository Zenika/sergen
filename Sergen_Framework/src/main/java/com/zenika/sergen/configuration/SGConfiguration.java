package com.zenika.sergen.configuration;

import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.pojo.SGResourceConfiguration;
import com.zenika.sergen.resourceManager.SGResourceGenerator;

import java.util.ArrayList;

/**
 * Created by Zenika on 19/06/2015.
 */

public enum SGConfiguration {
    INSTANCE;

    private SGConfigurationDAO configurationDAO;
    private SGConfigurationRestAPI configurationRestAPI;

    public ArrayList<Class<?>> generateAllResources() throws SGConfigurationNotFound {
        if (null == this.configurationDAO)
            throw new SGConfigurationNotFound("SGConfigurationDAO not set.");

        //récupère les configuration de la BDD
        ArrayList<SGResourceConfiguration> configurations = this.configurationDAO.loadAll();

        //génère les classes à partir des configurations en BDD
        ArrayList<Class<?>> generatedClasses = SGResourceGenerator.generateAllResources(configurations);

        //enregistre les classes générées
        this.configurationRestAPI.registerAll(generatedClasses);

        return generatedClasses;
    }

    public SGConfigurationDAO setConfigurationDAO(Class className) throws IllegalAccessException, InstantiationException {
        this.configurationDAO = (SGConfigurationDAO) className.newInstance();

        return this.configurationDAO;
    }

    public SGConfigurationRestAPI setConfigurationRestAPI(Class className) throws IllegalAccessException, InstantiationException {
        this.configurationRestAPI = (SGConfigurationRestAPI) className.newInstance();

        return this.configurationRestAPI;
    }

    public SGConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }

    public SGConfigurationRestAPI getConfigurationRestAPI() {
        return configurationRestAPI;
    }
}
