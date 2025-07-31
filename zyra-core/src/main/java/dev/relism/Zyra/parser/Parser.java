package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.exceptions.ZyraInvalidExportDependencyException;
import dev.relism.Zyra.exceptions.ZyraAnnotationMissingException;
import dev.relism.Zyra.exceptions.ZyraParsingException;

import java.util.Arrays;
import java.util.List;


public class Parser {

    public static ZyraParsedType parse(Class<?> clazz) {
        if (clazz.isEnum()) {
            return parseEnum(clazz);
        } else {
            return parseObject(clazz);
        }
    }

    public static ZyraParsedEnum parseEnum(Class<?> clazz) {
        if (!clazz.isEnum()) {
            throw new ZyraParsingException("parseEnum() expects an enum class, got: " + clazz.getName());
        }

        Zyra zyraConfig = clazz.getAnnotation(Zyra.class);
        if (zyraConfig == null) {
            throw new ZyraAnnotationMissingException(clazz);
        }

        Object[] constants = clazz.getEnumConstants();
        List<String> values = Arrays.stream(constants)
                .map(Object::toString)
                .toList();

        return new ZyraParsedEnum(clazz.getSimpleName(), values, zyraConfig);
    }

    public static ZyraParsedObject parseObject(Class<?> clazz) {
        if (clazz.isEnum()) {
            throw new ZyraParsingException("parseObject() expects a non-enum class, got enum: " + clazz.getName());
        }

        Zyra zyraConfig = clazz.getAnnotation(Zyra.class);
        if (zyraConfig == null) {
            throw new ZyraAnnotationMissingException(clazz);
        }

        List<ZyraParsedField> parsedFields = FieldParser.parseFields(clazz);

        for (ZyraParsedField field : parsedFields) {
            if (!field.isCustomType()) continue;

            Class<?> dep = field.resolvedClass();
            if (dep == null) {
                throw new ZyraParsingException(String.format(
                        "Resolved class for field '%s' in %s is null (tsType=%s)",
                        field.name(), clazz.getSimpleName(), field.tsType()
                ));
            }

            Zyra annotation = dep.getAnnotation(Zyra.class);
            if (annotation == null || annotation.export() == Zyra.Export.NONE) {
                throw new ZyraInvalidExportDependencyException(clazz.getSimpleName(), dep.getSimpleName());
            }
        }

        return new ZyraParsedObject(clazz.getSimpleName(), parsedFields, zyraConfig);
    }

}
