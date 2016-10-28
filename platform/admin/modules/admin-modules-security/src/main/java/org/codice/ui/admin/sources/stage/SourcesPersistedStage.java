package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_CSW_EVENT_URL_KEY;
import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_CSW_URL_KEY;
import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_PASSWORD_KEY;
import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_USERNAME_KEY;
import static org.codice.ui.admin.wizard.stage.components.ButtonActionComponent.Method.POST;

import java.util.List;
import java.util.Map;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.config.Configuration;
import org.codice.ui.admin.wizard.stage.Stage;
import org.codice.ui.admin.wizard.stage.StageParameters;
import org.codice.ui.admin.wizard.stage.components.ButtonActionComponent;
import org.codice.ui.admin.wizard.stage.components.Component;
import org.codice.ui.admin.wizard.stage.components.InfoComponent;
import org.codice.ui.admin.wizard.stage.components.RadioButtonsComponent;
import org.codice.ui.admin.wizard.stage.components.StatusPageComponent;

public class SourcesPersistedStage extends Stage {

    public static final String SOURCES_PERSISTED_STAGE_ID = "SourcesPersistedStage";

    private String finalCswUrl;
    private String finalCswEventUrl;
    private String finalUsername;
    private String finalPassword;

    public SourcesPersistedStage(){
        super();
    }

    boolean passed = false;

    public SourcesPersistedStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck, List<ConfigurationHandler> configurationHandlers) {
        Configuration cfg = stageToCheck.getConfiguration();
        String cswUrl = (String)stageToCheck.getConfiguration().getValue(SOURCES_CONFIG_CSW_URL_KEY);

        if (cswUrl.endsWith("csw")) {
            passed = true;
        }

        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        //TODO: this
        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, List<ConfigurationHandler> configurationHandlers, Map<String, String> params) {
        //TODO: this
        return stageToTest;
    }

    @Override
    public Stage commitStage(Stage stageToCommit, Map<String, String> params) {
        //TODO: this
        return stageToCommit;
    }

    @Override
    public Component getDefaultRootComponent() {

        return Component.builder("5", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new InfoComponent("Title")
                                .label("Source Has Been Added!"),
                        new StatusPageComponent().succeeded(true),
                        new ButtonActionComponent()
                                .setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + SOURCES_PERSISTED_STAGE_ID)
                                .label("Add Another Source"));
    }

    @Override
    public String getStageId() {
        return SOURCES_PERSISTED_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesPersistedStage(stageParameters);
    }
}
