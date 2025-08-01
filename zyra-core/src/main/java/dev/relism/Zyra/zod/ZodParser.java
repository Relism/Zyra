package dev.relism.Zyra.zod;

import dev.relism.Zyra.exceptions.ZyraParsingException;
import dev.relism.Zyra.zod.handlers.ZodBooleanHandler;
import dev.relism.Zyra.zod.handlers.ZodEnumHandler;
import dev.relism.Zyra.zod.handlers.ZodNumberHandler;
import dev.relism.Zyra.zod.handlers.ZodStringHandler;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class ZodParser {
    private static final List<ZodTypeHandler> handlers = List.of(
            new ZodStringHandler(),
            new ZodNumberHandler(),
            new ZodBooleanHandler(),
            new ZodEnumHandler()
    );

    public static ZyraZodSchema generateZodSchema(Class<?> clazz) {
        List<ZodField> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .filter(f -> handlers.stream().anyMatch(h -> h.supports(f)))
                .map(f -> handlers.stream()
                        .filter(h -> h.supports(f))
                        .findFirst()
                        .map(h -> h.parse(f))
                        .orElseThrow(() -> new ZyraParsingException("Handler missing for field: " + f.getName())))
                .toList();

        return new ZyraZodSchema(clazz.getSimpleName(), fields);
    }

}

