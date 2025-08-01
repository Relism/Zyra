package dev.relism.Zyra.zod;

import com.eclipsesource.json.JsonObject;

public interface ZodConstraint {
    /**
     * String representation of the constraint in Zod format (ex: "minLength(3)")
     */
    String toZodString();

    /**
     * String representation of the constraint in JSON Schema format (ex: "\"minLength\": 3")
     */
    JsonObject toJsonSchemaObject();
}

