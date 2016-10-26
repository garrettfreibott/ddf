package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.GET;
import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.sources.stage.SourcesAdvancedStage.SOURCES_ADVANCED_STAGE_ID;
import static org.codice.ui.admin.sources.stage.SourcesDiscoveryStage.SOURCES_DISCOVERY_STAGE_ID;

import java.util.Map;

import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.InfoComponent;

public class SourcesSetupStage extends Stage {

    public static final String SOURCES_SETUP_STAGE_ID = "sourcesSetupStage";

    public SourcesSetupStage(){
        super();
    }

    public SourcesSetupStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, Map<String, String> params) {
        return stageToTest;
    }

    @Override
    public Stage commitStage(Stage stageToCommit, Map<String, String> params) {
        return stageToCommit;
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("Sources", Component.ComponentType.BASE_CONTAINER)
                .subComponents(
                        new ButtonActionComponent().setMethod(GET)
                                .setUrl(getWizardUrl() + "/" + SOURCES_DISCOVERY_STAGE_ID)
                                .label("Discover Available Sources"),
                        new InfoComponent("OR").label("or"),
                        new ButtonActionComponent().setMethod(GET)
                                .setUrl(getWizardUrl() + "/" + SOURCES_ADVANCED_STAGE_ID)
                                .label("Manually Enter Source Info"));
    }

    @Override
    public String getStageId() {
        return SOURCES_SETUP_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesSetupStage(stageParameters);
    }
}
