package dev.relism.Zyra.typescript;

import java.lang.reflect.Type;

public interface TypeScriptTypeMapper {
    boolean supports(Type type);
    String toTypeScript(Type type, TypeScriptTypeResolver resolver);
}

