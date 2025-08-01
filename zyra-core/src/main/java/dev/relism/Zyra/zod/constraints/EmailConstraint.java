package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class EmailConstraint implements ZodConstraint {
    @Override
    public String toZodString() {
        return "email()";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        return new JsonObject().add("format", "email");
    }
}
