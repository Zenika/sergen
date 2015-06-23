package com.zenika.sergen.resourceManager.pojo;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 06/05/2015.
 */
@Data
public class SGResourceMethod {

    private String returnType;
    private String name;
    private String path;
    private String produces;
    private String consumes;
    private String typeOfRequete;
    private ArrayList<SGResourceMethodPathParams> pathParameters;
    private ArrayList<SGWorkflows> workflows;


}
