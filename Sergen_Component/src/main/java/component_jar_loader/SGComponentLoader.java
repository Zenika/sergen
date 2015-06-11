package component_jar_loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Zenika on 11/06/2015.
 */
public class SGComponentLoader {

    public Class load(String jarName, String className) throws ClassNotFoundException, MalformedURLException {
        File file = new File(jarName);
        URL url = file.toURL();
        URL[] urls = new URL[]{url};
        ClassLoader cl = new URLClassLoader(urls);

        Class cls = cl.loadClass(className);

        return cls;
    }
}

