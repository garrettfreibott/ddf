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
package org.codice.ddf.admin.logging;

public class LogEvent {

    private final long timestamp;
    
    private final String level;
    
    private final String message;
    
    private final String bundleName;
    
    private final String bundleVersion;
    
    public LogEvent(long timestamp, String level, String message, String bundleName, String bundleVersion) {
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
}
