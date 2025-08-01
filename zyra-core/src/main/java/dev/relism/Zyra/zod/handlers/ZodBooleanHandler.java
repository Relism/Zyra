package dev.relism.Zyra.zod.handlers;


import dev.relism.Zyra.annotations.zod.ZodBoolean;
import dev.relism.Zyra.zod.ZodField;
import dev.relism.Zyra.zod.ZodTypeHandler;

import java.lang.reflect.Field;
import java.util.List;

public class ZodBooleanHandler implements ZodTypeHandler {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ZodBoolean.class);
    }

    @Override
    public ZodField parse(Field field) {
        ZodBoolean annotation = field.getAnnotation(ZodBoolean.class);

        return new ZodField(
                field.getName(),
                "boolean()",
                List.of(),
                annotation.optional()
        );
    }
}
