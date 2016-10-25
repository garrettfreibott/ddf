package org.codice.ui.admin.security.api;

import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;

public interface StageFactory {

    String getStageId();

    Stage getNewInstance(StageParameters stageParameters);
}
