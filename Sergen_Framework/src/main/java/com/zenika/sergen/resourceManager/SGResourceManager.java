package com.zenika.sergen.resourceManager;

import com.zenika.sergen.configuration.SGConfiguration;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;

import java.util.ArrayList;

/**
 * Created by Zenika on 22/06/2015.
 */
public enum SGResourceManager {
      INSTANCE;
    public ArrayList<Class<?>> generateAllResources() throws SGConfigurationNotFound {
        if (null == SGConfiguration.INSTANCE.getConfigurationDAO())
            throw new SGConfigurationNotFound("SGConfigurationDAO not set.");

        //récupère les configuration de la BDD
        ArrayList<SGResourceConfiguration> configurations = SGConfiguration.INSTANCE.getConfigurationDAO().loadAll();

        //génère les classes à partir des configurations en BDD
        ArrayList<Class<?>> generatedClasses = SGResourceGenerator.generateAllResources(configurations);

        //enregistre les classes générées
        SGConfiguration.INSTANCE.getConfigurationRestAPI().registerAll(generatedClasses);

        return generatedClasses;
    }
}
