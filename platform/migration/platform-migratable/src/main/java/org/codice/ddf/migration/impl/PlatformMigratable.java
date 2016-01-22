/**
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.migration.impl;

import static org.apache.commons.lang.Validate.notNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codice.ddf.migration.Migratable;
import org.codice.ddf.migration.MigrationException;
import org.codice.ddf.migration.MigrationMetadata;
import org.codice.ddf.migration.MigrationWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles the export process for all Platform configurations and system files.
 *
 */
public class PlatformMigratable implements Migratable {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PlatformMigratable.class);

    private static final String KEYSTORE_SYSTEM_PROP = "javax.net.ssl.keyStore";

    private static final String TRUSTSTORE_SYSTEM_PROP = "javax.net.ssl.trustStore";

    private static final String WS_SECURITY_DIR = "etc/ws-security";

    private static final String SYSTEM_PROPERTIES = "etc/system.properties";

    private static final String USERS_PROPERTIES = "etc/users.properties";
    
    private static final String ABSOLUTE_PATH_WARNING =
            "The value for property [%s] is set to a path [%s] that is absolute; "
                    + "therefore, the file will not be included in the export.  "
                    + "Check that the file exists on the system you're migrating to "
                    + "or update the property value and export again.";

    private static final String OUTSIDE_PATH_WARNING =
            "The value for property [%s] is set to a path [%s] that is outside [%s]; "
                    + "therefore, the file will not be included in the export.  "
                    + "Check that the file exists on the system you're migrating to "
                    + "or update the property value and export again.";

    private static final String UNREAL_PATH_WARNING =
            "The value for property [%s] is set to a path [%s] that could not coerced into a real path; "
                    + "therefore, the file will not be included in the export.  "
                    + "Check that the file exists on the system you're migrating to "
                    + "or update the property value and export again.";

    private final Path ddfHome;
    
    public PlatformMigratable(@NotNull Path ddfHome) {
        notNull(ddfHome, "ddfHome cannot be null");
        this.ddfHome = ddfHome;
    }
    
    @Override
    @NotNull
    public MigrationMetadata export(@NotNull Path exportPath) throws MigrationException {
        notNull(exportPath, "exportPath cannot be null");
        exportSystemFiles(exportPath);

        Collection<MigrationWarning> migrationWarnings = new ArrayList<>();
        migrationWarnings.addAll(exportSecurity(exportPath));
        return new MigrationMetadata(migrationWarnings);
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Exports all Platform configurations and system files";
    }

    @Override
    public boolean isOptional() {
        return false;
    }
    
    private Collection<MigrationWarning> exportSecurity(Path exportDirectory) {
        exportDirectory(exportDirectory, WS_SECURITY_DIR);

        Collection<MigrationWarning> migrationWarnings = new ArrayList<>();
        migrationWarnings.addAll(exportKeystores(exportDirectory));
        return migrationWarnings;
    }

    private void exportSystemFiles(Path exportDirectory) {
        copyFile(ddfHome.resolve(SYSTEM_PROPERTIES), exportDirectory.resolve(SYSTEM_PROPERTIES));
        copyFile(ddfHome.resolve(USERS_PROPERTIES), exportDirectory.resolve(USERS_PROPERTIES));
    }

    private Collection<MigrationWarning> exportKeystores(Path exportDirectory) {
        Collection<MigrationWarning> migrationWarnings = new ArrayList<>();
        migrationWarnings.addAll(exportExternalFile(exportDirectory, KEYSTORE_SYSTEM_PROP));
        migrationWarnings.addAll(exportExternalFile(exportDirectory, TRUSTSTORE_SYSTEM_PROP));
        return migrationWarnings;
    }

    private Collection<MigrationWarning> exportExternalFile(Path exportDirectory,
            String propertyWithPath) {
        Collection<MigrationWarning> migrationWarnings = new ArrayList<>();
        String keystore = getProperty(propertyWithPath);
        migrationWarnings.addAll(checkIfPathIsMigratable(propertyWithPath, Paths.get(keystore)));
        if (migrationWarnings.isEmpty()) {
            Path source = ddfHome.resolve(keystore);
            copyFile(source, constructDestination(source, exportDirectory));
        }
        return migrationWarnings;
    }

    private Path constructDestination(Path pathToExport, Path exportDirectory) {
        Path destination = exportDirectory.resolve(ddfHome.relativize(pathToExport));
        return destination;
    }

    private void exportDirectory(Path destinationRoot, String directoryToCopy) {
        Path source = ddfHome.resolve(Paths.get(directoryToCopy));
        copyDirectory(source, constructDestination(source, destinationRoot));
    }

    private String getProperty(String property) throws MigrationException {
        String prop = System.getProperty(property);
        if (StringUtils.isBlank(prop)) {
            String message = String.format("System property %s is not set.", property);
            LOGGER.error(message);
            throw new MigrationException(message);
        }

        return prop;
    }

    private void copyFile(Path source, Path destination) throws MigrationException {
        try {
            FileUtils.copyFile(source.toFile(), destination.toFile());
        } catch (IOException e) {
            String message = String.format("Unable to copy [%s] to [%s].", source.toString(),
                    destination.toString());
            LOGGER.error(message, e);
            throw new MigrationException(message, e);
        }
    }

    private void copyDirectory(Path source, Path destination) throws MigrationException {
        try {
            FileUtils.copyDirectory(source.toFile(), destination.toFile());
        } catch (IOException e) {
            String message = String
                    .format("Unable to copy [%s] to [%s].", source.toAbsolutePath().toString(),
                            source.toAbsolutePath().toString());
            LOGGER.error(message, e);
            throw new MigrationException(message, e);
        }
    }

    /*
        Checks is a file is able to be migrated.  Returns warnings if the path is absolute,
        if the path leads somewhere outside DDF Home, or if there is an issue turning it into
        a real path.
     */
    private Collection<MigrationWarning> checkIfPathIsMigratable(String propertyName, Path path) {
        Collection<MigrationWarning> migrationWarnings = new ArrayList<>();
        try {
            if (path.isAbsolute()) {
                String message = String
                        .format(ABSOLUTE_PATH_WARNING, propertyName, path.toString());
                LOGGER.debug(message);
                migrationWarnings.add(new MigrationWarning(message));
            } else if (!getRealPath(ddfHome.resolve(path)).startsWith(ddfHome)) {
                String message = String.format(OUTSIDE_PATH_WARNING, propertyName, path.toString(),
                        ddfHome.toString());
                LOGGER.debug(message);
                migrationWarnings.add(new MigrationWarning(message));
            }
        } catch (MigrationException e) {
            String message = String.format(UNREAL_PATH_WARNING, propertyName, path.toString());
            LOGGER.debug(message);
            migrationWarnings.add(new MigrationWarning(message));
        }
        return migrationWarnings;
    }

    Path getRealPath(Path path) throws MigrationException {
        try {
            Path realPath = path.toRealPath();
            return realPath;
        } catch (IOException e) {
            String message = String
                    .format("Unable to construct real path from [%s].", path.toString());
            LOGGER.error(message, e);
            throw new MigrationException(message, e);
        }
    }

}
