package dev.relism.Zyra.exceptions;

public class ZyraAnnotationMissingException extends ZyraException {

    public ZyraAnnotationMissingException(Class<?> clazz) {
        super("Class '%s' isn't @Zyra annotated.".formatted(clazz.getName()));
    }
}
