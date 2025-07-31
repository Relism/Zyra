package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.ZyraIgnore;

import java.util.Arrays;
import java.util.List;

public class FieldParser {

    public static List<ZyraParsedField> parseFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isSynthetic() && !f.isAnnotationPresent(ZyraIgnore.class))
                .map(ZyraParsedField::new)
                .toList();
    }
}


