package com.zenika.sergen.resourceManager.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 20/05/2015.
 */
@Data

public class SGWorkflows {
    private String componentName;
    private String method;

    private List<String> parameters;
}
