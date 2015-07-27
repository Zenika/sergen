package com.zenika.sergen.ComponentJarFiles.mathematicsJar;

import com.zenika.sergen.components.SG_COMPONENT_TYPE;
import com.zenika.sergen.components.sergen_annotations.SGComponentClassAnnotation;
import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

/**
 * Created by matekordial on 21/07/2015.
 */
@SGComponentClassAnnotation(name = "Return 20", version = "1.0", description = "this class should return 10", componentType = SG_COMPONENT_TYPE.BUSINESS)
public class ReturnVingt {
    @SGComponentMethodAnnotation(businessName = "ReturningVingtOperation")
    public int getVingt() {
    return 20;
    }
}
