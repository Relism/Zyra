package dev.relism.Zyra.zod.handlers;

import dev.relism.Zyra.annotations.zod.ZodString;
import dev.relism.Zyra.zod.ZodConstraint;
import dev.relism.Zyra.zod.ZodField;
import dev.relism.Zyra.zod.ZodTypeHandler;
import dev.relism.Zyra.zod.constraints.EmailConstraint;
import dev.relism.Zyra.zod.constraints.MaxLengthConstraint;
import dev.relism.Zyra.zod.constraints.MinLengthConstraint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ZodStringHandler implements ZodTypeHandler {

    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(ZodString.class);
    }

    @Override
    public ZodField parse(Field field) {
        ZodString annotation = field.getAnnotation(ZodString.class);
        List<ZodConstraint> constraints = new ArrayList<>();

        if (annotation.min() >= 0) {
            constraints.add(new MinLengthConstraint(annotation.min()));
        }
        if (annotation.max() >= 0) {
            constraints.add(new MaxLengthConstraint(annotation.max()));
        }
        if (annotation.email()) {
            constraints.add(new EmailConstraint());
        }

        return new ZodField(
                field.getName(),
                "string()",
                constraints,
                annotation.optional()
        );
    }
}
