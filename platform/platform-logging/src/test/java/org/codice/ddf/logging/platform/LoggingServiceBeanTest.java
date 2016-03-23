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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.management.MBeanServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.ops4j.pax.logging.spi.PaxLevel;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;

@RunWith(MockitoJUnitRunner.class)
public class LoggingServiceBeanTest {

    private static final String BUNDLE_NAME = "bundle.name";

    private static final String BUNDLE_VERSION = "bundle.version";

    private static final int DEFAULT_LOG_EVENTS_LIMIT = 500;
    
    private static final int MAX_LOG_EVENTS_LIMIT = 5000;

    @Mock
    private MBeanServer mockMBeanServer;

    @Test
    public void testRetrieveLogEvents() {
        // Setup
        List<PaxLoggingEvent> mockPaxLoggingEvents = getMockPaxLoggingEventsTimestampOrderSmallestToLargest(3);
        List<LogEvent> expectedLogEvents = getExpectedLogEvents(mockPaxLoggingEvents);
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();
        appendLogs(loggingServiceBean, mockPaxLoggingEvents);

        // Perform Test
        List<LogEvent> actualLogEvents = loggingServiceBean.retrieveLogEvents();

        // Verify
        assertThat(actualLogEvents,
                contains(expectedLogEvents.toArray(new LogEvent[expectedLogEvents.size()])));
    }

    @Test
    public void testResizeMaxLogEventsFrom500To1() {
        // Setup
        List<PaxLoggingEvent> mockPaxLoggingEvents = getMockPaxLoggingEventsTimestampOrderSmallestToLargest(3);
        LogEvent expectedLogEventAfterResize = getExpectedLogEvent(mockPaxLoggingEvents.get(2));
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();
        appendLogs(loggingServiceBean, mockPaxLoggingEvents);

        // Perform Test
        loggingServiceBean.setMaxLogEvents(1);

        // Verify
        List<LogEvent> actualLogEvents = loggingServiceBean.retrieveLogEvents();
        assertThat(actualLogEvents, contains(expectedLogEventAfterResize));
    }

    @Test
    public void testResizeMaxLogEventsFrom500To600() {
        // Setup
        List<PaxLoggingEvent> mockPaxLoggingEvents = getMockPaxLoggingEventsTimestampOrderSmallestToLargest(3);
        List<LogEvent> expectedLogEvents = getExpectedLogEvents(mockPaxLoggingEvents);
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();
        appendLogs(loggingServiceBean, mockPaxLoggingEvents);

        // Perform Test
        loggingServiceBean.setMaxLogEvents(600);

        // Verify
        List<LogEvent> actualLogEvents = loggingServiceBean.retrieveLogEvents();
        assertThat(actualLogEvents,
                contains(expectedLogEvents.toArray(new LogEvent[expectedLogEvents.size()])));
    }

    @Test
    public void testSetMaxLogEventsTo0() {
        // Setup
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();

        // Perform Test
        loggingServiceBean.setMaxLogEvents(0);

        // Verify
        int newMaxLogEvents = loggingServiceBean.getMaxLogEvents();
        assertThat(newMaxLogEvents, is(DEFAULT_LOG_EVENTS_LIMIT));
    }

    @Test
    public void testSetMaxLogEventsToExceedLimit() {
        // Setup
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();

        // Perform Test
        loggingServiceBean.setMaxLogEvents(MAX_LOG_EVENTS_LIMIT + 1);

        // Verify
        int newMaxLogEvents = loggingServiceBean.getMaxLogEvents();
        assertThat(newMaxLogEvents, is(DEFAULT_LOG_EVENTS_LIMIT));
    }

    /**
     * Verify oldest log events are evicted when queue is full.
     */
    @Test
    public void testDoAppendWhenLoggingQueueIsFull() {
        // Setup
        List<PaxLoggingEvent> mockPaxLoggingEvents = getMockPaxLoggingEventsTimestampOrderSmallestToLargest(6);
        List<LogEvent> expectedLogEvents = getExpectedLogEvents(mockPaxLoggingEvents).subList(3,
                mockPaxLoggingEvents.size());
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.init();
        loggingServiceBean.setMaxLogEvents(3);
        appendLogs(loggingServiceBean, mockPaxLoggingEvents.subList(0, 3));

        // Perform Test
        appendLogs(loggingServiceBean, mockPaxLoggingEvents.subList(3, mockPaxLoggingEvents.size()));

        // Verify
        List<LogEvent> actualLogEvents = loggingServiceBean.retrieveLogEvents();
        assertThat(actualLogEvents,
                contains(expectedLogEvents.toArray(new LogEvent[expectedLogEvents.size()])));
    }

