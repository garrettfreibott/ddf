package org.codice.ui.admin.sources;

import static org.codice.ui.admin.sources.stage.SourcesConfirmationStage.SOURCES_CONFIRMATION_STAGE_ID;
import static org.codice.ui.admin.sources.stage.SourcesPersistedStage.SOURCES_PERSISTED_STAGE_ID;
import static org.codice.ui.admin.sources.stage.SourcesSetupStage.SOURCES_SETUP_STAGE_ID;
import static org.codice.ui.admin.sources.stage.SourcesDiscoveryStage.SOURCES_DISCOVERY_STAGE_ID;
import static org.codice.ui.admin.sources.stage.SourcesPollingStage.SOURCES_POLLING_STAGE_ID;



import java.util.List;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.api.StageFactory;
import org.codice.ui.admin.wizard.api.Wizard;
import org.codice.ui.admin.wizard.stage.StageComposer;

public class SourcesWizard implements Wizard {

    @Override
    public String getTitle() {
        return "Sources Wizard";
    }

    @Override
    public String getDescription() {
        return "Help setup that thing called SOURCES!";
    }

    @Override
    public String initialStageId() {
        return SOURCES_SETUP_STAGE_ID;
    }

    @Override
    public StageComposer getStageComposer(String contextPath, List<StageFactory> stages,
            List<ConfigurationHandler> configurationHandlers) {
        return StageComposer.builder(contextPath, stages, configurationHandlers)
                .link(SOURCES_DISCOVERY_STAGE_ID, SOURCES_POLLING_STAGE_ID)
                .link(SOURCES_POLLING_STAGE_ID, SOURCES_CONFIRMATION_STAGE_ID)
                .link(SOURCES_CONFIRMATION_STAGE_ID, SOURCES_PERSISTED_STAGE_ID);
    }

    @Override
    public String getWizardId() {
        return "sources";
    }
}
