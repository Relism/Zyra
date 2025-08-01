package dev.relism.Zyra.zod.constraints;


import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class PositiveNumberConstraint implements ZodConstraint {
    @Override
    public String toZodString() {
        return "positive()";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        return new JsonObject().add("exclusiveMinimum", 0);
    }
}
