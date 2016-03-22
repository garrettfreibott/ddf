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

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class LoggingServiceBean implements PaxAppender, LoggingServiceBeanMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingServiceBean.class);

    private static final String MBEAN_NAME = LoggingServiceBean.class.getName()
            + ":service=logging-service";

    private static final String BUNDLE_NAME = "bundle.name";

    private static final String BUNDLE_VERSION = "bundle.version";

    private final WriteLock writeLock = new ReentrantReadWriteLock().writeLock();

    private final ReadLock readLock = new ReentrantReadWriteLock().readLock();

    private EvictingQueue<LogEvent> logEvents;

    private int maxLogEvents = 500;

    private ObjectName objectName;

    private MBeanServer mBeanServer;

    public LoggingServiceBean() {
        try {
            logEvents = EvictingQueue.create(maxLogEvents);
            objectName = new ObjectName(MBEAN_NAME);
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
        } catch (MalformedObjectNameException e) {
            LOGGER.error("Unable to create Logging Service MBean with name [{}].", MBEAN_NAME, e);
        }
    }

    public void init() {
        try {
            try {
                mBeanServer.registerMBean(this, objectName);
                LOGGER.info("Registered Logging Service MBean under object name: {}",
                        objectName.toString());
            } catch (InstanceAlreadyExistsException e) {
                mBeanServer.unregisterMBean(objectName);
                mBeanServer.registerMBean(this, objectName);
                LOGGER.info("Re-registered Logging Service MBean");
            }
        } catch (MBeanRegistrationException | InstanceNotFoundException
                | InstanceAlreadyExistsException | NotCompliantMBeanException e) {
            LOGGER.error("Could not register MBean [{}].", objectName.toString(), e);
        }
    }

    public void destroy() {
        try {
            if (objectName != null && mBeanServer != null) {
                mBeanServer.unregisterMBean(objectName);
                LOGGER.info("Unregistered Logging Service MBean");
            }
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            LOGGER.error("Exception unregistering MBean [{}].", objectName.toString(), e);
        }
    }

    @Override
    public void doAppend(PaxLoggingEvent paxLoggingEvent) {
        LogEvent logEvent = createLogEvent(paxLoggingEvent);
        add(logEvent);
    }

    @Override
    public List<LogEvent> retrieveLogEvents() {
        try {
            readLock.lock();
            return Lists.newArrayList(logEvents);
        } finally {
            readLock.unlock();
        }
    }

    public void setMaxLogEvents(int newMaxLogEvents) {
        try {
            writeLock.lock();
            EvictingQueue<LogEvent> evictingQueue = EvictingQueue.create(newMaxLogEvents);

            if (logEvents.size() < newMaxLogEvents) {
                evictingQueue.addAll(logEvents);
            } else {
                Iterable<LogEvent> iterable = Iterables.skip(logEvents, logEvents.size()
                        - newMaxLogEvents);
                evictingQueue.addAll(Lists.newArrayList(iterable));
            }
            this.maxLogEvents = newMaxLogEvents;
            logEvents = evictingQueue;
        } finally {
            writeLock.unlock();
        }
    }

    public int getMaxLogEvents() {
        try {
            readLock.lock();
            return maxLogEvents;
        } finally {
            readLock.unlock();
        }
    }

    private void add(LogEvent logEvent) {
        try {
            writeLock.lock();
            logEvents.add(logEvent);
        } finally {
            writeLock.unlock();
        }
    }

    private LogEvent createLogEvent(PaxLoggingEvent paxLoggingEvent) {
        long timestamp = paxLoggingEvent.getTimeStamp();
        String level = paxLoggingEvent.getLevel().toString();
        String message = paxLoggingEvent.getMessage();
        String bundleName = getBundleName(paxLoggingEvent);
        String bundleVersion = getBundleVersion(paxLoggingEvent);
        LogEvent logEvent = new LogEvent(timestamp, level, message, bundleName, bundleVersion);
        return logEvent;
    }

    private String getBundleName(PaxLoggingEvent paxLoggingEvent) {
        return (String) paxLoggingEvent.getProperties().get(BUNDLE_NAME);
    }

    private String getBundleVersion(PaxLoggingEvent paxLoggingEvent) {
        return (String) paxLoggingEvent.getProperties().get(BUNDLE_VERSION);
    }
}

