/*
 *          Copyright 2016-2026 Rogério Lecarião Leite
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  CEO 2016: Rogério Lecarião Leite; ROLL Software
 */
package com.rollsoftware.br.common.db.internal.derby;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public class DerbyPropertiesTest {

    public DerbyPropertiesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getProperties method, of class ROLLSoftwareProperties.
     */
    @Test
    public void testGetProperties() {
        System.out.println("getProperties");
        Properties properties = DerbyProperties.getProperties();
        assertTrue("Properties not working!", !properties.isEmpty());
    }

    /**
     * Test of getProperties method, of class ROLLSoftwareProperties.
     */
    /*
     * Test:
     * test=test
     * test.test=${test}.${test}
     * test.insidetest=${test.${test}}
     * Expect:
     * test=test
     * test.test=test.test
     * test.insidetest=test.test
     */
    @Test
    public void testGetPropertiesPrint() {
        System.out.println("testGetPropertiesPrint");
        Properties properties = DerbyProperties.getProperties();

        properties.entrySet().forEach((entry) -> {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        });
    }

}
