package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.annotations.ZyraVirtualField;

import java.util.*;
import java.util.stream.Collectors;

public record ZyraParsedObject(
        String name,
        List<ZyraParsedField> fields,
        Zyra config
) implements ZyraParsedDefinition {

    public List<String> getRequiredImports() {
        return fields.stream()
                .flatMap(f -> f.getAllReferencedCustomTypes().stream())
                .map(Class::getSimpleName)
                .distinct()
                .sorted()
                .toList();
    }

    public String getImportStatement(Map<Class<?>, ZyraParsedDefinition> knownDefs) {
        return fields.stream()
                .flatMap(f -> f.getAllReferencedCustomTypes().stream())
                .filter(Objects::nonNull)
                .map(knownDefs::get)
                .filter(Objects::nonNull)
                .map(ZyraParsedDefinition::name)
                .filter(n -> !n.equals(name)) // avoid self-imports
                .distinct()
                .sorted()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        types -> types.isEmpty()
                                ? ""
                                : "import { " + String.join(", ", types) + " } from './';"
                ));
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
        if (!imports.isEmpty()) sb.append(imports).append("\n\n");

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
