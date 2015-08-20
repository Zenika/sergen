package com.zenika.sergen.resourceManager.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private String typeOfRequest;

    private List<SGResourceMethodPathParams> pathParameters;

    private List<SGWorkflows> workflows;


}
