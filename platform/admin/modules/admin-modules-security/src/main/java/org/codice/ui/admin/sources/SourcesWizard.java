package org.codice.ui.admin.sources;

import static org.codice.ui.admin.sources.SourcesUrlStage.SOURCES_URL_STAGE_ID;

import java.util.List;

import org.codice.ui.admin.security.api.StageFactory;
import org.codice.ui.admin.security.api.Wizard;
import org.codice.ui.admin.security.stage.StageComposer;

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
        return SOURCES_URL_STAGE_ID;
    }

    @Override
    public StageComposer getStageComposer(String contextPath, List<StageFactory> allStages) {
        return StageComposer.builder(contextPath, allStages);
    }

    @Override
    public String getId() {
        return "HarryPotter";
    }
}
