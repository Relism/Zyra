package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.Type;

public class CustomTypeMapper implements TypeScriptTypeMapper {
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> cls && cls.isAnnotationPresent(Zyra.class);
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        return ((Class<?>) type).getSimpleName();
    }
}

