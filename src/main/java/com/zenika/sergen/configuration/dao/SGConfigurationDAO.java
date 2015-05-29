package com.zenika.sergen.configuration.dao;

import com.zenika.sergen.configuration.pojo.SGResource;
import com.zenika.sergen.exceptions.SGConfigurationNotFound;

import java.util.ArrayList;

/**
 * Created by Zenika on 20/05/2015.
 */
public interface SGConfigurationDAO {
    public void save(SGResource configuration);

    public void delete(String name);

    public SGResource load(String name) throws SGConfigurationNotFound;

    public ArrayList<SGResource> loadAll();

    public ArrayList<String> loadAllNames();
}
