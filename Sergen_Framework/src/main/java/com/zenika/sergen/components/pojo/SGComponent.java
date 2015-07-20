package com.zenika.sergen.components.pojo;

import com.zenika.sergen.components.SG_COMPONENT_TYPE;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 10/06/2015.
 */
@Data
public class SGComponent {
    public String name;
    public String componentPackage;
    public String description;
    public String version;
    public SG_COMPONENT_TYPE type = SG_COMPONENT_TYPE.BUSINESS;

    public ArrayList<SGComponentMethod> sgComponentMethods;
}
