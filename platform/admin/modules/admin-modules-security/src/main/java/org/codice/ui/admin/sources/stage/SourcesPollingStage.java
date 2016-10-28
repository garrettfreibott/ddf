package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_CSW_URL_KEY;
import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_CONFIG_OPENSEARCH_URL_KEY;
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
import org.eclipse.jetty.http.HttpStatus;

public class SourcesPollingStage extends Stage {

    public static final String SOURCES_POLLING_STAGE_ID = "SourcesPollingStage";

    public static final String AVAILABLE_SOURCES_RADIO_ID = "AvailableSourcesRadio";

    public SourcesPollingStage(){
        super();
    }

    public SourcesPollingStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
        Configuration cfg = stageToCheck.getConfiguration();

        String[]options = new String[]{
                (String)cfg.getValue(SOURCES_CONFIG_CSW_URL_KEY),
                (String)cfg.getValue(SOURCES_CONFIG_OPENSEARCH_URL_KEY),
                "https://test:8993/services/closedsearch"
        };

        stageToCheck.getRootComponent().subComponents().subComponents(new InfoComponent("Title")
                        .label("Sources Found!")
                        .value("Choose which source to add."),
                new RadioButtonsComponent(AVAILABLE_SOURCES_RADIO_ID)
                        .setOptions(options)
                        .setDefault(options[1]),
                new ButtonActionComponent()
                        .setMethod(POST)
                        .setUrl(getWizardUrl() + "/" + SOURCES_POLLING_STAGE_ID)
                        .label("Next"));

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
        String selectedUrl = (String)stageToCommit.getComponent(AVAILABLE_SOURCES_RADIO_ID).getValue();
        stageToCommit.getConfiguration().addValue(SOURCES_CONFIG_CSW_URL_KEY, selectedUrl);
        return stageToCommit;
    }

    @Override
    public Component getDefaultRootComponent() {
        //TODO: dynamically create this list

        return Component.builder("3", Component.ComponentType.BASE_CONTAINER);
    }

    @Override
    public String getStageId() {
        return SOURCES_POLLING_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesPollingStage(stageParameters);
    }
}
