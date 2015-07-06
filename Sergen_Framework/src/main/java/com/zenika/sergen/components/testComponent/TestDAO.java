package com.zenika.sergen.components.testComponent;

import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;
import com.zenika.sergen.components.sergen_annotations.SGPackage;

/**
 * Created by Zenika on 29/06/2015.
 */
@SGPackage("com.zenika.sergen.components.testComponent")
public class TestDAO {
    private String description = "description of the component";
    private double version = 0.9;
    @SGComponentMethodAnnotation(businessName = "giveMeCount")
    public int giveMeCount() {
        return 10;
    }
}
