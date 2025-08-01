package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class MinLengthConstraint implements ZodConstraint {
    private final int min;

    public MinLengthConstraint(int min) {
        this.min = min;
    }

    @Override
    public String toZodString() {
        return "min(" + min + ")";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        return new JsonObject().add("minLength", min);
    }
}
