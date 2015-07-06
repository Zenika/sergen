package com.zenika.sergen.components.pojo;

import lombok.Data;

/**
 * Created by Zenika on 22/06/2015.
 */
@Data
public class SGComponentMethodParams {
    public Class<?> type; //e.g.  "String", ...
    //public String name;  On verra si c'est vraiment utile ou pas.

}
