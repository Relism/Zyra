package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;

import java.util.List;

public record ZyraParsedObject(
        String name,
        List<ZyraParsedField> fields,
        Zyra config
) implements ZyraParsedType {
    public List<String> getRequiredImports() {
        return fields.stream()
                .filter(ZyraParsedField::isCustomType)
                .map(ZyraParsedField::tsType)
                .distinct()
                .toList();
    }

    public String getImportStatement() {
        List<String> types = fields.stream()
                .filter(ZyraParsedField::isCustomType)
                .map(ZyraParsedField::tsType)
                .distinct()
                .sorted()
                .toList();

        if (types.isEmpty()) return "";

        String joined = String.join(", ", types);
        return "import { " + joined + " } from './';";
    }

    @Override
    public String generateTypeScriptDefinition() {
        int indentSpaces = config != null ? config.indentSpaces() : 2;
        boolean useInterface = config != null && config.style() == Zyra.DefinitionStyle.INTERFACE;
        Zyra.Export export = config != null ? config.export() : Zyra.Export.NAMED;

        String indent = " ".repeat(indentSpaces);
        StringBuilder sb = new StringBuilder();

        String importStatement = getImportStatement();
        if (!importStatement.isEmpty()) {
            sb.append(importStatement).append("\n\n");
        }

        switch (export) {
            case NAMED -> sb.append("export ");
            case DEFAULT -> sb.append("export default ");
            case NONE -> {}
        }

        if (useInterface) {
            sb.append("interface ").append(name).append(" {\n");
        } else {
            sb.append("type ").append(name).append(" = {\n");
        }

        for (ZyraParsedField field : fields) {
            sb.append(indent)
                    .append(field.name())
                    .append(field.isOptional() ? "?: " : ": ")
                    .append(field.tsType())
                    .append(";\n");
        }

        sb.append("};");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ZyraParsedObject{name='%s', fields=%s}".formatted(
                name,
                fields
        );
    }
}
