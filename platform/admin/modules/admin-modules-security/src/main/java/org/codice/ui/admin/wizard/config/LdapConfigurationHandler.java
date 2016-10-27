package org.codice.ui.admin.wizard.config;

import org.forgerock.opendj.ldap.Connection;
import org.forgerock.opendj.ldap.LDAPConnectionFactory;
import org.forgerock.opendj.ldap.LDAPOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.codice.ui.admin.wizard.api.ConfigurationHandler;
import org.forgerock.opendj.ldap.LdapException;
import org.forgerock.opendj.ldap.SearchScope;
import org.forgerock.opendj.ldap.requests.Requests;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

public class LdapConfigurationHandler implements ConfigurationHandler {

    public static final String LDAP_CONFIGURATION_HANDLER_ID = "ldapConfigurationHandler";

    public String getConfigurationHandlerId() {
        return LDAP_CONFIGURATION_HANDLER_ID;
    }

    public static final String LDAPS = "ldaps";

    public static final String TLS = "tls";

    public static final String NONE = "none";

    // LDAP Network Configuration Ids
    public static final String LDAP_HOST_NAME_CONFIGURATION_ID = "ldapHostName";

    public static final String LDAP_PORT_CONFIGURATION_ID = "ldapPort";

    public static final String LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID = "ldapEncryptionMethod";

    //  LDAP Directory Configuration Ids
    public static final String LDAP_BASE_USER_DN_CONFIGURATION_ID = "ldapBaseUserDN";

    public static final String LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID =
            "ldapBaseUserNameAttribute";

    public static final String LDAP_BASE_GROUP_DN_CONFIGURATION_ID = "ldapBaseGroupDN";

    //  LDAP Bind Host Configuration Ids
    public static final String LDAP_BIND_USER_DN_CONFIGURATION_ID = "ldapBindUserDN";

    public static final String LDAP_BIND_USER_PASS_CONFIGURATION_ID = "ldapBindUserPassword";

    // Test Ids
    public static final String LDAP_CONNECTION_TEST_ID = "testLdapConnection";

    public static final String LDAP_BIND_TEST_ID = "testLdapBind";

    public static final String LDAP_DIRECTORY_STRUCT_TEST_ID = "testLdapDirStruct";

    public List<String> test(String testId, Configuration ldapConfiguration) {
        switch (testId) {
        case LDAP_CONNECTION_TEST_ID:
            return testLdapConnection(ldapConfiguration);
        case LDAP_BIND_TEST_ID:
            return testLdapBind();
        case LDAP_DIRECTORY_STRUCT_TEST_ID:
            return testLdapDirectoryStructure();
        }

        return Arrays.asList("No found test for " + testId);
    }

    public List<String> persist(Configuration config) {
        return new ArrayList<>();
    }

    List<String> getTestIds() {
        return Arrays.asList(LDAP_CONNECTION_TEST_ID,
                LDAP_BIND_TEST_ID,
                LDAP_DIRECTORY_STRUCT_TEST_ID);
    }

    public List<String> testLdapConnection(Configuration ldapConfiguration) {

        List<String> errors = cannotBeNullFields(ldapConfiguration.getValues(), new ArrayList<>());
        if (!errors.isEmpty()) {
            return errors;
        }

        String ldapHostname = (String) ldapConfiguration.getValue(LDAP_HOST_NAME_CONFIGURATION_ID);
        int ldapPort = (int) ldapConfiguration.getValue(LDAP_PORT_CONFIGURATION_ID);
        String encryptionMethod = (String) ldapConfiguration.getValue(
                LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID);

        try {
            Connection ldapConnection = getLdapConnection(ldapHostname, ldapPort, encryptionMethod);
            ConnectionEntryReader result = ldapConnection.search(Requests.newSearchRequest(
                    "dc=example,dc=com",
                    SearchScope.WHOLE_SUBTREE,
                    "(sn=Jensen)",
                    "cn"));

        } catch (Exception e) {
            errors.add("Connection refused.");
        }

        return errors;
    }


    public Connection getLdapConnection(String ldapHostName, int ldapPort, String encryptionMethod)
            throws LdapException {

        LDAPOptions ldapOptions = new LDAPOptions();

        if (encryptionMethod.equals(LDAPS)) {
            try {
                ldapOptions.setSSLContext(SSLContext.getDefault());
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if(encryptionMethod.equals(TLS)) {
            ldapOptions.setUseStartTLS(true);
        }

        ldapOptions.addEnabledCipherSuite(System.getProperty("https.cipherSuites")
                .split(","));
        ldapOptions.addEnabledProtocol(System.getProperty("https.protocols")
                .split(","));
        ldapOptions.setProviderClassLoader(LdapConfigurationHandler.class.getClassLoader());
        LDAPConnectionFactory ldapConnection = new LDAPConnectionFactory(ldapHostName,
                ldapPort,
                ldapOptions);

        return ldapConnection.getConnection();
    }

    public List<String> testLdapBind() {
        return new ArrayList<>();
    }

    public List<String> testLdapDirectoryStructure() {
        return new ArrayList<>();
    }

    public List<String> cannotBeNullFields(Map<String, Object> config, List<String> errors) {
        for (Map.Entry<String, Object> configEntry : config.entrySet()) {
            if (configEntry.getValue() == null) {
                errors.add("Entry " + configEntry.getKey() + " cannot be empty");
            }
        }

        return errors;
    }
}
