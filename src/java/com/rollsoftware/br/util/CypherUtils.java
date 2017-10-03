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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public final class CypherUtils {

    private CypherUtils() {
    }

    private static String encrypt0(String decrypted) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method encrypt
                    = clazz.getMethod("encrypt", new Class[]{String.class});
            return (String) encrypt.invoke(instance, decrypted);
        } catch (Throwable ex) {
            return decrypted;
        }
    }

    private static String encrypt0(
            String key, String initVector, String decrypted) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method encrypt
                    = clazz.getMethod("encrypt", new Class[]{
                String.class, String.class, String.class
            });
            return (String) encrypt.invoke(instance, key, initVector, decrypted);
        } catch (Throwable ex) {
            return decrypted;
        }
    }

    private static String decrypt0(String encrypted) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method decrypt
                    = clazz.getMethod("decrypt", new Class[]{String.class});
            return (String) decrypt.invoke(instance, encrypted);
        } catch (Throwable ex) {
            return encrypted;
        }
    }

    private static String decrypt0(
            String key, String initVector, String encrypted) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method decrypt
                    = clazz.getMethod("decrypt", new Class[]{
                String.class, String.class, String.class
            });
            return (String) decrypt.invoke(instance, key, initVector, encrypted);
        } catch (Throwable ex) {
            return encrypted;
        }
    }

    private static String generateHash0(Object... values) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method decrypt
                    = clazz.getMethod("generateHash", new Class[]{
                Object[].class
            });
            return (String) decrypt.invoke(instance, new Object[]{values});
        } catch (Throwable ex) {
            return String.valueOf(Objects.hash(values));
        }
    }

    private static String generateHash0(List values) {
        Object instance;
        Class clazz;

        try {
            clazz = Class.forName("com.rollsoftware.br.cipher.CypherUtils");
            java.lang.reflect.Constructor constructor
                    = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            java.lang.reflect.Method decrypt
                    = clazz.getMethod("generateHash", new Class[]{
                List.class
            });
            return (String) decrypt.invoke(instance, values);
        } catch (Throwable ex) {
            return String.valueOf(Objects.hash(values));
        }
    }

    public static String encrypt(String decrypted) {
        return encrypt0(decrypted);
    }

    public static String encrypt(
            String key, String initVector, String decrypted) {
        return encrypt0(key, initVector, decrypted);
    }

    public static String decrypt(String encrypted) {
        return decrypt0(encrypted);
    }

    public static String decrypt(
            String key, String initVector, String encrypted) {
        return decrypt0(key, initVector, encrypted);
    }

    public static String generateHash(Object... values) {
        return generateHash0(values);
    }

    public static String generateHash(List values) {
        return generateHash0(values);
    }

    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        return generateHash(uuid);
    }
}
