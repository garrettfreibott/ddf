package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;

import java.util.Map;

import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.PasswordComponent;
import org.codice.ui.admin.security.stage.components.StringComponent;

public class LdapBindHostSettingsStage extends Stage {

    public static final String LDAP_BIND_HOST_SETTINGS_STAGE_ID = "ldapBindHostSettingsStageId";

    public static final String BIND_USER_DN = "bindUserDN";

    public static final String BIND_USER_PASS = "bindUserPassword";

    public LdapBindHostSettingsStage() {
        super();
    }

    public LdapBindHostSettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("LDAP Bind User Settings", Component.ComponentType.BASE_CONTAINER)
                .subComponents(
                        new StringComponent(BIND_USER_DN)
                                .label("LDAP Bind User DN"),
                        new PasswordComponent(BIND_USER_PASS)
                                .label("LDAP Bind User Password"),
                        new ButtonActionComponent().setMethod(POST)
                                .setUrl(getWizardUrl() + "/" + getStageId())
                                .label("check"));
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component bindUserDNQ = stageToCheck.getComponent(BIND_USER_DN);
        Component bindUserPassQ = stageToCheck.getComponent(BIND_USER_PASS);

        if (bindUserDNQ.getValue() == null) {
            bindUserDNQ.addError("Invalid bind user DN");
        }

        if (bindUserPassQ.getValue() == null) {
            bindUserPassQ.addError("Invalid password entry");
        }

        return stageToCheck;
    }

    @Override
    public Stage testStage(Stage stageToTest, Map<String, String> params) {
        //Test ldap connection
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //// TODO: tbatie - 10/5/16 - Test bind user connection
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            stageToTest.getRootComponent()
                    .subComponents(new ButtonActionComponent().setUrl(getWizardUrl() + "?skip=true")
                            .setMethod(POST)
                            .label("skip"));
        }

        return stageToTest;
    }

    @Override
    public Stage commitStage(Stage stageToPersist, Map<String, String> params) {
        Configuration newConfiguration = stageToPersist.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }
        newConfiguration.addValue(BIND_USER_DN,
                stageToPersist.getComponent(BIND_USER_DN)
                        .getValue());
        newConfiguration.addValue(BIND_USER_PASS,
                stageToPersist.getComponent(BIND_USER_PASS)
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