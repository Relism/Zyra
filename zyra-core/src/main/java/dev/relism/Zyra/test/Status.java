package dev.relism.Zyra.test;

import dev.relism.Zyra.annotations.Zyra;

@Zyra(export = Zyra.Export.NAMED, indentSpaces = 4)
public enum Status {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;
}
