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
package org.codice.ui.admin.wizard.api;

import java.util.List;

import org.codice.ui.admin.wizard.config.Configuration;

public interface ConfigurationHandler<S extends Configuration> {

    String CONFIGURATION_HANDLER_ID = "configurationHandler";

    /**
     * Used to search the system for information relative to the configuration and type of probing.
     *
     * @param probeId       - id of the probe to be used for searching
     * @param configuration - information used for probing
     * @return ProbeReport containing the results of probe
     */
    ProbeReport probe(String probeId, S configuration);

    /**
     * Returns a list of error messages resulting from the testing. If empty, assume testing was successful
     *
     * @param testId        - Id of the test to perform
     * @param configuration - Configuration with properties that will be used for testing
     * @return Error messages resulting from testing.
     */
    TestReport test(String testId, S configuration);

    /**
     * Persists the configuration to the according bundles and services. Returns a list of error messages resulting from persisting
     *
     * @param configuration - Configuration to persist
     * @return Error messages resulting from persisting
     */
    TestReport persist(S configuration);

    /*
     * Returns configurations previously created from this configuration handler
     */
    List<S> getConfigurations();

    /**
     * UUID of this configuration handler
     *
     * @return - uuid
     */
    String getConfigurationHandlerId();
}