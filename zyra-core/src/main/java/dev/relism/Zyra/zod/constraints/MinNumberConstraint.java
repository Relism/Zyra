package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

public class MinNumberConstraint implements ZodConstraint {
    private final double min;

    public MinNumberConstraint(double min) {
        this.min = min;
    }

    @Override
    public String toZodString() {
        return "min(" + min + ")";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        JsonObject obj = new JsonObject();
        if (min == (int) min) {
            obj.add("minimum", (int) min);
        } else {
            obj.add("minimum", min);
        }
        return obj;
    }
}
