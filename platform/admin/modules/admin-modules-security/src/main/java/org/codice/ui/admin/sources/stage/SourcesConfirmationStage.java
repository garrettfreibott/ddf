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
import org.codice.ui.admin.wizard.stage.components.PasswordComponent;
import org.codice.ui.admin.wizard.stage.components.StringComponent;

public class SourcesConfirmationStage extends Stage {

    public static final String SOURCES_CONFIRMATION_STAGE_ID = "SourcesConfirmationStage";

    public SourcesConfirmationStage(){
        super();
    }

    public SourcesConfirmationStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
        Configuration cfg = stageToCheck.getConfiguration();

        String finalCswUrl = (String)cfg.getValue(SOURCES_CONFIG_CSW_URL_KEY);
        String finalCswEventUrl = (String)cfg.getValue(SOURCES_CONFIG_CSW_EVENT_URL_KEY);
        String finalUsername = (String)cfg.getValue(SOURCES_CONFIG_USERNAME_KEY);
        String finalPassword = (String)cfg.getValue(SOURCES_CONFIG_PASSWORD_KEY);

        stageToCheck.getRootComponent().subComponents(new InfoComponent("Confirmation")
                        .label("Finalize Source Configuration")
                        .value("Confirm details and press Finish to add source."),
                new StringComponent("CswUrl")
                        .label("CSW Address")
                        .value(finalCswUrl)
                        .isDisabled(true),
                new StringComponent("CswEventUrl")
                        .label("Event Service Address")
                        .value(finalCswEventUrl)
                        .isDisabled(true),
                new StringComponent("Username")
                        .label("Username")
                        .value(finalUsername)
                        .isDisabled(true),
                new PasswordComponent("Password")
                        .label("Password")
                        .value(finalPassword)
                        .isDisabled(true),
                new ButtonActionComponent()
                        .setMethod(POST)
                        .setUrl(getWizardUrl() + "/" + SOURCES_CONFIRMATION_STAGE_ID)
                        .label("Finish"));

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

        return Component.builder("4", Component.ComponentType.BASE_CONTAINER);
    }

    @Override
    public String getStageId() {
        return SOURCES_CONFIRMATION_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesConfirmationStage(stageParameters);
    }
}
