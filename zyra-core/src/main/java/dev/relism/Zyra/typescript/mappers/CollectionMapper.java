package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class CollectionMapper implements TypeScriptTypeMapper {
    @Override
    public boolean supports(Type type) {
        return type instanceof ParameterizedType pt &&
                pt.getRawType() instanceof Class<?> raw &&
                Collection.class.isAssignableFrom(raw);
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        ParameterizedType pt = (ParameterizedType) type;
        Type elementType = pt.getActualTypeArguments()[0];
        return resolver.resolve(elementType) + "[]";
    }
}

