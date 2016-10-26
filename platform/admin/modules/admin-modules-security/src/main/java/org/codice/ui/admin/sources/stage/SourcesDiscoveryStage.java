package org.codice.ui.admin.sources.stage;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codice.ui.admin.security.api.ConfigurationHandler;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.HostnameComponent;
import org.codice.ui.admin.security.stage.components.InfoComponent;
import org.codice.ui.admin.security.stage.components.PortComponent;
import org.codice.ui.admin.security.stage.components.StringComponent;

public class SourcesDiscoveryStage extends Stage {

    public static final String SOURCES_DISCOVERY_STAGE_ID = "SourcesDiscoveryStage";

    public static final String SOURCE_HOSTNAME_ID = "sourceHostnameId";
    public static final String SOURCE_PORT_ID = "sourcePort";

    public static final String SOURCE_USERNAME = "sourceUsername";
    public static final String SOURCE_PASSWORD = "sourcePassword";

    public SourcesDiscoveryStage(){
        super();
    }

    public SourcesDiscoveryStage(StageParameters stageParameters){
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component sourceHostname = stageToCheck.getComponent(SOURCE_HOSTNAME_ID);
        Component sourcePort = stageToCheck.getComponent(SOURCE_PORT_ID);

        if (StringUtils.isNotBlank((String)sourceHostname.getValue())) {
            sourceHostname.addError("Invalid Hostname");
        }

        if (sourcePort.getValue() == null && (int)sourcePort.getValue() > 0) {
            sourcePort.addError("Invalid Port");
        }

        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, List<ConfigurationHandler> configurationHandlers,
            Map<String, String> params) {
        return stageToTest;
    }

    @Override
    public Stage commitStage(Stage stageToCommit, Map<String, String> params) {
        //TODO: this
        return stageToCommit;
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("Sources", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new InfoComponent("Title").label("Discover Sources").value("Enter source information to scan for available sources."),
                        new HostnameComponent(SOURCE_HOSTNAME_ID).label("Hostname"),
                        new PortComponent(SOURCE_PORT_ID).label("Port").defaults(8993).value(8993),
                        new StringComponent(SOURCE_USERNAME).label("Username (optional)"),
                        new StringComponent(SOURCE_PASSWORD).label("Password (optional)"),
                        new ButtonActionComponent().setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + getStageId())
                                .label("Check"));
    }

    @Override
    public String getStageId() {
        return SOURCES_DISCOVERY_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new SourcesDiscoveryStage(stageParameters);
    }
}
