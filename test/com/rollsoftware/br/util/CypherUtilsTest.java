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

import java.util.Arrays;
import java.util.List;
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
public class CypherUtilsTest {

    public CypherUtilsTest() {
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
     * Test of illegal call, of class Utils.
     *
     * @throws java.lang.Throwable
     */
    @Test(expected = java.lang.IllegalAccessException.class)
    public void testIllegalCall() throws Throwable {
        System.out.println("illegalCall");
        Object instance;
        Class clazz;

        String decrypted = "illegalCall";

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method encrypt
                    = clazz.getMethod("encrypt", new Class[]{String.class});
            encrypt.invoke(instance, decrypted);
        } catch (Throwable ex) {
            do {
                System.out.println("\t" + ex.getClass());
                ex = ex.getCause();
            } while (ex != null && ex.getCause() != null);
            throw ex;
        }
    }

    /**
     * Test of encrypt and decrypt method, of class Utils.
     */
    @Test
    public void testEncryptDecrypt() {
        System.out.println("encryptDecrypt");
        String text = "Hello World";
        String key = "^(}p<fCxH'z~X[~@g7jGx+Qvj%3}9KSB"; // 256 bit key
        String initVector = "^(}p<fCxH'z~X[~@g7jGx+Qvj%3}9KSB"; // 32 bytes IV
        String encrypt = CypherUtils.encrypt(key, initVector, text);
        String decrypt = CypherUtils.decrypt(key, initVector, encrypt);

        System.out.println("test plain: " + text);
        System.out.println("encrypt: " + encrypt);
        System.out.println("decrypt: " + decrypt);

        assertEquals(text, decrypt);
    }

    /**
     * Test of generateHash method, of class CypherUtils.
     */
    @Test
    public void testGenerateHash_ObjectArray() {
        System.out.println("generateHash");
        Object[] values = {1, "2", 3.0};

        String result0 = CypherUtils.generateHash(values);
        String result1 = CypherUtils.generateHash(values[0]);
        String result2 = CypherUtils.generateHash(values[1]);
        String result3 = CypherUtils.generateHash(values[2]);
        String result4 = CypherUtils.generateHash(values[2], values[1], values[0]);

        System.out.println("result0: " + result0);
        System.out.println("result1: " + result1);
        System.out.println("result2: " + result2);
        System.out.println("result3: " + result3);
        System.out.println("result4: " + result4);

        assertEquals(32, result0.length());
        assertEquals(32, result1.length());
        assertEquals(32, result2.length());
        assertEquals(32, result3.length());
        assertEquals(32, result4.length());
    }

    /**
     * Test of generateHash method, of class CypherUtils.
     */
    @Test
    public void testGenerateHash_List() {
        System.out.println("generateHash");
        Object[] values = {1, "2", 3.0};
        List valuesList = Arrays.asList(values);

        String result = CypherUtils.generateHash(valuesList);

        System.out.println("result: " + result);

        assertEquals(32, result.length());
    }

}
