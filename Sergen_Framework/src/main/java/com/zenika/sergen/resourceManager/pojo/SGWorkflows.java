package com.zenika.sergen.resourceManager.pojo;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 20/05/2015.
 */
@Data
public class SGWorkflows {
    private String componentName;
    private String method;
    private ArrayList<String> parameters;
}
