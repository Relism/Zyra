package dev.relism.Zyra.generator;

import dev.relism.Zyra.parser.*;

import java.util.*;

public class TypeScriptGenerator {

    private final Map<Class<?>, ZyraParsedDefinition> parsedDefinitions = new HashMap<>();

    public void load(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            parseRecursive(clazz);
        }
    }

    private void parseRecursive(Class<?> clazz) {
        if (parsedDefinitions.containsKey(clazz)) return;

        if (clazz.isEnum()) {
            parsedDefinitions.put(clazz, Parser.parseEnum(clazz));
            return;
        }

        ZyraParsedObject obj = Parser.parseObject(clazz);
        parsedDefinitions.put(clazz, obj);

        for (ZyraParsedField field : obj.fields()) {
            if (!field.isCustomType()) continue;

            Class<?> dep = field.resolvedClass();
            if (dep != null && !parsedDefinitions.containsKey(dep)) {
                parseRecursive(dep);
            }
        }
    }

    public List<GeneratedFile> generate() {
        List<ZyraParsedObject> orderedObjects = sortByDependencyOrder();
        List<GeneratedFile> files = new ArrayList<>();

        for (ZyraParsedDefinition def : parsedDefinitions.values()) {
            if (def instanceof ZyraParsedEnum en) {
                files.add(new GeneratedFile(en.fileName(), en.generateTypeScriptDefinition()));
            }
        }

        for (ZyraParsedObject obj : orderedObjects) {
            files.add(new GeneratedFile(obj.fileName(), obj.generateTypeScriptDefinition(parsedDefinitions)));
        }

        return files;
    }

    private List<ZyraParsedObject> sortByDependencyOrder() {
        List<ZyraParsedObject> result = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();

        for (Class<?> clazz : parsedDefinitions.keySet()) {
            ZyraParsedDefinition def = parsedDefinitions.get(clazz);
            if (def instanceof ZyraParsedObject) {
                visit(clazz, visited, result);
            }
        }

        return result;
    }

    private void visit(Class<?> clazz, Set<Class<?>> visited, List<ZyraParsedObject> result) {
        if (visited.contains(clazz)) return;
        visited.add(clazz);

        ZyraParsedDefinition def = parsedDefinitions.get(clazz);
        if (!(def instanceof ZyraParsedObject obj)) return;

        for (ZyraParsedField field : obj.fields()) {
            if (!field.isCustomType()) continue;

            Class<?> dep = field.resolvedClass();
            if (dep != null && parsedDefinitions.get(dep) instanceof ZyraParsedObject) {
                visit(dep, visited, result);
            }
        }

        result.add(obj);
    }

    public record GeneratedFile(String filename, String content) {}
}
