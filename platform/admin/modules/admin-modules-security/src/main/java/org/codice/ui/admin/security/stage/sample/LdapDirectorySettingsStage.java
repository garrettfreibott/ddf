package org.codice.ui.admin.security.stage.sample;

import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.BASE_CONTAINER;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.BUTTON;

import java.util.Map;

import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageFinder;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.StringComponent;

public class LdapDirectorySettingsStage extends Stage {

    public static final String LDAP_DIRECTORY_SETTINGS_STAGE_ID = "ldapDirectorySettingStage";

    public static final String BASE_USER_DN = "baseUserDN";

    public static final String BASE_USERNAME_ATTRIBUTE = "baseUserNameAttribute";

    public static final String BASE_GROUP_DN = "baseGroupDN";

    public LdapDirectorySettingsStage(StageFinder stageFinder) {
        super(stageFinder);
    }

    public LdapDirectorySettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public void registerStage(StageFinder stageFinder) {
        stageFinder.registerStage(getStageId(), LdapDirectorySettingsStage::new);
    }

    @Override
    public Stage validateStage(Stage stageToCheck, Map<String, String> params) {
        Component baseUserDnQ = stageToCheck.getComponent(BASE_USER_DN);
        Component baseGroupDnQ = stageToCheck.getComponent(BASE_GROUP_DN);
        Component baseUsernameAttriQ = stageToCheck.getComponent(BASE_USERNAME_ATTRIBUTE);

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
    public Stage testStage(Stage stageToTest, Map<String, String> params) {
        //Test ldap connection
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        boolean connectionSuccessful = false;
        if (!skipConnectionTest) {
            //test baserUserDN, baseGroupDN, baseUsernameAttri
            connectionSuccessful = true;
        }

        if (!connectionSuccessful && !skipConnectionTest) {
            stageToTest.getRootComponent()
                    .subComponents(new ButtonActionComponent().setUrl(
                                    getWizardUrl() + "?skip=true")
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

        newConfiguration.addValue(BASE_USER_DN,
                currentStage.getComponent(BASE_USER_DN)
                        .getValue());
        newConfiguration.addValue(BASE_GROUP_DN,
                currentStage.getComponent(BASE_GROUP_DN)
                        .getValue());
        newConfiguration.addValue(BASE_USERNAME_ATTRIBUTE,
                currentStage.getComponent(BASE_USERNAME_ATTRIBUTE)
                        .getValue());
        currentStage.setConfiguration(newConfiguration);
        return currentStage;
    }

    @Override
    public Component getDefaultRootComponent() {
        return Component.builder("LDAP Directory Settings", BASE_CONTAINER)
                .subComponents(new StringComponent(BASE_USER_DN).label("Base User DN"),
                        new StringComponent(BASE_GROUP_DN).label("Group User DN"),
                        new StringComponent(BASE_USERNAME_ATTRIBUTE).label("User name attribute"),
                        new ButtonActionComponent().setUrl(
                                getWizardUrl() + "/" + LDAP_DIRECTORY_SETTINGS_STAGE_ID)
                                .setMethod(POST)
                                .label("check"));
    }

    @Override
    public String getStageId() {
        return LDAP_DIRECTORY_SETTINGS_STAGE_ID;
    }
}