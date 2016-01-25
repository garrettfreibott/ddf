/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.security.migratable.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.codice.ddf.migration.MigrationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

@PrepareForTest({SecurityMigratable.class, FileUtils.class})
public class SecurityMigratableTest {

    private static final Path DDF_BASE_DIR = Paths.get("ddf");

    private static final String KEYSTORE_REL_PATH = "etc/keystores/keystore.jks";

    private static final String INVALID_KEYSTORE_REL_PATH = "invalidKeystore.jks";

    private static final String KEYSTORES_DIR = "etc/keystores";

    private static final String TRUSTSTORE_REL_PATH = "etc/keystores/truststore.jks";

    private static final String INVALID_TRUSTSTORE_REL_PATH = "invalidTruststore.jks";

    private static final String WS_SECURITY_SERVER_DIR = "etc/ws-security/server";

    private static final String WS_SECURITY_SERVER_ENC_PROP_FILE_REL_PATH =
            WS_SECURITY_SERVER_DIR + "/encryption.properties";

    private static final String WS_SECURITY_SERVER_SIG_PROP_FILE_REL_PATH =
            WS_SECURITY_SERVER_DIR + "/signature.properties";

    private static final String WS_SECURITY_ISSUER_DIR = "etc/ws-security/issuer";

    private static final String WS_SECURITY_ISSUER_ENC_PROP_FILE_REL_PATH =
            WS_SECURITY_ISSUER_DIR + "/encryption.properties";

    private static final String WS_SECURITY_ISSUER_SIG_PROP_FILE_REL_PATH =
            WS_SECURITY_ISSUER_DIR + "/signature.properties";

    private static final String SECURITY_DIRECTORY_REL_PATH = "security";

    private static final String PDP_POLICIES_DIR_REL_PATH = "etc/pdp";

    private static final String CRL_DIR = "etc/certs/demoCA/crl";

    private static final String CRL_REL_PATH = "etc/certs/demoCA/crl/crl.pem";

    private static final String INVALID_CRL_REL_PATH = "invalidCrl.pem";

    private static final String CRL_PROP_KEY = "org.apache.ws.security.crypto.merlin.x509crl.file";

    private static final String EXPORTED_REL_PATH = "etc/exported";

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    private Path ddfHome;

    private Path exportDirectory;

    private Path existingFilePath;

    private Path symLinkPath;

    @Before
    public void setup() throws Exception {
        mockStatic(FileUtils.class);
        ddfHome = Paths.get(tempDir.getRoot()
                .getAbsolutePath() + File.separator + DDF_BASE_DIR);
        exportDirectory = ddfHome.resolve(EXPORTED_REL_PATH);
    }

