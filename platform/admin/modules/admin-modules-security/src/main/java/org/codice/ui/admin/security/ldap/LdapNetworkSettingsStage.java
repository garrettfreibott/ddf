package org.codice.ui.admin.security.ldap;

import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAP_CONFIGURATION_HANDLER_ID;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAP_CONNECTION_TEST_ID;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAP_HOST_NAME_CONFIGURATION_ID;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAP_PORT_CONFIGURATION_ID;
import static org.codice.ui.admin.security.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.security.stage.components.Component.ComponentType.BASE_CONTAINER;

import static org.codice.ui.admin.security.config.LdapConfigurationHandler.NONE;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.LDAPS;
import static org.codice.ui.admin.security.config.LdapConfigurationHandler.TLS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codice.ui.admin.security.api.ConfigurationHandler;
import org.codice.ui.admin.security.config.Configuration;
import org.codice.ui.admin.security.stage.Stage;
import org.codice.ui.admin.security.stage.StageParameters;
import org.codice.ui.admin.security.stage.components.ButtonActionComponent;
import org.codice.ui.admin.security.stage.components.Component;
import org.codice.ui.admin.security.stage.components.ErrorMessageComponent;
import org.codice.ui.admin.security.stage.components.HostnameComponent;
import org.codice.ui.admin.security.stage.components.PortComponent;
import org.codice.ui.admin.security.stage.components.StringEnumComponent;

import com.google.common.collect.ImmutableMap;

public class LdapNetworkSettingsStage extends Stage {

    public static final String LDAP_NETWORK_SETTINGS_STAGE_ID = "ldapNetworkSettingsStage";

    public static final Map LDAP_ENCRYPTION_METHODS_MAP = ImmutableMap.of("No encryption",
            NONE,
            "Use ldaps",
            LDAPS,
            "Use startTls",
            TLS);

    public LdapNetworkSettingsStage() {
        super();
    }

    public LdapNetworkSettingsStage(StageParameters stageParameters) {
        super(stageParameters);
    }

    @Override
    public Stage preconfigureStage(Stage stageToCheck,
            List<ConfigurationHandler> configurationHandlers) {
        return stageToCheck;
    }

    @Override
    public Stage validateStage(Stage ldapNetworkSettingsStage, Map<String, String> params) {
        Component ldapHostNameQ = ldapNetworkSettingsStage.getComponent(
                LDAP_HOST_NAME_CONFIGURATION_ID);
        Component ldapPortQ = ldapNetworkSettingsStage.getComponent(LDAP_PORT_CONFIGURATION_ID);
        Component ldapEncryptionMethodQ = ldapNetworkSettingsStage.getComponent(
                LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID);

        if (ldapHostNameQ.getValue() == null) {
            ldapHostNameQ.addError("LDAP host name cannot be empty.");
        }

        if (ldapPortQ.getValue() == null) {
            ldapPortQ.addError("LDAP port number cannot be empty.");
        }

        if (ldapEncryptionMethodQ.getValue() == null
                || !mapKeysToArray(LDAP_ENCRYPTION_METHODS_MAP.keySet()).contains(
                ldapEncryptionMethodQ.getValue()
                        .toString())) {
            ldapEncryptionMethodQ.addError("Invalid encryption method.");
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage testStage(Stage ldapNetworkSettingsStage,
            List<ConfigurationHandler> configurationHandlers, Map<String, String> params) {

        List<String> testErrors = new ArrayList<>();

        // TODO: tbatie - 10/25/16 - Maybe we should move params to the StageComposer level instead. It feels like the stages don't need to know anything about the request coming in. Any information they need should be contained inside the state
        boolean skipConnectionTest =
                params.get("skip") == null ? false : Boolean.getBoolean(params.get("skip"));
        if (!skipConnectionTest) {
            testErrors = getConfigurationHandler(configurationHandlers,
                    LDAP_CONFIGURATION_HANDLER_ID).test(LDAP_CONNECTION_TEST_ID,
                    ldapNetworkSettingsStage.getConfiguration());
        }

        if (!testErrors.isEmpty()) {
            for (String error : testErrors) {
                ldapNetworkSettingsStage.getRootComponent()
                        .subComponents(new ErrorMessageComponent(null).value(error));

            }

            ldapNetworkSettingsStage.getRootComponent()
                    .subComponents(new ButtonActionComponent().setMethod(POST)
                            .setUrl(getWizardUrl() + "?skip=true")
                            .label("skip"));
        }

        return ldapNetworkSettingsStage;

    }

    @Override
    public Stage commitStage(Stage stageToPersist, Map<String, String> params) {
        Configuration newConfiguration = stageToPersist.getConfiguration();
        if (newConfiguration == null) {
            newConfiguration = new Configuration();
        }

        newConfiguration.addValue(LDAP_HOST_NAME_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_HOST_NAME_CONFIGURATION_ID)
                        .getValue());
        newConfiguration.addValue(LDAP_PORT_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_PORT_CONFIGURATION_ID)
                        .getValue());

        newConfiguration.addValue(LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID,
                stageToPersist.getComponent(LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID)
                        .getValue());
        stageToPersist.setConfiguration(newConfiguration);
        return stageToPersist;
    }

    @Override
    public Component getDefaultRootComponent() {
        List encryptionMethods = Arrays.asList(LDAP_ENCRYPTION_METHODS_MAP.keySet()
                .toArray());

        return Component.builder("LDAP Network Settings", BASE_CONTAINER)
                .subComponents(new HostnameComponent(LDAP_HOST_NAME_CONFIGURATION_ID).label(
                        "LDAP Host name"), new PortComponent(LDAP_PORT_CONFIGURATION_ID).defaults(
                        389,
                        636)
                        .value(389)
                        .label("LDAP PortComponent"), new StringEnumComponent(
                        LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID).defaults(
                        LDAP_ENCRYPTION_METHODS_MAP.keySet())
                        .value((String) encryptionMethods.get(1))
                        .label("Encryption method"), (new ButtonActionComponent()).setUrl(
                        getWizardUrl() + "/" + getStageId())
                        .setMethod(POST)
                        .label("check"));
    }

    public List<String> mapKeysToArray(Set<String> keys) {

        List<String> array = new ArrayList<>();

        for (String key : keys) {
            array.add(key);
        }

        return array;
    }

    @Override
    public String getStageId() {
        return LDAP_NETWORK_SETTINGS_STAGE_ID;
    }

    @Override
    public Stage getNewInstance(StageParameters stageParameters) {
        return new LdapNetworkSettingsStage(stageParameters);
    }
}