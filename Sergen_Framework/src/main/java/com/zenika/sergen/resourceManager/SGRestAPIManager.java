package com.zenika.sergen.resourceManager;

/**
 * Created by Zenika on 27/05/2015.
 */
public class SGRestAPIManager {

    /**
     * @param className
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @since 0.1
     */
    static public SGRestAPI init(Class className) throws IllegalAccessException, InstantiationException {
        return (SGRestAPI) className.newInstance();
    }
}
