package org.codice.ui.admin.wizard.api;

import java.util.List;

import org.codice.ui.admin.wizard.config.Configuration;

public interface ConfigurationHandler {

    /**
     * Returns a list of error messages resulting from the testing. If empty, assume testing was successful
     * @param testId - Id of the test to perform
     * @param configuration - Configuration with properties that will be used for testing
     * @return Error messages resulting from testing.
     */
    List<String> test(String testId, Configuration configuration);

    /**
     * Persists the configuration to the according bundles and services. Returns a list of error messages resulting from persisting
     * @param configuration - Configuration to persist
     * @return Error messages resulting from persisting
     */
    List<String> persist(Configuration configuration);

    /**
     * UUID of this configuration handler
     * @return - uuid
     */
    String getConfigurationHandlerId();
}
