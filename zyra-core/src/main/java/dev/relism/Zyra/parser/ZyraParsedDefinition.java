package dev.relism.Zyra.parser;

public interface ZyraParsedDefinition {
    String name();
    default String fileName() {
        return name() + ".ts";
    }
}

