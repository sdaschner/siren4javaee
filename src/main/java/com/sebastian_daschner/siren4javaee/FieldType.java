package com.sebastian_daschner.siren4javaee;

/**
 * All available field types.
 *
 * @author Sebastian Daschner
 */
public enum FieldType {

    TEXT,
    NUMBER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
