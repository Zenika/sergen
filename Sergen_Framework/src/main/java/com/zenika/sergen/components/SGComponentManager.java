package com.zenika.sergen.components;

/**
 * Created by Zenika on 07/07/2015.
 */
public enum SGComponentManager {
    INSTANCE;
    public void loadAllComponents(){
        try {
            (new SGConfigurationComponentJar()).loadAllComponents();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
