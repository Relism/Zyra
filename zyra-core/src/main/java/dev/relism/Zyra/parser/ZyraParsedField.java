package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.annotations.ZyraOptional;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public record ZyraParsedField(
        Field field
) {

    public String name() {
        return field.getName();
    }

    public Type javaType() {
        return field.getGenericType();
    }

    private static final TypeScriptTypeResolver tsResolver = new TypeScriptTypeResolver();

    public String tsType() {
        return tsResolver.resolve(javaType());
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
        return resolved != null && resolved.isAnnotationPresent(Zyra.class);
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

    public Set<Class<?>> getAllReferencedCustomTypes() {
        Set<Class<?>> result = new HashSet<>();
        collectCustomTypes(javaType(), result);
        return result;
    }

    private void collectCustomTypes(Type type, Set<Class<?>> result) {
        if (type instanceof Class<?> cls) {
            if (isValidCustomType(cls)) result.add(cls);
        } else if (type instanceof ParameterizedType pt) {
            Type raw = pt.getRawType();
            if (raw instanceof Class<?> rawCls && isValidCustomType(rawCls)) {
                result.add(rawCls);
            }

            for (Type arg : pt.getActualTypeArguments()) {
                if (arg instanceof Class<?> argCls && isValidCustomType(argCls)) {
                    result.add(argCls);
                } else {
                    collectCustomTypes(arg, result);
                }
            }
        } else if (type instanceof GenericArrayType gat) {
            collectCustomTypes(gat.getGenericComponentType(), result);
        }
    }


    private boolean isValidCustomType(Class<?> cls) {
        return !cls.isPrimitive() && cls.isAnnotationPresent(Zyra.class);
    }


}
