package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class NegativeNumberConstraint implements ZodConstraint {
    @Override
    public String toZodString() {
        return "negative()";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        return new JsonObject().add("exclusiveMaximum", 0);
    }
}
