package dev.relism.Zyra.zod;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.WriterConfig;

import java.util.List;

public record ZyraZodSchema(
        String className,
        List<ZodField> fields
) {

    public String zodObjectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("const ").append(className).append("Schema = z.object({\n");

        for (ZodField field : fields) {
            sb.append("  ").append(field.name())
                    .append(": ")
                    .append(field.toZodString())
                    .append(",\n");
        }

        sb.append("});");
        return sb.toString();
    }

    public String zodJsonSchema() {
        JsonObject schema = new JsonObject();
        schema.add("$schema", "http://json-schema.org/draft-07/schema#");
        schema.add("type", "object");

        JsonObject properties = new JsonObject();
        JsonArray required = new JsonArray();

        for (ZodField field : fields) {
            JsonObject prop = field.toJsonSchemaProperty();
            properties.add(field.name(), prop);

            if (!field.optional()) {
                required.add(field.name());
            }
        }

        schema.add("properties", properties);
        schema.add("required", required);

        return schema.toString(WriterConfig.PRETTY_PRINT); // Indenta con 2 spazi
    }
}
