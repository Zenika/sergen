package src.main.java;

import com.zenika.sergen.components.sergen_annotations.SGComponentMethodAnnotation;

public class Math {

    @SGComponentMethodAnnotation(businessName="Multiplcation")
    public float multiply(float a, float b) {
        return a * b;
    }

    public int test(int z) {
        return 10 + z;
    }
}