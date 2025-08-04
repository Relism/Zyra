package dev.relism.Zyra.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Zyra {
    Export export() default Export.NAMED;
    DefinitionStyle style() default DefinitionStyle.TYPE;
    int indentSpaces() default 2;
    String name() default "";

    enum Export {
        NAMED,
        DEFAULT,
        NONE
    }

    enum DefinitionStyle {
        TYPE,
        INTERFACE
    }
}
