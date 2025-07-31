package dev.relism.Zyra.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Number {
    double min() default Double.NaN;
    double max() default Double.NaN;
    boolean nonNegative() default false;
    boolean intOnly() default false;
    boolean optional() default false;
}
