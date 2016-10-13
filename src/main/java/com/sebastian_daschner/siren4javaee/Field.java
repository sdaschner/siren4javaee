package com.sebastian_daschner.siren4javaee;

/**
 * Represents a Siren action field.
 * This is created by the {@link com.sebastian_daschner.siren4javaee.EntityReader} or the {@link com.sebastian_daschner.siren4javaee.SirenClient}
 * functionality, respectively.
 * Instances are immutable.
 *
 * @author Sebastian Daschner
 */
public class Field extends SirenObject {

    private String name;
    private FieldType type;
    private String value;
    private boolean required;

    private Field() {
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final Field field = (Field) o;

        if (required != field.required) return false;
        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        if (type != field.type) return false;
        return value != null ? value.equals(field.value) : field.value == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", required=" + required +
                ", classes=" + getClasses() +
                ", title=" + getTitle() +
                '}';
    }

    /**
     * Creates new {@link Field}s.
     * Only used by {@link EntityReader}.
     */
    static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder pattern to create new {@link Field}s.
     * Only used by {@link EntityReader}.
     */
    static class Builder extends SirenObject.Builder<Field, Builder> {

        private final Field field = new Field();

        @Override
        protected Field object() {
            return field;
        }

        Builder setName(final String name) {
            object().name = name;
            return this;
        }

        Builder setType(final FieldType type) {
            object().type = type;
            return this;
        }

        Builder setValue(final String value) {
            object().value = value;
            return this;
        }

        Builder setRequired(final boolean required) {
            object().required = required;
            return this;
        }

        protected Field build() {
            return field;
        }

    }

}
