package com.zenika.sergen.configuration.pojo;


import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 06/05/2015.
 */
@Data
public class SGResource {

    private Object _id;
    private String name;
    private String path;
    private ArrayList<SGMethod> methods;
}
