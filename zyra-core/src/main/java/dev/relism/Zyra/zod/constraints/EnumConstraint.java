package dev.relism.Zyra.zod.constraints;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.ZodConstraint;

import java.util.List;

public class EnumConstraint implements ZodConstraint {

    private final List<String> values;

    public EnumConstraint(List<String> values) {
        this.values = values;
    }

    @Override
    public String toZodString() {
        return "";
    }

    @Override
    public JsonObject toJsonSchemaObject() {
        JsonArray arr = new JsonArray();
        values.forEach(arr::add);
        JsonObject obj = new JsonObject();
        obj.add("enum", arr);
        return obj;
    }
}
