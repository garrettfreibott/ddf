package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.wizard.stage.components.ButtonActionComponent.Method.POST;

import java.util.List;
import java.util.Map;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.stage.Stage;
import org.codice.ui.admin.wizard.stage.StageParameters;
import org.codice.ui.admin.wizard.stage.components.ButtonActionComponent;
import org.codice.ui.admin.wizard.stage.components.Component;
import org.codice.ui.admin.wizard.stage.components.InfoComponent;
import org.codice.ui.admin.wizard.stage.components.RadioButtonsComponent;

public class SourcesPollingStage extends Stage {

    public static final String SOURCES_POLLING_STAGE_ID = "SourcesPollingStage";

    public SourcesPollingStage(){
        super();
    }

    public SourcesPollingStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
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
        //TODO: dynamically create this list
        String[] options = {"https://test:1234/services/csw",
                "https://test:1234/services/opensearch",
                "https://test:1234/services/closedsearch",
                "https://test:1234/services/advaaaaanced"};

        return Component.builder("Polling", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new InfoComponent("Title")
                                .label("Sources Found!")
                                .value("Choose which source to add."),
                        new RadioButtonsComponent("Available Sources")
                                .setOptions(options)
                                .setDefault(options[1]),
                        new ButtonActionComponent()
                                .setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + SOURCES_POLLING_STAGE_ID)
                                .label("Next"));
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
