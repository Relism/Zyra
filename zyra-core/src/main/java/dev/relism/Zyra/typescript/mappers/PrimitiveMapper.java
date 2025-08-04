package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.Type;
import java.util.Map;

public class PrimitiveMapper implements TypeScriptTypeMapper {

    private static final Map<Class<?>, String> PRIMITIVES = Map.ofEntries(
            Map.entry(String.class, "string"),
            Map.entry(char.class, "string"),
            Map.entry(Character.class, "string"),
            Map.entry(int.class, "number"),
            Map.entry(Integer.class, "number"),
            Map.entry(long.class, "number"),
            Map.entry(Long.class, "number"),
            Map.entry(float.class, "number"),
            Map.entry(Float.class, "number"),
            Map.entry(double.class, "number"),
            Map.entry(Double.class, "number"),
            Map.entry(short.class, "number"),
            Map.entry(Short.class, "number"),
            Map.entry(byte.class, "number"),
            Map.entry(Byte.class, "number"),
            Map.entry(boolean.class, "boolean"),
            Map.entry(Boolean.class, "boolean")
    );

    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> cls && PRIMITIVES.containsKey(cls);
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        return PRIMITIVES.get((Class<?>) type);
    }
}

