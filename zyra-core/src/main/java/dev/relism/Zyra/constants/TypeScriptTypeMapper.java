package dev.relism.Zyra.constants;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TypeScriptTypeMapper {

    private static final Map<Class<?>, String> PRIMITIVE_MAPPINGS = Map.ofEntries(
            Map.entry(String.class, "string"),
            Map.entry(char.class, "string"),
            Map.entry(Character.class, "string"),
            Map.entry(LocalDate.class, "string"),
            Map.entry(LocalDateTime.class, "string"),
            Map.entry(Instant.class, "string"),
            Map.entry(UUID.class, "string"),

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
            Map.entry(BigDecimal.class, "number"),
            Map.entry(BigInteger.class, "number"),
            Map.entry(Number.class, "number"),

            Map.entry(Date.class, "Date"),

            Map.entry(boolean.class, "boolean"),
            Map.entry(Boolean.class, "boolean")
    );

    public static String toTypeScriptType(Type type) {
        if (type instanceof Class<?> clazz) {
            if (PRIMITIVE_MAPPINGS.containsKey(clazz)) {
                return PRIMITIVE_MAPPINGS.get(clazz);
            }

            if (clazz.isArray()) {
                String elementType = toTypeScriptType(clazz.getComponentType());
                return elementType + "[]";
            }

            return clazz.getSimpleName();
        }

        if (type instanceof ParameterizedType pt) {
            Class<?> rawType = (Class<?>) pt.getRawType();
            Type[] typeArgs = pt.getActualTypeArguments();

            if (Collection.class.isAssignableFrom(rawType)) {
                Type elementType = typeArgs[0];
                return toTypeScriptType(elementType) + "[]";
            }

            if (Map.class.isAssignableFrom(rawType)) {
                Type keyType = typeArgs[0];
                Type valueType = typeArgs[1];
                String tsKey = toTypeScriptKeyType(keyType);
                String tsVal = toTypeScriptType(valueType);
                return "{ [key: " + tsKey + "]: " + tsVal + " }";
            }
            return rawType.getSimpleName();
        }

        // Fallback
        return "any";
    }

    private static String toTypeScriptKeyType(Type keyType) {
        String tsType = toTypeScriptType(keyType);
        return switch (tsType) {
            case "string", "number" -> tsType;
            default -> "string";
        };
    }

}

