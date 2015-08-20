package com.zenika.sergen.configuration;

import org.springframework.stereotype.Service;

/**
 * Created by Zenika on 19/06/2015.
 */

public enum SGConfiguration {
    INSTANCE;

    private SGConfigurationDAO configurationDAO;
    private SGConfigurationRestAPI configurationRestAPI;
    private String componentsPath;



    public String getComponentsPath() {
        return componentsPath;
    }

    public void setComponentsPath(String componentsPath) {
        this.componentsPath = componentsPath;
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
