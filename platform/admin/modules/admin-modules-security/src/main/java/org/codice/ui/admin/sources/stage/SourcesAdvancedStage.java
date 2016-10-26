package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;

import java.util.List;
import java.util.Map;

import org.codice.ui.admin.security.api.ConfigurationHandler;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.InfoComponent;

public class SourcesAdvancedStage extends Stage {

    public static final String SOURCES_ADVANCED_STAGE_ID = "SourcesAdvancedStage";

    public SourcesAdvancedStage(){
        super();
    }

    public SourcesAdvancedStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, List<ConfigurationHandler> configurationHandlers,
            Map<String, String> params) {
        return stageToTest;
    }

    @Override
    public Stage commitStage(Stage stageToCommit, Map<String, String> params) {
        return stageToCommit;
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("Sources", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new InfoComponent("Title").label("The Advanced Configuration Page").value("It's advaaaaaanced!"),
                        new ButtonActionComponent().setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + getStageId())
                                .label("leave"));
    }

    @Override
    public String getStageId() {
        return SOURCES_ADVANCED_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesAdvancedStage(stageParameters);
    }
}
