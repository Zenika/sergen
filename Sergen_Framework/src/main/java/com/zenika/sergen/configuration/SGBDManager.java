package com.zenika.sergen.configuration;

/**
 * Created by Zenika on 27/05/2015.
 */
public class SGBDManager {

    /**
     * @param className
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @since 0.1
     */
    static public SGConfigurationDAO init(Class className) throws IllegalAccessException, InstantiationException {
        return (SGConfigurationDAO) className.newInstance();
    }
}
