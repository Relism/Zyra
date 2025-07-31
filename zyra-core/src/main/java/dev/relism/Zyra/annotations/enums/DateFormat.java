package dev.relism.Zyra.annotations.enums;

import lombok.Getter;

@Getter
public enum DateFormat {
    ISO("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    US("MM/dd/yyyy"),
    EU("dd/MM/yyyy"),
    NULL("");

    private final String pattern;

    DateFormat(String pattern) {
        this.pattern = pattern;
    }

}
