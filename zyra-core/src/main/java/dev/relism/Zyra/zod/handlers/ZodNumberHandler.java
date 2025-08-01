package dev.relism.Zyra.zod.handlers;

import dev.relism.Zyra.annotations.zod.ZodNumber;
import dev.relism.Zyra.zod.ZodConstraint;
import dev.relism.Zyra.zod.ZodField;
import dev.relism.Zyra.zod.ZodTypeHandler;
import dev.relism.Zyra.zod.constraints.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ZodNumberHandler implements ZodTypeHandler {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ZodNumber.class);
    }

    @Override
    public ZodField parse(Field field) {
        ZodNumber annotation = field.getAnnotation(ZodNumber.class);
        List<ZodConstraint> constraints = new ArrayList<>();

        if (!Double.isNaN(annotation.min())) {
            constraints.add(new MinNumberConstraint(annotation.min()));
        }

        if (!Double.isNaN(annotation.max())) {
            constraints.add(new MaxNumberConstraint(annotation.max()));
        }

        if (annotation.positive()) {
            constraints.add(new PositiveNumberConstraint());
        }

        if (annotation.negative()) {
            constraints.add(new NegativeNumberConstraint());
        }

        return new ZodField(
                field.getName(),
                "number()",
                constraints,
                annotation.optional()
        );
    }
}

