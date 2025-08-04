package dev.relism.Zyra;

import dev.relism.Zyra.annotations.Zyra;

public class ZyraUtils {

    private ZyraUtils() {
        // Prevent instantiation
    }

    public static String normalizeFilename(Class<?> clazz) {
        String className = clazz.getSimpleName();
        String override = clazz.getAnnotation(Zyra.class).name();
        if (override == null || override.isBlank()) {
            return className;
        }

        String trimmed = override.trim();
        String original = trimmed;

        if (trimmed.toLowerCase().endsWith(".ts")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3).trim();
            System.err.printf(
                    "[Zyra] Warning: filenameOverride for class '%s' should not include '.ts'. Auto-corrected to '%s'.%n",
                    className, trimmed
            );
        }

        if (!trimmed.equals(original)) {
            System.err.printf(
                    "[Zyra] Warning: filenameOverride for class '%s' was auto-corrected from '%s' to '%s'. Please fix it for maintainability.%n",
                    className, original, trimmed
            );
        }

        return trimmed.isEmpty() ? className : trimmed;
    }
}
