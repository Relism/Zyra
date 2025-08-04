package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalMapper implements TypeScriptTypeMapper {
    @Override
    public boolean supports(Type type) {
        return type instanceof ParameterizedType pt &&
                pt.getRawType() instanceof Class<?> raw &&
                Optional.class.isAssignableFrom(raw);
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        ParameterizedType pt = (ParameterizedType) type;
        return resolver.resolve(pt.getActualTypeArguments()[0]);
    }
}

