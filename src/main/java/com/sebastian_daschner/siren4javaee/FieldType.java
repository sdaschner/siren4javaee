package com.sebastian_daschner.siren4javaee;

/**
 * All available types for Siren action field.
 *
 * @author Sebastian Daschner
 */
public enum FieldType {

    /**
     * Text (i.e. String) type.
     */
    TEXT,

    /**
     * Number type &mdash; either integer or floating point number.
     */
    NUMBER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
