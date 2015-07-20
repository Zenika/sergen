package src.main.java;

import com.zenika.sergen.components.SG_COMPONENT_TYPE;
import com.zenika.sergen.components.sergen_annotations.SGComponentClassAnnotation;
import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

@SGComponentClassAnnotation(name = "Mathematics", description = "Mathematics library", version = "0.9", componentType = SG_COMPONENT_TYPE.BUSINESS)
public class Math {

    @SGComponentMethodAnnotation(businessName = "Multiplication")
    public float multiply(float a, float b) {
        return a * b;
    }

    public int test(int z) {
        return 10 + z;
    }
}