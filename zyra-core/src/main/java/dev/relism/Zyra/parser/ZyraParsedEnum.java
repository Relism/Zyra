package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;

import java.util.List;

public record ZyraParsedEnum(
        String name,
        List<String>
        enumValues,
        Zyra config
) implements ZyraParsedDefinition {

    public String generateTypeScriptDefinition() {
        int indentSpaces = config != null ? config.indentSpaces() : 2;
        Zyra.Export export = config != null ? config.export() : Zyra.Export.NAMED;
        String indent = " ".repeat(indentSpaces);

        StringBuilder sb = new StringBuilder();

        sb.append("enum ").append(name).append(" {\n");
        for (int i = 0; i < enumValues.size(); i++) {
            String val = enumValues.get(i);
            sb.append(indent)
                    .append(val)
                    .append(" = \"")
                    .append(val)
                    .append("\"");
            if (i < enumValues.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("}\n\n");

        switch (export) {
            case NAMED -> sb.append("export { ").append(name).append(" };");
            case DEFAULT -> sb.append("export default ").append(name).append(";");
            case NONE -> {}
        }

        return sb.toString();
    }


}
