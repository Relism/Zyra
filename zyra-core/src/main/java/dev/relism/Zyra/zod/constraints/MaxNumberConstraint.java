package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class MaxNumberConstraint implements ZodConstraint {
    private final double max;

    public MaxNumberConstraint(double max) {
        this.max = max;
    }

    @Override
    public String toZodString() {
        return "max(" + max + ")";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        JsonObject obj = new JsonObject();
        if (max == (int) max) {
            obj.add("maximum", (int) max);
        } else {
            obj.add("maximum", max);
        }
        return obj;
    }
}

