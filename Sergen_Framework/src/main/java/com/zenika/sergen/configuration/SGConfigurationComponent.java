package com.zenika.sergen.configuration;

/**
 * Created by Zenika on 03/07/2015.
 */
public interface SGConfigurationComponent {
    public  void loadAllComponents();
    public void loadComponent(String componentName);
    public void unloadComponent(String componentName);
}
