package component;

import pojo.SGComponent;

import java.util.ArrayList;

/**
 * Created by Zenika on 10/06/2015.
 */
public interface SGComponentManager {
    public ArrayList<SGComponent> load(String name);

    public ArrayList<SGComponent> loadAll();

    public void save(ArrayList<SGComponent> component);


    public void delete(String name);
}
