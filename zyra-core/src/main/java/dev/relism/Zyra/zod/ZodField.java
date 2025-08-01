package dev.relism.Zyra.zod;

import com.eclipsesource.json.JsonObject;
import dev.relism.Zyra.zod.constraints.EnumConstraint;

import java.util.List;

public record ZodField(
        String name,
        String baseExpression,
        List<ZodConstraint> constraints,
        boolean optional
) {
    public String toZodString() {
        StringBuilder sb = new StringBuilder("z." + baseExpression);
        for (ZodConstraint c : constraints) {
            sb.append(".").append(c.toZodString());
        }
        if (optional) sb.append(".optional()");
        return sb.toString();
    }

    public JsonObject toJsonSchemaProperty() {
        JsonObject obj = new JsonObject();

        if (baseExpression.startsWith("enum(")) {
            obj.add("type", "string");
            for (ZodConstraint constraint : constraints) {
                if (constraint instanceof EnumConstraint enumConstraint) {
                    JsonObject enumObj = enumConstraint.toJsonSchemaObject();
                    for (String key : enumObj.names()) {
                        obj.add(key, enumObj.get(key));
                    }
                }
            }
        } else {
            obj.add("type", jsonTypeFor(baseExpression));
            for (ZodConstraint constraint : constraints) {
                if (!(constraint instanceof EnumConstraint)) {
                    JsonObject single = constraint.toJsonSchemaObject();
                    for (String key : single.names()) {
                        obj.add(key, single.get(key));
                    }
                }
            }
        }
        return obj;
    }

    private String jsonTypeFor(String zodBaseType) {
        return switch (zodBaseType) {
            case "number()" -> "number";
            case "boolean()" -> "boolean";
            default -> "string";
        };
    }
}




