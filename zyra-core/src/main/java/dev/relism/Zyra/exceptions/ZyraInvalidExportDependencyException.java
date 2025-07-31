package dev.relism.Zyra.exceptions;

public class ZyraInvalidExportDependencyException extends ZyraException {
    public ZyraInvalidExportDependencyException(String importingClass, String importedClass) {
        super("Class '" + importingClass + "' references '" + importedClass +
                "', which is not exported (Zyra.export() == NONE) or not @Zyra annotated.");
    }
}

