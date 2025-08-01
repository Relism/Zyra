package dev.relism.Zyra.annotations.zod;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZodEnum {
    boolean optional() default false;
}
