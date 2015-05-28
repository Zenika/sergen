package com.zenika.sergen.configuration.pojo;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 20/05/2015.
 */
@Data
public class SGWorkflows {
    private boolean withReturn;
    private String classPath;
    private String methode;
    private ArrayList<String> parameters;
}
