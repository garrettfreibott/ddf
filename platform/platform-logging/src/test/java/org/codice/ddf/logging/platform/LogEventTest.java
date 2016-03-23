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
package org.codice.ddf.logging.platform;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class LogEventTest {

    private static final String LEVEL = "INFO";

    private static final String BUNDLE_VERSION = "1.2.3";

    private static final String MESSAGE_1 = "message 1";

    private static final String MESSAGE_2 = "message 2";

    private static final String BUNDLE_NAME_1 = "my-bundle-name-1";

    private static final String BUNDLE_NAME_2 = "my-bundle-name-2";

    @Test
    public void testEqualsLogEventsSameFieldValues() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        assertThat(logEvent.equals(anotherLogEvent), is(true));
    }

    @Test
    public void testEqualsLogEventsDifferentReferences() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(2L, LEVEL, MESSAGE_2, BUNDLE_NAME_2, BUNDLE_VERSION);
        assertThat(logEvent.equals(anotherLogEvent), is(false));
    }

    @Test
    public void testEqualsLogEventsSameReference() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        assertThat(logEvent.equals(logEvent), is(true));
    }

    @Test
    public void testEqualsOtherLogEventNotInstanceOfLogEvent() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        String anotherLogEvent = "logEvent";
        assertThat(logEvent.equals(anotherLogEvent), is(false));
    }

    @Test
    public void testCompareToLogEventsHaveSameTimestamp() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(1L, LEVEL, MESSAGE_2, BUNDLE_NAME_2, BUNDLE_VERSION);
        assertThat(logEvent.compareTo(anotherLogEvent), is(0));
    }

    @Test
    public void testCompareToAnotherLogEventHasBiggerTimestamp() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(2L, LEVEL, MESSAGE_2, BUNDLE_NAME_2, BUNDLE_VERSION);
        assertThat(logEvent.compareTo(anotherLogEvent), is(-1));
    }

    @Test
    public void testCompareToAnotherLogEventHasSmallerTimestamp() {
        LogEvent logEvent = new LogEvent(2L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(1L, LEVEL, MESSAGE_2, BUNDLE_NAME_2, BUNDLE_VERSION);
        assertThat(logEvent.compareTo(anotherLogEvent), is(1));
    }

    @Test
    public void testHashCode() {
        LogEvent logEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        LogEvent anotherLogEvent = new LogEvent(1L, LEVEL, MESSAGE_1, BUNDLE_NAME_1, BUNDLE_VERSION);
        assertThat(logEvent.equals(anotherLogEvent), is(true));
        assertThat(anotherLogEvent.equals(logEvent), is(true));
        assertThat(logEvent.hashCode(), is(anotherLogEvent.hashCode()));
    }
}

