package dev.relism.Zyra.test;

import dev.relism.Zyra.annotations.Zyra;
import lombok.Getter;

@Getter
@Zyra(export = Zyra.Export.DEFAULT, style = Zyra.DefinitionStyle.INTERFACE, indentSpaces = 2)
public class Address {
    private String street;
    private String city;
    private String zipCode;
    private Country country;
}