    @Test
    public void testDestroy() {
        LoggingServiceBean loggingServiceBean = new LoggingServiceBean();
        loggingServiceBean.destroy();
    }

    private PaxLevel getMockPaxLevel(String level) {
        PaxLevel mockPaxLevel = new PaxLevel() {
            @Override
            public String toString() {
                return level;
            }

            @Override
            public boolean isGreaterOrEqual(PaxLevel r) {
                return false;
            }

            @Override
            public int toInt() {
                return 0;
            }

            @Override
            public int getSyslogEquivalent() {
                return 0;
            }
        };

        return mockPaxLevel;
    }

    private List<PaxLoggingEvent> getMockPaxLoggingEventsTimestampOrderSmallestToLargest(
            int numberOfLoggingEvents) {
        String baseMessage = "message ";
        String baseBundleName = "my-bundle-name-";

        List<PaxLoggingEvent> mockPaxLoggingEvents = new ArrayList<>(3);

        for (int i = 0; i < numberOfLoggingEvents; i++) {
            PaxLoggingEvent mockPaxLoggingEvent = getMockPaxLoggingEvent(i, "INFO",
                    baseMessage + i, baseBundleName + i, "1.2.3");
            mockPaxLoggingEvents.add(mockPaxLoggingEvent);
        }

        return mockPaxLoggingEvents;
    }

    private List<LogEvent> getExpectedLogEvents(List<PaxLoggingEvent> mockPaxLoggingEvents) {
        List<LogEvent> expectedLogEvents = new ArrayList<>(mockPaxLoggingEvents.size());

        for (PaxLoggingEvent mockPaxLoggingEvent : mockPaxLoggingEvents) {
            expectedLogEvents.add(getExpectedLogEvent(mockPaxLoggingEvent));
        }

        Collections.sort(expectedLogEvents);
        return expectedLogEvents;
    }

    private LogEvent getExpectedLogEvent(PaxLoggingEvent mockPaxLoggingEvent) {
        return new LogEvent(mockPaxLoggingEvent.getTimeStamp(), mockPaxLoggingEvent.getLevel()
                .toString(), mockPaxLoggingEvent.getMessage(), getBundleName(mockPaxLoggingEvent),
                getBundleVersion(mockPaxLoggingEvent));
    }

    private void addBundleNameProperty(Properties properties, String bundleName) {
        properties.put(BUNDLE_NAME, bundleName);
    }

    private void addBundleVersionProperty(Properties properties, String bundleVersion) {
        properties.put(BUNDLE_VERSION, bundleVersion);
    }

    private Properties getLoggingProperties(String bundleName, String bundleVersion) {
        Properties properties = new Properties();
        addBundleNameProperty(properties, bundleName);
        addBundleVersionProperty(properties, bundleVersion);
        return properties;
    }

    private PaxLoggingEvent getMockPaxLoggingEvent(long timestamp, String level, String message,
            String bundleName, String bundleVersion) {
        PaxLoggingEvent mockPaxLoggingEvent = mock(PaxLoggingEvent.class);
        when(mockPaxLoggingEvent.getTimeStamp()).thenReturn(timestamp);
        when(mockPaxLoggingEvent.getLevel()).thenReturn(getMockPaxLevel(level));
        when(mockPaxLoggingEvent.getMessage()).thenReturn(message);
        when(mockPaxLoggingEvent.getProperties()).thenReturn(
                getLoggingProperties(bundleName, bundleVersion));
        return mockPaxLoggingEvent;
    }

    private String getBundleName(PaxLoggingEvent paxLoggingEvent) {
        return (String) paxLoggingEvent.getProperties().get(BUNDLE_NAME);
    }

    private String getBundleVersion(PaxLoggingEvent paxLoggingEvent) {
        return (String) paxLoggingEvent.getProperties().get(BUNDLE_VERSION);
    }

    private void appendLogs(LoggingServiceBean loggingServiceBean,
            List<PaxLoggingEvent> mockPaxLoggingEvents) {
        for (PaxLoggingEvent mockPaxLoggingEvent : mockPaxLoggingEvents) {
            loggingServiceBean.doAppend(mockPaxLoggingEvent);
        }
    }
}
