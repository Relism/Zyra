package dev.relism.Zyra.typescript;

import dev.relism.Zyra.typescript.mappers.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class TypeScriptTypeResolver {

    private final List<TypeScriptTypeMapper> mappers = new ArrayList<>();

    public TypeScriptTypeResolver() {
        ServiceLoader.load(TypeScriptTypeMapper.class).forEach(mappers::add);

        mappers.addAll(List.of(
                new PrimitiveMapper(),
                new ArrayMapper(),
                new CollectionMapper(),
                new MapMapper(),
                new OptionalMapper(),
                new CustomTypeMapper()
        ));
    }

    public String resolve(Type type) {
        for (TypeScriptTypeMapper mapper : mappers) {
            if (mapper.supports(type)) {
                return mapper.toTypeScript(type, this);
            }
        }
        return "any";
    }
}

