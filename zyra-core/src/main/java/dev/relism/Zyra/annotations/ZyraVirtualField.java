package dev.relism.Zyra.annotations;

public @interface ZyraVirtualField {
    String name();
    String tsType(); // es: "string", "number", "MyCustomType[]", ecc.
    boolean optional() default false;
}
