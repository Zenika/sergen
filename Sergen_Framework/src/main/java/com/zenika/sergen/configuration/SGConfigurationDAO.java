package com.zenika.sergen.configuration;


import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.resourceManager.pojo.SGResourceConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 20/05/2015.
 */
public interface SGConfigurationDAO {
    public void save(SGResourceConfiguration configuration);

    public void delete(String name);

    public List<SGResourceConfiguration> load(String name) throws SGConfigurationNotFound;

    public List<SGResourceConfiguration> loadAll();

    public List<String> loadAllNames();
}
