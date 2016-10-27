package org.codice.ui.admin.wizard.stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.api.StageFactory;
import org.codice.ui.admin.wizard.config.Configuration;
import org.codice.ui.admin.wizard.stage.components.Component;

public abstract class Stage implements StageFactory {

    /**
     * Adding this id as a key to the state with a stageId as a value will result in dictating which stage the stage composer should look up next. Should be set during commitStage method
     */
    public static final String NEXT_STAGE_ID = "nextStageId";

    private String wizardUrl;

    /**
     * The configuration object that is built up and persisted between stages
     */
    protected Configuration configuration;

    /**
     * The component that will be rendered in the UI
     */
    private Component rootComponent;

    /**
     * A map used to indicate status of the wizard and any type of useful information along the lifecycle of the stages
     */
    private Map<String, String> state;

    public Stage() {
        state = new HashMap<>();
        configuration = new Configuration();
        rootComponent = getDefaultRootComponent();
    }

    public Stage(StageParameters stageParameters) {
        wizardUrl = stageParameters.getWizardUrl();
        state = stageParameters.getState();
        configuration = stageParameters.getConfiguration();
        rootComponent = getDefaultRootComponent();
    }

    /**
     * This method is invoked when a new stage is returned from the stageComposer. This method should perform default value look and population as well as any any additional preconfiguration
     *
     * @param stageToCheck - The stage to configure before being return to the user
     * @return Preconfigured stage
     */
    public abstract Stage preconfigureStage(Stage stageToCheck, List<ConfigurationHandler> configurationHandlers);

    /**
     * Invokes the components of the stage to validate themselves and performs any additional field validation and testing
     *
     * @param stageToCheck - Instance of the stage object to validate
     * @param params       - parameters of request
     * @return stage that may contain component validation errors
     */
    public abstract Stage validateStage(Stage stageToCheck, Map<String, String> params);

    /**
     * Tests the fields of the stage to provide default values and verify inputs
     *
     * @param stageToTest - Instance of the stage object to test
     * @param params      - parameters of request
     * @return stage that may contain test errors
     */
    public abstract Stage testStage(Stage stageToTest,
            List<ConfigurationHandler> configurationHandlers, Map<String, String> params);

    /**
     * Persists the information from the stage to the stage configuration or to the backend
     *
     * @param stageToPersist - Instance of the stage to persist
     * @param params         - parameters of request
     * @return stage that may contain errors that resulted from persisting
     */
    public abstract Stage commitStage(Stage stageToPersist, Map<String, String> params);

    /**
     * The original root component that should be created on new instances of the stage
     *
     * @return root component
     */
    public abstract Component getDefaultRootComponent();

    public ConfigurationHandler getConfigurationHandler(
            List<ConfigurationHandler> configurationHandlers, String configurationId) {
        Optional<ConfigurationHandler> foundConfigHandler = configurationHandlers.stream()
                .filter(handler -> handler.getConfigurationHandlerId()
                        .equals(configurationId))
                .findFirst();

        // TODO: tbatie - 10/25/16 - Return null or throw exception?
        return foundConfigHandler.isPresent() ? foundConfigHandler.get() : null;
    }

    public Map<String, String> getState() {
        return state;
    }

    public void setState(Map<String, String> state) {
        this.state = state;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Component getRootComponent() {
        return rootComponent;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> Optional<T> getComponent(String componentId, Class<T> clazz) {
        Component component = getComponent(componentId);

        if (clazz.isInstance(component)) {
            return Optional.of((T) component);
        }

        return Optional.empty();
    }

    public void addErrorToComponent(String componentId, String errorMsg) {
        getComponent(componentId).addError(errorMsg);
    }

    public Component getComponent(String componentId) {
        return rootComponent.getComponent(componentId);
    }

    public String getWizardUrl() {
        return wizardUrl;
    }

    public boolean containsError() {
        return rootComponent.containsErrors();
    }

    public void clearErrors() {
        rootComponent.clearAllErrors();
    }
}
