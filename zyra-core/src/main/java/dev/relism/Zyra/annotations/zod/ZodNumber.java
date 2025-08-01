package dev.relism.Zyra.annotations.zod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZodNumber {
    double min() default Double.NaN;
    double max() default Double.NaN;
    boolean intOnly() default false;
    boolean positive() default false;
    boolean negative() default false;
    boolean optional() default false;
}
