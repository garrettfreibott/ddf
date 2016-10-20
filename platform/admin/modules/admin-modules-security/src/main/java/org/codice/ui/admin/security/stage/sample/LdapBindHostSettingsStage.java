package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.PASSWORD;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.STRING;

import java.util.Map;

import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageFinder;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;

public class LdapBindHostSettingsStage extends Stage {

    public static final String LDAP_BIND_HOST_SETTINGS_STAGE_ID = "ldapBindHostSettingsStageId";

    public static final String BIND_USER_DN = "bindUserDN";

    public static final String BIND_USER_PASS = "bindUserPassword";

    public LdapBindHostSettingsStage(StageFinder stageFinder) {
        super(stageFinder);
    }

    public LdapBindHostSettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public void registerStage(StageFinder stageFinder) {
        stageFinder.registerStage(getStageId(), LdapBindHostSettingsStage::new);
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("LDAP Bind User Settings", Component.ComponentType.BASE_CONTAINER)
                .subComponents(Component.builder(BIND_USER_DN, STRING)
                                .label("LDAP Bind User DN"),
                        Component.builder(BIND_USER_PASS, PASSWORD)
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
    public Stage commitStage(Stage currentStage, Map<String, String> params) {
        Configuration newConfiguration = currentStage.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }
        newConfiguration.addValue(BIND_USER_DN,
                currentStage.getComponent(BIND_USER_DN)
                        .getValue());
        newConfiguration.addValue(BIND_USER_PASS,
                currentStage.getComponent(BIND_USER_PASS)
                        .getValue());
        currentStage.setConfiguration(newConfiguration);
        return currentStage;
    }

    @Override
    public String getStageId() {
        return LDAP_BIND_HOST_SETTINGS_STAGE_ID;
    }
}