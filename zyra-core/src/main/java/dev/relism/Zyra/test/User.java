package dev.relism.Zyra.test;

import dev.relism.Zyra.annotations.Zyra;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Zyra(export = Zyra.Export.DEFAULT, style = Zyra.DefinitionStyle.INTERFACE, indentSpaces = 2)
public class User {
    private String username;
    private int age;
    private boolean active;

    private Address address;

    private List<String> roles;
    private List<User> friends;

    private Map<String, Integer> preferences;

    private Status status;
}
