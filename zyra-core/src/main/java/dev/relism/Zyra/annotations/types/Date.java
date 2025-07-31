package dev.relism.Zyra.annotations.types;

import dev.relism.Zyra.annotations.enums.DateFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {
    boolean optional() default false;
    DateFormat format() default DateFormat.NULL;
}

