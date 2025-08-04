package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;

import java.util.*;
import java.util.stream.Collectors;

public record ZyraParsedObject(
        String name,
        List<ZyraParsedField> fields,
        Zyra config
) implements ZyraParsedDefinition {

    public String getImportStatement(Map<Class<?>, ZyraParsedDefinition> knownDefs) {
        Map<String, Zyra.Export> dependencies = new LinkedHashMap<>();
        for (ZyraParsedField f : fields) {
            for (Class<?> dep : f.getAllReferencedCustomTypes()) {
                if (dep != null && knownDefs.containsKey(dep)) {
                    ZyraParsedDefinition def = knownDefs.get(dep);
                    if (!def.name().equals(name)) {
                        Zyra config = null;
                        if (def instanceof ZyraParsedObject obj) config = obj.config();
                        Zyra.Export exportType = config != null ? config.export() : Zyra.Export.NAMED;
                        dependencies.put(def.name(), exportType);
                    }
                }
            }
        }
        return ZyraImportManager.getImportStatements(dependencies, name);
    }

    public List<ZyraVirtualTsField> getVirtualFields() {
        if (config == null) return List.of();
        return Arrays.stream(config.virtualFields())
                .map(vf -> new ZyraVirtualTsField(vf.name(), vf.tsType(), vf.optional()))
                .toList();
    }

    public String generateTypeScriptDefinition(Map<Class<?>, ZyraParsedDefinition> knownDefs) {
        int indentSize = config != null ? config.indentSpaces() : 2;
        boolean isInterface = config != null && config.style() == Zyra.DefinitionStyle.INTERFACE;
        Zyra.Export export = config != null ? config.export() : Zyra.Export.NAMED;

        String indent = " ".repeat(indentSize);
        StringBuilder sb = new StringBuilder();

        String imports = getImportStatement(knownDefs);
        if (!imports.isEmpty()) sb.append(imports).append("\n");

        if (export == Zyra.Export.NAMED) sb.append("export ");
        else if (export == Zyra.Export.DEFAULT) sb.append("export default ");

        sb.append(isInterface ? "interface " : "type ")
                .append(name())
                .append(" ")
                .append(isInterface ? "{\n" : "= {\n");

        // Real fields
        for (ZyraParsedField field : fields) {
            sb.append(indent)
                    .append(field.name())
                    .append(field.isOptional() ? "?: " : ": ");

            String type = field.isCustomType()
                    ? Optional.ofNullable(knownDefs.get(field.resolvedClass()))
                    .map(ZyraParsedDefinition::name)
                    .orElse(field.tsType())
                    : field.tsType();

            sb.append(type).append(";\n");
        }

        // Virtual fields
        for (ZyraVirtualTsField vf : getVirtualFields()) {
            sb.append(indent)
                    .append(vf.name)
                    .append(vf.optional ? "?: " : ": ")
                    .append(vf.tsType)
                    .append(";\n");
        }

        sb.append("};");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ZyraParsedObject{name='%s', fields=%s}".formatted(name(), fields);
    }

    private record ZyraVirtualTsField(String name, String tsType, boolean optional) {}
}
