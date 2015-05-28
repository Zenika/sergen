package com.zenika.sergen.jsonParser;

import java.util.Map;

/**
 * Created by Zenika on 06/05/2015.
 */
public class SG_Configuration {


    private Object _id;
    private String resourceName;
    private String resourcePath;
    private Map<String, String> resourceAttributs;
    private ResourceFonctions resourceFonctions;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Map<String, String> getResourceAttributs() {
        return resourceAttributs;
    }

    public void setResourceAttributs(Map<String, String> resourceAttributs) {
        this.resourceAttributs = resourceAttributs;
    }

    public ResourceFonctions getResourceFonctions() {
        return resourceFonctions;
    }

    public void setResourceFonctions(ResourceFonctions resourceFonctions) {
        this.resourceFonctions = resourceFonctions;
    }
    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(get_id() + "\n");
        stringBuilder.append(getResourceName() + "\n");
        stringBuilder.append(getResourcePath() + "\n");
        stringBuilder.append(getResourceAttributs() + "\n");
        stringBuilder.append(getResourceFonctions() + "\n");
        return stringBuilder.toString();
    }

}