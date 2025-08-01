package dev.relism.Zyra.annotations.zod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZodString {
    int min() default -1;
    int max() default -1;
    boolean email() default false;
    boolean optional() default false;
}
