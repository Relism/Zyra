package dev.relism.Zyra.zod;

import java.lang.reflect.Field;

public interface ZodTypeHandler {
    boolean supports(Field field);
    ZodField parse(Field field);
}
