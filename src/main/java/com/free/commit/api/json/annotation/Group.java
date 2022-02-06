package com.free.commit.api.json.annotation;

import com.free.commit.api.json.overwritter.DefaultOverwrite;
import com.free.commit.api.json.overwritter.Overwrite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Group {
    String name() default "_default";


    boolean onlyId() default true;


    boolean ascent() default false;


    String key() default "";


    Class< ? extends Overwrite< ? > > overwrite() default DefaultOverwrite.class;


    Row[] row() default {};


    boolean forceEncoding() default false;
}
