package com.zenika.sergen.configuration.dao;

import com.zenika.sergen.configuration.pojo.SGResource;

import java.util.ArrayList;

/**
 * Created by Zenika on 20/05/2015.
 */
public interface SGConfigurationDAO {
    public void save(SGResource configuration);

    public void delete(String name);

    public SGResource load(String name);

    public ArrayList<SGResource> loadAll();

    public ArrayList<String> loadAllNames();
}
