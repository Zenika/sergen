package com.zenika.sergen.configuration;

import com.zenika.sergen.components.SGConfigurationComponentJar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.FileHandler;

/**
 * Created by Zenika on 19/06/2015.
 */

public enum SGConfiguration {
    INSTANCE;

    private SGConfigurationDAO configurationDAO;
    private SGConfigurationRestAPI configurationRestAPI;

    private SGConfigurationComponent configurationComponent;

    public SGConfigurationComponent getConfigurationComponent() {
        return configurationComponent;
    }


    public SGConfigurationComponent setConfigurationComponent(Class className) throws IllegalAccessException, InstantiationException {
        this.configurationComponent = (SGConfigurationComponent) className.newInstance();
        return this.configurationComponent;
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
