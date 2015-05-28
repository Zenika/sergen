package com.zenika.sergen.configuration.pojo;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Zenika on 06/05/2015.
 */
@Data
public class SGMethod {

    private String returnType;
    private String name;
    private String path;
    private String produces;
    private String consumes;
    private String typeOfRequete;
    private ArrayList<SGMethodePathParams> pathParameters;
    private ArrayList<SGWorkflows> workflows;




}
