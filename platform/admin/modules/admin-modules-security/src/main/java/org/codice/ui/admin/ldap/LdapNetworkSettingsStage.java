package org.codice.ui.admin.ldap;

import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_CONFIGURATION_HANDLER_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_CONNECTION_TEST_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_HOST_NAME_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAP_PORT_CONFIGURATION_ID;
import static org.codice.ui.admin.wizard.stage.components.ButtonActionComponent.Method.POST;
import static org.codice.ui.admin.wizard.stage.components.Component.ComponentType.BASE_CONTAINER;

import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.NONE;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.LDAPS;
import static org.codice.ui.admin.wizard.config.LdapConfigurationHandler.TLS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;
import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.codice.ui.admin.wizard.config.Configuration;
import org.codice.ui.admin.wizard.stage.Stage;
import org.codice.ui.admin.wizard.stage.StageParameters;
import org.codice.ui.admin.wizard.stage.components.ButtonActionComponent;
import org.codice.ui.admin.wizard.stage.components.Component;
import org.codice.ui.admin.wizard.stage.components.ErrorInfoComponent;
import org.codice.ui.admin.wizard.stage.components.HostnameComponent;
import org.codice.ui.admin.wizard.stage.components.PortComponent;
import org.codice.ui.admin.wizard.stage.components.StringEnumComponent;

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

        // validate fields
        Optional<HostnameComponent> ldapHostName = ldapNetworkSettingsStage.getComponent(
                LDAP_HOST_NAME_CONFIGURATION_ID,
                HostnameComponent.class);

        Optional<PortComponent> ldapPort = ldapNetworkSettingsStage.getComponent(
                LDAP_PORT_CONFIGURATION_ID,
                PortComponent.class);

        Optional<StringEnumComponent> encryptMethod = ldapNetworkSettingsStage.getComponent(
                LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID,
                StringEnumComponent.class);

        if (!ldapHostName.isPresent() || StringUtils.isEmpty(ldapHostName.get().getValue())) {
            ldapNetworkSettingsStage.addErrorToComponent(LDAP_HOST_NAME_CONFIGURATION_ID,
                    "LDAP host name cannot be empty.");
        }

        if (!ldapPort.isPresent() || ldapPort.get().getValue() == null) {
            ldapNetworkSettingsStage.addErrorToComponent(LDAP_PORT_CONFIGURATION_ID,
                    "LDAP port number cannot be empty.");
        }

        if (!encryptMethod.isPresent() || StringUtils.isEmpty(encryptMethod.get().getValue())) {
            // TODO: tbatie - 10/26/16 - Check if its in the encryption method list
            ldapNetworkSettingsStage.addErrorToComponent(LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID,
                    "Invalid encryption method.");
        }

        return ldapNetworkSettingsStage;
    }

    @Override
    public Stage testStage(Stage ldapNetworkSettingsStage,
            List<ConfigurationHandler> configurationHandlers, Map<String, String> params) {

        String ldapHostName = ldapNetworkSettingsStage.getComponent(
                LDAP_HOST_NAME_CONFIGURATION_ID,
                HostnameComponent.class).get().getValue();

        int ldapPort = ldapNetworkSettingsStage.getComponent(
                LDAP_PORT_CONFIGURATION_ID,
                PortComponent.class).get().getValue();

        String encryptMethod = ldapNetworkSettingsStage.getComponent(
                LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID,
                StringEnumComponent.class).get().getValue();

        Configuration testConfiguration = ldapNetworkSettingsStage.getConfiguration();
        testConfiguration.addValue(LDAP_HOST_NAME_CONFIGURATION_ID, ldapHostName);
        testConfiguration.addValue(LDAP_PORT_CONFIGURATION_ID, ldapPort);
        testConfiguration.addValue(LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID, encryptMethod);

        List<String> testErrors = getConfigurationHandler(configurationHandlers,
                LDAP_CONFIGURATION_HANDLER_ID).test(LDAP_CONNECTION_TEST_ID, testConfiguration);

        if (!testErrors.isEmpty()) {
            for (String error : testErrors) {
                ldapNetworkSettingsStage.getRootComponent()
                        .subComponents(new ErrorInfoComponent(null).value(error));

            }

            ldapNetworkSettingsStage.getRootComponent()
                    .subComponents(new ButtonActionComponent().setMethod(POST)
                            .setUrl(getWizardUrl() + "?skipTest=true")
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

    public void addErrorToComponent(Stage stage, String componentId, String errorMsg) {
        stage.getComponent(LDAP_HOST_NAME_CONFIGURATION_ID)
                .addError("LDAP host name cannot be empty.");
    }
}