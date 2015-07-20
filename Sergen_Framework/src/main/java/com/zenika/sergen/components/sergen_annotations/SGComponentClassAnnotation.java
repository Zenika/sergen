package com.zenika.sergen.components.sergen_annotations;

import com.zenika.sergen.components.SG_COMPONENT_TYPE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Zenika on 04/06/2015.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SGComponentClassAnnotation {
    String name();

    String description();

    String version();

    SG_COMPONENT_TYPE componentType();

}
