package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapMapper implements TypeScriptTypeMapper {

    @Override
    public boolean supports(Type type) {
        return type instanceof ParameterizedType pt &&
                pt.getRawType() instanceof Class<?> raw &&
                Map.class.isAssignableFrom(raw);
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        ParameterizedType pt = (ParameterizedType) type;
        Type keyType = pt.getActualTypeArguments()[0];
        Type valType = pt.getActualTypeArguments()[1];

        String tsKey = resolver.resolve(keyType);
        String tsVal = resolver.resolve(valType);

        if (tsKey.equals("string") || tsKey.equals("number") || tsKey.equals("symbol")) {
            return "{ [key: " + tsKey + "]: " + tsVal + " }";
        } else {
            return "Map<" + tsKey + ", " + tsVal + ">";
        }
    }
}

