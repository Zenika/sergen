package com.zenika.sergen.components.pojo;

import java.util.ArrayList;

/**
 * Created by Zenika on 22/06/2015.
 * <p/>
 * example :
 *
 * @SGComponentMethod(businessName="Multiplication") public float multiply(float a, float b) { //"multiply" => technicalName
 * return a * b;
 * }
 * <p/>
 * inputs = [{type: "float", name:"a"}, {type:"float", name:"b"}]
 * output = float
 * technicalName = "multiply"
 * businessName = "Multiplication"
 */
public class SGComponentMethod {
    public ArrayList<SGComponentMethodParams> inputs;
    public Object output;

    //technical name for the method
    public String technicalName;

    //Business name for the method
    public String businessName;
}
