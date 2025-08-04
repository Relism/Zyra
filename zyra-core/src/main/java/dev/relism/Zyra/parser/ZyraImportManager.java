package dev.relism.Zyra.parser;

import dev.relism.Zyra.annotations.Zyra;
import java.util.Map;

public class ZyraImportManager {
    public static String getImportStatement(String typeName, Zyra.Export exportType) {
        String fileName = "./" + typeName;
        switch (exportType) {
            case DEFAULT:
                return "import type " + typeName + " from '" + fileName + "';";
            case NAMED:
                return "import type { " + typeName + " } from '" + fileName + "';";
            default:
                return "";
        }
    }

    public static String getImportStatements(Map<String, Zyra.Export> dependencies, String selfType) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Zyra.Export> entry : dependencies.entrySet()) {
            String typeName = entry.getKey();
            Zyra.Export exportType = entry.getValue();
            if (!typeName.equals(selfType)) {
                if (!first) sb.append("\n");
                sb.append(getImportStatement(typeName, exportType));
                first = false;
            }
        }
        if (sb.length() > 0) sb.append("\n"); // Only one blank line after imports if any
        return sb.toString();
    }
}
