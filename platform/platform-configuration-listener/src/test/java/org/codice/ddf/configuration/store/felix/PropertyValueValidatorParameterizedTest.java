/**
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.configuration.store.felix;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PropertyValueValidatorParameterizedTest {

    @Parameterized.Parameters(name = "line: {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // String Values
                {"latte", "", true}, {"cappuccino", null, true},
                // Boolean Single Values
                {"true", "B", true}, {"false", "B", true},
                {"troo", "B", false}, {"2", "B", false},
                // Integer Single Values
                {"1", "I", true}, {"12", "I", true}, {"123", "I", true},
                {"One", "I", false}, {"1.2", "I", false}, {"true", "I", false},
                // Long Single Values
                {"100", "L", true}, {"200", "L", true}, {"300", "L", true},
                {"1.1", "L", false}, {"Long", "L", false}, {"true", "L", false},
                // Double Single Values
                {"1", "D", true}, {"2.2", "D", true}, {"33.33", "D", true},
                {"One", "D", false}, {"true", "D", false}, {"1.1.1", "D", false},
                // Float Single Values
                {"1.1", "F", true}, {"2.2", "F", true}, {"3.3", "F", true},
                {"one.one", "F", false}, {"true", "F", false}, {"1.1.1", "F", false},
                // Arrays / vectors*/
                {"macchiato,affogato,americano", "", true}, {"true,false", "B", true},
                {"10,20,30", "I", true}, {"100,200,300", "L", true}, {"1,2.2,33.33", "D", true},
                {"1.1,2.2,3.3", "F", true},
                {"true,flase", "B", false}, {"10,20,thirty", "I", false}, {"100,200,threehundred", "L", false},
                {"1,two.two,33.33", "D", false}, {"1.1,true,3.3", "F", false}
        });
    }

    @Parameterized.Parameter
    public String propertyValue;

    @Parameterized.Parameter(1)
    public String propertyType;

    @Parameterized.Parameter(2)
    public boolean expectedResult;

    @Test
    public void testValidation() {
        Boolean result = PropertyValueValidator.isValidSyntax(propertyValue, propertyType);

        assertThat(result, equalTo(expectedResult));
    }

}
