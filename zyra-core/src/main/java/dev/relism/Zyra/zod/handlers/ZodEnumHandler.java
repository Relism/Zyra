package dev.relism.Zyra.zod.handlers;

import dev.relism.Zyra.annotations.zod.ZodEnum;
import dev.relism.Zyra.zod.ZodField;
import dev.relism.Zyra.zod.ZodTypeHandler;
import dev.relism.Zyra.zod.constraints.EnumConstraint;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZodEnumHandler implements ZodTypeHandler {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ZodEnum.class) && field.getType().isEnum();
    }

    @Override
    public ZodField parse(Field field) {
        ZodEnum annotation = field.getAnnotation(ZodEnum.class);

        Class<?> enumType = field.getType();
        Object[] enumConstants = enumType.getEnumConstants();

        if (enumConstants == null) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not a valid enum");
        }

        List<String> values = Stream.of(enumConstants)
                .map(Object::toString)
                .collect(Collectors.toList());

        String enumDefinition = "enum([" +
                values.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", ")) +
                "])";

        return new ZodField(
                field.getName(),
                enumDefinition,
                List.of(new EnumConstraint(values)),
                annotation.optional()
        );
    }
}
