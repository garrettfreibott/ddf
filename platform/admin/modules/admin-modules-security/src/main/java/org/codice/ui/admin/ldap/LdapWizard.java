package org.codice.ui.admin.ldap;

import static org.codice.ui.admin.ldap.stage.LdapBindHostSettingsStage.LDAP_BIND_HOST_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.ldap.stage.LdapDirectorySettingsStage.LDAP_DIRECTORY_SETTINGS_STAGE_ID;
import static org.codice.ui.admin.ldap.stage.LdapNetworkSettingsStage.LDAP_NETWORK_SETTINGS_STAGE_ID;

import java.util.List;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.api.StageFactory;
import org.codice.ui.admin.wizard.api.Wizard;
import org.codice.ui.admin.wizard.stage.StageComposer;

public class LdapWizard implements Wizard {

    @Override
    public String getTitle() {
        return "LDAP Wizard";
    }

    @Override
    public String getDescription() {
        return "Help setup that thing called LDAP!";
    }

    @Override
    public String initialStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }

    @Override
    public StageComposer getStageComposer(String contextPath, List<StageFactory> allStages,
            List<ConfigurationHandler> configurationHandlers) {
        return StageComposer.builder(contextPath, allStages, configurationHandlers)
                .link(LDAP_NETWORK_SETTINGS_STAGE_ID, LDAP_BIND_HOST_SETTINGS_STAGE_ID)
                .link(LDAP_BIND_HOST_SETTINGS_STAGE_ID, LDAP_DIRECTORY_SETTINGS_STAGE_ID);
    }

    @Override
    public String getWizardId() {
        return "ldap";
    }
}