    @Test
    public void testExportCrlAbsolutePath() throws Exception {
        // Setup
        Path crlAbsolutePath = ddfHome.resolve(Paths.get(CRL_REL_PATH));
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY, crlAbsolutePath.toString());
                return properties;
            }
        };

        // Perform Test
        assertThat("A migration warning wasn't returned.",
                securityMigratable.export(exportDirectory)
                        .getMigrationWarnings(),
                is(not(empty())));
    }

    @Test
    public void testDecriptorsSet() throws Exception {
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf());

        assertThat(securityMigratable.getDescription(), not(isEmptyOrNullString()));
        assertThat(securityMigratable.isOptional(), not(isNull()));
    }

    @Test
    public void testExportInvalidCrlRelativePath() throws Exception {
        // Setup
        Path invalidCrlRelativePath = tempDir.getRoot()
                .toPath()
                .resolve(INVALID_CRL_REL_PATH);
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY,
                        ddfHome.relativize(invalidCrlRelativePath)
                                .toString());
                return properties;
            }
        };

        // Perform Test
        assertThat("A migration warning wasn't returned.",
                securityMigratable.export(exportDirectory)
                        .getMigrationWarnings(),
                is(not(empty())));
    }

    @Test
    public void testSymbolicLinkPath() throws Exception {
        //setup
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY, symLinkPath.toString());
                return properties;
            }
        };

        assertThat("A migration warning wasn't returned.",
                securityMigratable.export(symLinkPath)
                        .getMigrationWarnings(),
                is(not(empty())));
    }

    @Test(expected = MigrationException.class)
    public void testExportExceptionReadingCrl() throws Exception {
        // Setup
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                throw new MigrationException("Error reading CRL");
            }
        };

        // Perform Test
        securityMigratable.export(exportDirectory);
    }

    /**
     * The crl path can be commented out in <ddf home>/etc/ws-security/server/encryption.properties
     * if not in use. This test verifies that no exception is thrown if the crl path is not found.
     */
    @Test
    public void testExportCrlPathNotSet() throws Exception {
        // Setup
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                return new Properties();
            }
        };

        // Perform Test
        securityMigratable.export(exportDirectory);
    }

    @Test(expected = MigrationException.class)
    public void testExportExceptionCopyingFile() throws Exception {
        // Setup
        doThrow(new IOException("Error copying file")).when(FileUtils.class);
        FileUtils.copyFile(any(File.class), any(File.class));
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY, CRL_REL_PATH);
                return properties;
            }
        };

        // Perform Test
        securityMigratable.export(exportDirectory);
    }

    @Test(expected = MigrationException.class)
    public void testExportExceptionCopyingDirectory() throws Exception {
        // Setup
        doThrow(new IOException("Error copying directory")).when(FileUtils.class);
        FileUtils.copyDirectory(any(File.class), any(File.class));
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {

            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY, CRL_REL_PATH);
                return properties;
            }
        };

        // Perform Test
        securityMigratable.export(exportDirectory);
    }

    @Test
    public void testExportValidRelativePaths() throws Exception {
        // Setup
        SecurityMigratable securityMigratable = new SecurityMigratable(createTempDdf()) {
            @Override
            Properties readPropertiesFile(Path propertiesFile) throws MigrationException {
                Properties properties = new Properties();
                properties.setProperty(CRL_PROP_KEY, CRL_REL_PATH);
                return properties;
            }
        };

        // Perform Test
        securityMigratable.export(exportDirectory);

        // Verify
        ArgumentCaptor<File> sourceDirCaptor = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<File> destinationDirCaptor = ArgumentCaptor.forClass(File.class);
        verifyStatic(times(1));
        FileUtils.copyDirectory(sourceDirCaptor.capture(), destinationDirCaptor.capture());
        List<File> destinationDirs = destinationDirCaptor.getAllValues();
        assertDestinationDirectory(destinationDirs);

        ArgumentCaptor<File> sourceFileCaptor = ArgumentCaptor.forClass(File.class);
        ArgumentCaptor<File> destinationFileCaptor = ArgumentCaptor.forClass(File.class);
        verifyStatic(times(1));
        FileUtils.copyFile(sourceFileCaptor.capture(), destinationFileCaptor.capture());
        List<File> destinationFiles = destinationFileCaptor.getAllValues();
        assertDestinationFile(destinationFiles);
    }

    private void assertDestinationFile(List<File> destinationFiles) {
        assertThat(destinationFiles.size(), equalTo(1));
        assertThat(destinationFiles,
                contains(new File(exportDirectory + File.separator + CRL_REL_PATH)));
    }

    private void assertDestinationDirectory(List<File> destinationDirs) {
        assertThat(destinationDirs.size(), equalTo(1));
        assertThat(destinationDirs,
                contains(new File(exportDirectory + File.separator + PDP_POLICIES_DIR_REL_PATH)));
    }

    private Path createTempDdf() throws IOException {

        Path rootTempDir = tempDir.getRoot()
                .toPath();
        Path ddfHome = rootTempDir.resolve(DDF_BASE_DIR);
        Files.createDirectories(ddfHome);
        Files.createDirectories(ddfHome.resolve(SECURITY_DIRECTORY_REL_PATH));
        Files.createDirectories(ddfHome.resolve(KEYSTORES_DIR));
        Files.createFile(ddfHome.resolve(Paths.get(KEYSTORE_REL_PATH)));
        Files.createFile(ddfHome.resolve(Paths.get(TRUSTSTORE_REL_PATH)));
        Files.createDirectories(ddfHome.resolve(WS_SECURITY_SERVER_DIR));
        Files.createFile(ddfHome.resolve(Paths.get(WS_SECURITY_SERVER_ENC_PROP_FILE_REL_PATH)));
        Files.createFile(ddfHome.resolve(Paths.get(WS_SECURITY_SERVER_SIG_PROP_FILE_REL_PATH)));
        Files.createDirectories(ddfHome.resolve(WS_SECURITY_ISSUER_DIR));
        Files.createFile(ddfHome.resolve(Paths.get(WS_SECURITY_ISSUER_ENC_PROP_FILE_REL_PATH)));
        Files.createFile(ddfHome.resolve(Paths.get(WS_SECURITY_ISSUER_SIG_PROP_FILE_REL_PATH)));
        Files.createDirectories(ddfHome.resolve(PDP_POLICIES_DIR_REL_PATH));
        Files.createDirectories(ddfHome.resolve(CRL_DIR));
        Files.createFile(ddfHome.resolve(Paths.get(CRL_REL_PATH)));

        Files.createFile(rootTempDir.resolve(INVALID_KEYSTORE_REL_PATH));
        Files.createFile(rootTempDir.resolve(INVALID_TRUSTSTORE_REL_PATH));
        Files.createFile(rootTempDir.resolve(INVALID_CRL_REL_PATH));

        existingFilePath = Paths.get(ddfHome.resolve("orig.config")
                .toString());
        symLinkPath = Paths.get(ddfHome.resolve("symlink.config")
                .toString());
        Files.createFile(existingFilePath);
        Files.createSymbolicLink(symLinkPath, existingFilePath);

        return ddfHome;
    }
}
