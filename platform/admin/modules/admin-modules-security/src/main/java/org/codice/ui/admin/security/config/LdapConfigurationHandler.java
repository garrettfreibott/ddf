package org.codice.ui.admin.security.config;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.codice.ui.admin.security.api.ConfigurationHandler;

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

    public static final String LDAP_BASE_USERNAME_ATTRIBUTE_CONFIGURATION_ID = "ldapBaseUserNameAttribute";

    public static final String LDAP_BASE_GROUP_DN_CONFIGURATION_ID = "ldapBaseGroupDN";

    //  LDAP Bind Host Configuration Ids
    public static final String LDAP_BIND_USER_DN_CONFIGURATION_ID = "ldapBindUserDN";

    public static final String LDAP_BIND_USER_PASS_CONFIGURATION_ID = "ldapBindUserPassword";

    // Test Ids
    public static final String LDAP_CONNECTION_TEST_ID = "testLdapConnection";

    public static final String LDAP_BIND_TEST_ID = "testLdapBind";

    public static final String LDAP_DIRECTORY_STRUCT_TEST_ID = "testLdapDirStruct";


    public List<String> test(String testId, Configuration ldapConfiguration){
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
        return Arrays.asList(LDAP_CONNECTION_TEST_ID, LDAP_BIND_TEST_ID,
                LDAP_DIRECTORY_STRUCT_TEST_ID);
    }

    public List<String> testLdapConnection(Configuration ldapConfiguration) {

        String ldapHostname = (String) ldapConfiguration.getValue(LDAP_HOST_NAME_CONFIGURATION_ID);
        String ldapPort = (String) ldapConfiguration.getValue(LDAP_PORT_CONFIGURATION_ID);
        String encryptionMethod = (String) ldapConfiguration.getValue(LDAP_ENCRYPTION_METHOD_CONFIGURATION_ID);


        return new ArrayList<>();
    }

    public List<String> testLdapBind() {
        return new ArrayList<>();
    }

    public List<String> testLdapDirectoryStructure() {
        return new ArrayList<>();
    }

// ---- From SslLdapLoginModule ----
//
// protected LDAPConnectionFactory createLdapConnectionFactory(String url, Boolean startTls)
//            throws LdapException {
//
//
//        boolean useSsl = url.startsWith("ldaps");
//        boolean useTls = !url.startsWith("ldaps") && startTls;
//
//        LDAPOptions lo = new LDAPOptions();
//
//        try {
//            if (useSsl || useTls) {
//                SSLContext sslContext = SSLContext.getDefault();
//                lo.setSSLContext(sslContext);
//            }
//        } catch (GeneralSecurityException e) {
//            LOGGER.info("Error encountered while configuring SSL. Secure connection will fail.",
//                    e);
//        }
//
//        lo.setUseStartTLS(useTls);
//        lo.addEnabledCipherSuite(System.getProperty("https.cipherSuites")
//                .split(","));
//        lo.addEnabledProtocol(System.getProperty("https.protocols")
//                .split(","));
//        lo.setProviderClassLoader(SslLdapLoginModule.class.getClassLoader());
//
//        String host = url.substring(url.indexOf("://") + 3, url.lastIndexOf(":"));
//        Integer port = useSsl ? 636 : 389;
//        try {
//            port = Integer.valueOf(url.substring(url.lastIndexOf(":") + 1));
//        } catch (NumberFormatException ignore) {
//        }
//
//        return new LDAPConnectionFactory(host, port, lo);
//    }
}
