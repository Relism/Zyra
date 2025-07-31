package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.ZyraOptional;
import dev.relism.Zyra.constants.TypeScriptTypeMapper;

import java.lang.reflect.*;
import java.util.Arrays;

public record ZyraParsedField(
        Field field
) {

    public String name() {
        return field.getName();
    }

    public Type javaType() {
        return field.getGenericType();
    }

    public String tsType() {
        return TypeScriptTypeMapper.toTypeScriptType(javaType());
    }

    public Class<?> resolvedClass() {
        Type type = javaType();
        if (type instanceof Class<?> cls) {
            return cls;
        }
        if (type instanceof ParameterizedType pt) {
            Type raw = pt.getRawType();
            if (raw instanceof Class<?> cls) {
                return cls;
            }
        }
        return null;
    }

    public boolean isPrimitiveOrBuiltin() {
        String ts = tsType();
        return ts.equals("string") || ts.equals("number") || ts.equals("boolean") || ts.equals("any");
    }

    public boolean isCustomType() {
        Class<?> resolved = resolvedClass();
        if (resolved == null) return false;
        String ts = tsType();
        return !isPrimitiveOrBuiltin()
                && !ts.endsWith("[]")
                && !ts.startsWith("{ [key:");
    }

    public boolean isOptional() {
        boolean isOptionalGeneric = false;
        Type type = javaType();
        if (type instanceof ParameterizedType pt) {
            Type rawType = pt.getRawType();
            if (rawType instanceof Class<?> rawClass) {
                isOptionalGeneric = rawClass.getSimpleName().equals("Optional");
            }
        }

        boolean isZyraOptional = field.isAnnotationPresent(ZyraOptional.class);

        boolean isNullable = Arrays.stream(field.getAnnotations())
                .anyMatch(ann -> ann.annotationType().getSimpleName().equals("Nullable"));

        return isOptionalGeneric || isZyraOptional || isNullable;
    }
}
