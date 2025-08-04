package dev.relism.Zyra.typescript.mappers;

import dev.relism.Zyra.typescript.TypeScriptTypeMapper;
import dev.relism.Zyra.typescript.TypeScriptTypeResolver;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;

public class DateMapper implements TypeScriptTypeMapper {

    @Override
    public boolean supports(Type type) {
        if (!(type instanceof Class<?> clazz)) return false;
        return clazz == Date.class ||
                clazz == Instant.class ||
                clazz == LocalDate.class ||
                clazz == OffsetDateTime.class;
    }

    @Override
    public String toTypeScript(Type type, TypeScriptTypeResolver resolver) {
        return "string";
    }
}

