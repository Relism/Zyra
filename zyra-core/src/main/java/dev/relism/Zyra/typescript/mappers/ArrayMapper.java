package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.Type;

public class ArrayMapper implements TypeScriptTypeMapper {
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> cls && cls.isArray();
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        Class<?> cls = (Class<?>) type;
        return resolver.resolve(cls.getComponentType()) + "[]";
    }
}

