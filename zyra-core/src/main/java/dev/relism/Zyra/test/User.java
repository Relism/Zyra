package dev.relism.Zyra.test;

import dev.relism.Zyra.annotations.Zyra;
import dev.relism.Zyra.annotations.zod.ZodEnum;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import dev.relism.Zyra.annotations.zod.ZodBoolean;
import dev.relism.Zyra.annotations.zod.ZodNumber;
import dev.relism.Zyra.annotations.zod.ZodString;

@Getter
@Zyra(export = Zyra.Export.DEFAULT, style = Zyra.DefinitionStyle.INTERFACE, indentSpaces = 2)
public class User {

    @ZodString(min = 3, max = 20)
    private String username;

    @ZodNumber(min = 0, max = 120, positive = true, optional = true)
    private int age;

    @ZodBoolean
    private boolean active;

    private Address address; // oggetto custom, sarà validato via ricorsione

    private List<String> roles;

    private List<User> friends;

    private Map<String, Integer> preferences;

    @ZodEnum(optional = true)
    private Status status; // enum
}

