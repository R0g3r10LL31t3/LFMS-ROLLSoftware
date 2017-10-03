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
package com.rollsoftware.br.util;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public class UtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public UtilsTest() {
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
     * Test of replace method, of class Utils.
     */
    @Test
    public void testReplace() {
        System.out.println("replace");

        Map<String, String> replacements = new HashMap();

        replacements.put("test", "test");
        replacements.put("test.${test}", "${test}.${test}");
        replacements.put("test.test.test", "${test.${test}}.${test}");
        Map<String, String> copyReplacements;

        copyReplacements = new HashMap(replacements);
        copyReplacements.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = entry.getValue();
            String oldKey = key;
            key = Utils.replace(key, replacements);
            if (!oldKey.equals(key)) {
                replacements.remove(oldKey);
                replacements.put(key, value);
            }
        });

        copyReplacements = new HashMap(replacements);
        copyReplacements.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            String value = entry.getValue();
            String oldValue = value;
            value = Utils.replace(value, replacements);
            if (!oldValue.equals(value)) {
                replacements.put(key, value);
            }
        });

        replacements.entrySet().forEach((entry) -> {
            System.out.println("entry: " + entry);
            assertTrue(entry.getKey(),
                    !entry.getKey().contains("${")
                    && !entry.getKey().contains("}"));
            assertTrue(entry.getValue(),
                    !entry.getValue().contains("${")
                    && !entry.getValue().contains("}"));
        });

    }
}
