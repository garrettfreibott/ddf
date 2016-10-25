package org.codice.ui.admin.sources;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.HostnameComponent;
import org.codice.ui.admin.security.stage.components.PortComponent;
import org.codice.ui.admin.security.stage.components.StringComponent;

public class SourcesUrlStage extends Stage {

    public static final String SOURCES_URL_STAGE_ID = "SourcesUrlStage";

    public static final String SOURCE_NAME_ID = "sourcename";
    public static final String SOURCE_HOSTNAME_ID = "hostname";
    public static final String SOURCE_PORT_ID = "port";

    public SourcesUrlStage(){
        super();
    }

    public SourcesUrlStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component sourceName = stageToCheck.getComponent(SOURCE_NAME_ID);
        Component sourceHostname = stageToCheck.getComponent(SOURCE_HOSTNAME_ID);
        Component sourcePort = stageToCheck.getComponent(SOURCE_PORT_ID);

        if (StringUtils.isNotBlank((String)sourcePort.getValue())) {
            sourceName.addError("Invalid Source Name");
        }

        if (StringUtils.isNotBlank((String)sourceHostname.getValue())) {
            sourceHostname.addError("Invalid Hostname");
        }

        if (sourcePort.getValue() == null && (int)sourcePort.getValue() > 0) {
            sourcePort.addError("Invalid Port");
        }

        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, Map<String, String> params) {
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
        return Component.builder("Sources Url Settings", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new StringComponent(SOURCE_NAME_ID).label("Source Name"),
                        new HostnameComponent(SOURCE_HOSTNAME_ID).label("Hostname"),
                        new PortComponent(SOURCE_PORT_ID).label("Port").defaults(8993).value(8993),
                        new ButtonActionComponent().setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + getStageId())
                                .label("Check"));
    }

    @Override
    public String getStageId() {
        return SOURCES_URL_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesUrlStage(stageParameters);
    }
}
