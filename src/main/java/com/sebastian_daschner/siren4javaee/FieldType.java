package com.sebastian_daschner.siren4javaee;

/**
 * All available types for Siren action field.
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

}
