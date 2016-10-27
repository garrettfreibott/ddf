package org.codice.ui.admin.ldap;

import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BIND_TEST_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BIND_USER_DN_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_BIND_USER_PASS_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_CONFIGURATION_HANDLER_ID;
import static org.codice.ui.admin.wizard.stage.components.ButtonActionComponent.Method.POST;

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
import org.codice.ui.admin.wizard.stage.components.PasswordComponent;
import org.codice.ui.admin.wizard.stage.components.StringComponent;

public class LdapBindHostSettingsStage extends Stage {

    public static final String LDAP_BIND_HOST_SETTINGS_STAGE_ID = "ldapBindHostSettingsStageId";

    public LdapBindHostSettingsStage() {
        super();
    }

    public LdapBindHostSettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("LDAP Bind User Settings", Component.ComponentType.BASE_CONTAINER)
                .subComponents(new StringComponent(LDAP_BIND_USER_DN_CONFIGURATION_ID).label(
                        "LDAP Bind User DN"),
                        new PasswordComponent(LDAP_BIND_USER_PASS_CONFIGURATION_ID).label(
                                "LDAP Bind User Password"),
                        new ButtonActionComponent().setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + getStageId())
                                .label("check"));
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck, List<ConfigurationHandler> configurationHandlers) {
        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component bindUserDNQ = stageToCheck.getComponent(LDAP_BIND_USER_DN_CONFIGURATION_ID);
        Component bindUserPassQ = stageToCheck.getComponent(LDAP_BIND_USER_PASS_CONFIGURATION_ID);

        if (bindUserDNQ.getValue() == null) {
            bindUserDNQ.addError("Invalid bind user DN");
        }

        if (bindUserPassQ.getValue() == null) {
            bindUserPassQ.addError("Invalid password entry");
        }

        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage ldapBindHostSettingsStage,
            List<ConfigurationHandler> configurationHandlers, Map<String, String> params) {
        List<String> testErrors = new ArrayList<>();

        // TODO: tbatie - 10/25/16 - Maybe we should move params to the StageComposer level instead. It feels like the stages don't need to know anything about the request coming in. Any information they need should be contained inside the state
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        if (!skipConnectionTest) {
            testErrors = getConfigurationHandler(configurationHandlers,
                    LDAP_CONFIGURATION_HANDLER_ID).test(LDAP_BIND_TEST_ID,
                    ldapBindHostSettingsStage.getConfiguration());
        }

        if (!testErrors.isEmpty()) {
            for (String error : testErrors) {
                ldapBindHostSettingsStage.getRootComponent()
                        .subComponents(new ErrorInfoComponent(null).value(error));

            }

            ldapBindHostSettingsStage.getRootComponent()
                    .subComponents(new ButtonActionComponent().setMethod(POST)
                            .setUrl(getWizardUrl() + "?skip=true")
                            .label("skip"));
        }

        return ldapBindHostSettingsStage;
    }

    @Override
    public Stage commitStage(Stage stageToPersist, Map<String, String> params) {
        Configuration newConfiguration = stageToPersist.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }
        newConfiguration.addValue(LDAP_BIND_USER_DN_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_BIND_USER_DN_CONFIGURATION_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_BIND_USER_PASS_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_BIND_USER_PASS_CONFIGURATION_ID)
                        .getValue());
        stageToPersist.setConfiguration(newConfiguration);
        return stageToPersist;
    }

    @Override
    public String getStageId() {
        return LDAP_BIND_HOST_SETTINGS_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new LdapBindHostSettingsStage(stageParameters);
    }
}