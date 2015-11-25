/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.configuration.store.felix;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates syntax for the File Handler
 */
public class PropertyValueValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyValueConverter.class);

    private static final Pattern PATTERN = Pattern.compile(",");

    /**
     * Checks if the given string is valid syntax for the given configuration value type
     * Strings ('') will always return true
     *
     * @param value The value to check
     * @param type  The single letter configuration value type. Only the first character of string is used.
     * @return True if the value is valid syntax for the type given, false otherwise.
     */
    public static boolean isValidSyntax(String value, String type) {
        // Do not check strings for syntax
        if (StringUtils.isBlank(type)) {
            return true;
        }

        LOGGER.debug("Attempting to validate [{}] as type [{}]", value, type);
        return PATTERN.splitAsStream(value).allMatch(getTypeValidator(type));
    }

    /**
     * Returns a predicate for validating type syntax
     *
     * @param type String with single character (b,i,l,d,f) specifying type validator to return
     * @return a predicate to validate the given type
     */
    private static Predicate<String> getTypeValidator(String type) {
        char typeChar = Character.toLowerCase(type.charAt(0));

        switch (typeChar) {
        case 'b':
            return PropertyValueValidator::isBooleanSyntax;
        case 'i':
            return PropertyValueValidator::isIntegerSyntax;
        case 'l':
            return PropertyValueValidator::isLongSyntax;
        case 'd':
            return PropertyValueValidator::isDoubleSyntax;
        case 'f':
            return PropertyValueValidator::isFloatSyntax;
        default:
            LOGGER.error("Value type \"{}\" is invalid", type);
            return (item) -> false;
        }
    }

    /**
     * Checks if every item in the list is valid boolean syntax
     *
     * @param value list of strings to check
     * @return True if all are valid, false if 1 or more are invalid
     */
    private static boolean isBooleanSyntax(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return true;
        }
        LOGGER.error("Unable to parse \"{}\" as type boolean.", value);
        return false;
    }

    /**
     * Checks if every item in the list is valid integer syntax
     *
     * @param value list of strings to check
     * @return True if all are valid, false if 1 or more are invalid
     */
    private static boolean isIntegerSyntax(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to parse \"{}\" as type Integer.", value);
            return false;
        }
    }

    /**
     * Checks if every item in the list is valid long syntax
     *
     * @param value list of strings to check
     * @return True if all are valid, false if 1 or more are invalid
     */
    private static boolean isLongSyntax(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to parse \"{}\" as type Long.", value);
            return false;
        }
    }

    /**
     * Checks if every item in the list is valid double syntax
     *
     * @param value list of strings to check
     * @return True if all are valid, false if 1 or more are invalid
     */
    private static boolean isDoubleSyntax(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to parse \"{}\" as type Double.", value);
            return false;
        }
    }

    /**
     * Checks if every item in the list is valid float syntax
     *
     * @param value list of strings to check
     * @return True if all are valid, false if 1 or more are invalid
     */
    private static boolean isFloatSyntax(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Unable to parse \"{}\" as type Float.", value);
            return false;
        }
    }
}
