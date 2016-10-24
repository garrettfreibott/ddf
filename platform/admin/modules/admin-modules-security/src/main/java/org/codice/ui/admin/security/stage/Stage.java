package org.codice.ui.admin.security.stage;

import java.util.Map;
import java.util.Optional;

import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.components.Component;

/**
 * Created by tbatie123 on 10/10/16.
 */
public abstract class Stage {

    public static final String NEXT_STAGE_ID = "nextStageId";

    protected Configuration configuration;

    private Component rootComponent;

    private Map<String, String> state;

    private String wizardUrl;

    public Stage(StageFinder stageFinder){
        registerStage(stageFinder);
    }

    public Stage(StageParameters stageParameters){
        wizardUrl = stageParameters.getWizardUrl();
        state = stageParameters.getState();
        configuration = stageParameters.getConfiguration();
        rootComponent = getDefaultRootComponent();
    }

    public abstract void registerStage(StageFinder stageFinder);

    public abstract Stage validateStage(Stage stageToCheck, Map<String, String> params);

    public abstract Stage testStage(Stage stageToTest, Map<String, String> params);

    public abstract Stage commitStage(Stage currentStage, Map<String, String> params);

    public abstract Component getDefaultRootComponent();

    public abstract String getStageId();

    public Map<String, String> getState() {
        return state;
    }

    public void setState(Map<String, String> state) {
        this.state = state;
    }

    public Configuration getConfiguration(){
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

    public Component getComponent(String componentId) {
        return rootComponent.getComponent(componentId);
    }

    public void setDefaultRootComponent(Component defaultRootComponent) {
        this.rootComponent = defaultRootComponent;
    }

    public String getWizardUrl() {
        return wizardUrl;
    }

    public void setWizardUrl(String wizardUrl) {
        this.wizardUrl = wizardUrl;
    }

    public boolean containsError() {
        return rootComponent.containsErrors();
    }

    public void clearErrors(){
        rootComponent.clearAllErrors();
    }
}
