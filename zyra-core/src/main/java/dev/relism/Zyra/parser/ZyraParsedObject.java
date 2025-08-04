package dev.relism.Zyra.parser;

import dev.relism.Zyra.ZyraUtils;
import dev.relism.Zyra.annotations.Zyra;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public String getImportStatement(Map<Class<?>, ZyraParsedDefinition> knownDefinitions) {
        List<String> types = fields.stream()
                .flatMap(f -> f.getAllReferencedCustomTypes().stream())
                .filter(Objects::nonNull)
                .filter(knownDefinitions::containsKey)
                .map(cls -> knownDefinitions.get(cls).name())
                .filter(importName -> !importName.equals(name)) // ignore auto imports
                .distinct()
                .sorted()
                .toList();

        if (types.isEmpty()) return "";

        return "import { " + String.join(", ", types) + " } from './';";
    }


    public String generateTypeScriptDefinition(Map<Class<?>, ZyraParsedDefinition> knownDefinitions) {
        int indentSpaces = config != null ? config.indentSpaces() : 2;
        boolean useInterface = config != null && config.style() == Zyra.DefinitionStyle.INTERFACE;
        Zyra.Export export = config != null ? config.export() : Zyra.Export.NAMED;

        String indent = " ".repeat(indentSpaces);
        StringBuilder sb = new StringBuilder();

        String importStatement = getImportStatement(knownDefinitions);
        if (!importStatement.isEmpty()) {
            sb.append(importStatement).append("\n\n");
        }

        switch (export) {
            case NAMED -> sb.append("export ");
            case DEFAULT -> sb.append("export default ");
            case NONE -> {}
        }

        if (useInterface) {
            sb.append("interface ").append(name()).append(" {\n");
        } else {
            sb.append("type ").append(name()).append(" = {\n");
        }

        for (ZyraParsedField field : fields) {
            sb.append(indent)
                    .append(field.name())
                    .append(field.isOptional() ? "?: " : ": ");

            if (field.isCustomType()) {
                Class<?> dep = field.resolvedClass();
                String depName = dep != null && knownDefinitions.containsKey(dep)
                        ? knownDefinitions.get(dep).name()
                        : field.tsType(); // fallback
                sb.append(depName);
            } else {
                sb.append(field.tsType());
            }

            sb.append(";\n");
        }

        sb.append("};");
        return sb.toString();
    }


    @Override
    public String toString() {
        return "ZyraParsedObject{name='%s', fields=%s}".formatted(
                name(),
                fields
        );
    }
}
