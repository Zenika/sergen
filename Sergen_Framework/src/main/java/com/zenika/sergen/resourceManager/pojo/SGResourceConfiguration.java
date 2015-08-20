package com.zenika.sergen.resourceManager.pojo;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zenika on 06/05/2015.
 */
@Data

public class SGResourceConfiguration {

    private Object _id;
    private String name;
    private String path;

    private List<SGResourceMethod> methods;
}
