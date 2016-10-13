package com.sebastian_daschner.siren4javaee;

import java.util.stream.Stream;

/**
 * The available field types for Siren actions.
 *
 * @author Sebastian Daschner
 */
public enum FieldType {

    TEXT,
    NUMBER,
    HIDDEN,
    SEARCH,
    TEL,
    URL,
    EMAIL,
    PASSWORD,
    DATETIME,
    DATE,
    MONTH,
    WEEK,
    TIME,
    DATETIME_LOCAL("datetime-local"),
    RANGE,
    COLOR,
    CHECKBOX,
    RADIO,
    FILE;

    private String name;

    FieldType() {
        this.name = name().toLowerCase();
    }

    FieldType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static FieldType fromString(final String string) {
        if (string == null)
            return null;

        return Stream.of(values()).filter(f -> f.toString().equals(string.toLowerCase())).findAny().orElse(null);
    }

}
