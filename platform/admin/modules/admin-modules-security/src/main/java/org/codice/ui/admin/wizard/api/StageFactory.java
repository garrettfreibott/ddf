package org.codice.ui.admin.wizard.api;

import org.codice.ui.admin.wizard.stage.Stage;
import org.codice.ui.admin.wizard.stage.StageParameters;

public interface StageFactory {

    String getStageId();

    Stage getNewInstance(StageParameters stageParameters);
}
