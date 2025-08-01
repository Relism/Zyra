package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class MaxLengthConstraint implements ZodConstraint {
    private final int max;

    public MaxLengthConstraint(int max) {
        this.max = max;
    }

    @Override
    public String toZodString() {
        return "max(" + max + ")";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        return new JsonObject().add("maxLength", max);
    }
}
