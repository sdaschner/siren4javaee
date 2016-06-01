/*
 * Copyright (C) 2016 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
