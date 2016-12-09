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
 **/
package org.codice.ui.admin.wizard.config.handlers

import org.codice.ui.admin.wizard.config.ConfiguratorException
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

class PropertyConfigHandlerTest extends Specification {
    @Rule
    TemporaryFolder tempFolder
    @Shared
    File file

    def setup() {
        file = tempFolder.newFile('test.properties')
        def initProps = new Properties()
        initProps.put('key1', 'val1')
        initProps.put('key2', 'val2')
        initProps.put('key3', 'val3')
        file.newWriter().with {
            initProps.store(it, null)
        }
    }

    def 'test write properties to an unknown file fails'() {
        setup:
        def configs = [key1: 'newVal1', key4: 'val4', key5: 'val5']
        def badFile = new File(file, "doesnotexist")

        when:
        def handler = PropertyConfigHandler.instance(badFile.toPath(), configs, true)

        then:
        thrown(ConfiguratorException)
    }

    def 'test write new properties and keep old properties'() {
        setup:
        def configs = [key1: 'newVal1', key4: 'val4', key5: 'val5']
        def handler = PropertyConfigHandler.instance(file.toPath(), configs, true)
        def props = new Properties()

        when:
        handler.commit()
        file.newReader().with {
            props.load(it)
        }

        then:
        props.getProperty('key1') == 'newVal1'
        props.getProperty('key2') == 'val2'
        props.getProperty('key3') == 'val3'
        props.getProperty('key4') == 'val4'
        props.getProperty('key5') == 'val5'
    }

    def 'test write new properties and remove old properties'() {
        setup:
        def configs = [key1: 'newVal1', key4: 'val4', key5: 'val5']
        def handler = PropertyConfigHandler.instance(file.toPath(), configs, false)
        def props = new Properties()

        when:
        handler.commit()
        file.newReader().with {
            props.load(it)
        }

        then:
        props.getProperty('key1') == 'newVal1'
        props.getProperty('key4') == 'val4'
        props.getProperty('key5') == 'val5'
    }

    def 'test rollback'() {
        setup:
        def configs = [key1: 'newVal1', key4: 'val4', key5: 'val5']
        def handler = PropertyConfigHandler.instance(file.toPath(), configs, false)
        def props = new Properties()

        when:
        handler.commit()
        handler.rollback()
        file.newReader().with {
            props.load(it)
        }

        then:
        props.getProperty('key1') == 'val1'
        props.getProperty('key2') == 'val2'
        props.getProperty('key3') == 'val3'
        props.getProperty('key4') == null
        props.getProperty('key5') == null
    }
}