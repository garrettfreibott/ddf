package org.codice.ui.admin.security.api;

import java.util.List;

import org.codice.ui.admin.security.stage.StageComposer;
import org.codice.ui.admin.security.api.StageFactory;

public interface Wizard {

    StageComposer getStageComposer(String contextPath, List<StageFactory> stages, List<ConfigurationHandler> configurationHandlers);

    String getWizardId();

    String getTitle();

    String getDescription();

    String initialStageId();
}
