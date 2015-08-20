package com.zenika.sergen.ComponentJarFiles.mathematicsJar;

import com.zenika.sergen.components.SG_COMPONENT_TYPE;
import com.zenika.sergen.components.sergen_annotations.SGComponentClassAnnotation;
import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

/**
 * Created by matekordial on 21/07/2015.
 */
@SGComponentClassAnnotation(name = "Return 10", version = "1.0", description = "this class should return 10", componentType = SG_COMPONENT_TYPE.BUSINESS)
public class ReturnDix {
    @SGComponentMethodAnnotation(businessName = "ReturningDixOperation")
    public int getDix() {
        return 10;
    }
}
