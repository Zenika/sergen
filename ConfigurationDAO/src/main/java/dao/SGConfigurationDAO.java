package dao;


import com.zenika.sergen.exceptions.SGConfigurationNotFound;
import com.zenika.sergen.pojo.SGConfiguration;

import java.util.ArrayList;

/**
 * Created by Zenika on 20/05/2015.
 */
public interface SGConfigurationDAO {
    public void save(SGConfiguration configuration);

    public void delete(String name);

    public SGConfiguration load(String name) throws SGConfigurationNotFound;

    public ArrayList<SGConfiguration> loadAll();

    public ArrayList<String> loadAllNames();
}
