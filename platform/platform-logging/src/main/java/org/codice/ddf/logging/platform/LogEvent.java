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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Describes a log event in the system
 */
public class LogEvent implements Comparable<LogEvent> {

    private final long timestamp;

    private final String level;

    private final String message;

    private final String bundleName;

    private final String bundleVersion;

    /**
     * @param timestamp
     *            timestamp of this {@link LogEvent}
     * @param level
     *            level of this {@link LogEvent}
     * @param message
     *            log message of this {@link LogEvent}
     * @param bundleName
     *            the name of the bundle creating this {@link LogEvent}
     * @param bundleVersion
     *            the version of the bundle creating this {@link LogEvent}
     */
    public LogEvent(long timestamp, String level, String message, String bundleName,
            String bundleVersion) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.bundleName = bundleName;
        this.bundleVersion = bundleVersion;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getBundleName() {
        return bundleName;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    @Override
    public int compareTo(LogEvent anotherLogEvent) {
        long anotherTimestamp = ((LogEvent) anotherLogEvent).getTimestamp();
        if (timestamp < anotherTimestamp) {
            return -1;
        } else if (timestamp > anotherTimestamp) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object anotherLogEvent) {
        if (!(anotherLogEvent instanceof LogEvent)) {
            return false;
        }
        if (anotherLogEvent == this) {
            return true;
        }
        LogEvent rhs = (LogEvent) anotherLogEvent;
        return new EqualsBuilder().append(timestamp, rhs.getTimestamp())
                .append(level, rhs.getLevel().toString()).append(message, rhs.getMessage())
                .append(bundleName, rhs.getBundleName())
                .append(bundleVersion, rhs.getBundleVersion()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(timestamp).append(level).append(message)
                .append(bundleName).append(bundleVersion).toHashCode();
    }
}
