package org.codice.ui.admin.security.api;

import java.util.List;

import org.codice.ui.admin.security.stage.StageComposer;
import org.codice.ui.admin.security.api.StageFactory;

public interface Wizard {

    String getContextPath();

    StageComposer getStageComposer(String contextPath, List<StageFactory> stages);

    String getTitle();

    String getDescription();

    String initialStageId();
}
