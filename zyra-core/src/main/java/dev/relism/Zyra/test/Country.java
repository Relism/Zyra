package dev.relism.Zyra.test;

import dev.relism.Zyra.annotations.Zyra;
import lombok.Getter;

@Getter
@Zyra(export = Zyra.Export.DEFAULT, style = Zyra.DefinitionStyle.INTERFACE, indentSpaces = 2)
public class Country {
    private String name;
    private String isoCode;
    private int population;
}
