package org.codice.ui.admin.ldap;

import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BASE_GROUP_DN_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BASE_USER_DN_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_CONFIGURATION_HANDLER_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_DIRECTORY_STRUCT_TEST_ID;
import static org.codice.ui.admin.wizard.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.wizard.stage.components.Component.ComponentType.BASE_CONTAINER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.stage.StageParameters;
import org.codice.ui.admin.wizard.config.Configuration;
import org.codice.ui.admin.wizard.stage.Stage;
import org.codice.ui.admin.wizard.stage.components.ButtonActionComponent;
import org.codice.ui.admin.wizard.stage.components.Component;
import org.codice.ui.admin.wizard.stage.components.ErrorInfoComponent;
import org.codice.ui.admin.wizard.stage.components.StringComponent;

public class LdapDirectorySettingsStage extends Stage {

    public static final String LDAP_DIRECTORY_SETTINGS_STAGE_ID = "ldapDirectorySettingStage";

    public LdapDirectorySettingsStage() {
        super();
    }

    public LdapDirectorySettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck, List<ConfigurationHandler> configurationHandlers) {
        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component baseUserDnQ = stageToCheck.getComponent(LDAP_BASE_USER_DN_CONFIGURATION_ID);
        Component baseGroupDnQ = stageToCheck.getComponent(LDAP_BASE_GROUP_DN_CONFIGURATION_ID);
        Component baseUsernameAttriQ = stageToCheck.getComponent(LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID);

        if (baseUserDnQ.getValue() == null) {
            baseUserDnQ.addError("The specified user DN does not exist.");
        }

        if (baseGroupDnQ.getValue() == null) {
            baseGroupDnQ.addError("The specified group DN does not exist.");
        }

        if (baseUsernameAttriQ.getValue() == null) {
            baseUsernameAttriQ.addError("The specified username attribute does not appear to exist.");
        }

        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage ldapDirectorySettingsStage, List<ConfigurationHandler> configurationHandlers, Map<String, String> params) {
        List<String> testErrors = new ArrayList<>();

        // TODO: tbatie - 10/25/16 - Maybe we should move params to the StageComposer level instead. It feels like the stages don't need to know anything about the request coming in. Any information they need should be contained inside the state
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        if (!skipConnectionTest) {
            testErrors = getConfigurationHandler(configurationHandlers,
                    LDAP_CONFIGURATION_HANDLER_ID).test(LDAP_DIRECTORY_STRUCT_TEST_ID,
                    ldapDirectorySettingsStage.getConfiguration());
        }

        if (!testErrors.isEmpty()) {
            for (String error : testErrors) {
                ldapDirectorySettingsStage.getRootComponent()
                        .subComponents(new ErrorInfoComponent(null).value(error));

            }

            ldapDirectorySettingsStage.getRootComponent()
                    .subComponents(new ButtonActionComponent().setMethod(POST)
                            .setUrl(getWizardUrl() + "?skip=true")
                            .label("skip"));
        }

        return ldapDirectorySettingsStage;
    }

    @Override
    public Stage commitStage(Stage stageToPersist, Map<String, String> params) {
        Configuration newConfiguration = stageToPersist.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }

        newConfiguration.addValue(LDAP_BASE_USER_DN_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_BASE_USER_DN_CONFIGURATION_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_BASE_GROUP_DN_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_BASE_GROUP_DN_CONFIGURATION_ID)
                        .getValue());
        stageToPersist.setConfiguration(newConfiguration);
        return stageToPersist;
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("LDAP Directory Settings", BASE_CONTAINER)
                .subComponents(new StringComponent(LDAP_BASE_USER_DN_CONFIGURATION_ID).label("Base User DN"),
                        new StringComponent(LDAP_BASE_GROUP_DN_CONFIGURATION_ID).label("Group User DN"),
                        new StringComponent(LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID).label("User name attribute"),
                        new ButtonActionComponent().setUrl(
                                getWizardUrl() + "/" + LDAP_DIRECTORY_SETTINGS_STAGE_ID)
                                .setMethod(POST)
                                .label("check"));
    }

    @Override
    public String getStageId() {
        return LDAP_DIRECTORY_SETTINGS_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new LdapDirectorySettingsStage(stageParameters);
    }
}