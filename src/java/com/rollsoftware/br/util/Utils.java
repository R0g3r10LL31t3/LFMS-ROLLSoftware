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

import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public final class Utils {

    private static final SimpleDateFormat TIMESTAMP;
    private static final SimpleDateFormat HOUR;

    static {
        TIMESTAMP = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        HOUR = new SimpleDateFormat("HH:mm:ss.SSS");
    }

    private Utils() {
    }

    public static URL getResource(String name) {
        return Utils.class.getClassLoader().getResource(name);
    }

    public static InputStream getResourceAsStream(String name) {
        return Utils.class.getClassLoader().getResourceAsStream(name);
    }

    public static void addShutdownHook(Runnable runnable) {

        Runtime.getRuntime().addShutdownHook(
                new Thread() {
            @Override
            public void run() {
                AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    runnable.run();
                    return null;
                });
            }
        });
    }

    public static String getCurrentTimeStampFormatted() {
        return TIMESTAMP.format(new Date(System.currentTimeMillis()));
    }

    public static String formatToHour(long value) {
        return HOUR.format(value);
    }

    public static String formatToTimestamp(long value) {
        return TIMESTAMP.format(value);
    }

    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getTimeZone("GMT-3");
    }

    public static String replace(
            String target, Map<String, String> replacements) {

        String oldTarget;
        while (target.contains("${")) {
            oldTarget = target;
            int indexOf1 = target.lastIndexOf("${");
            int indexOf2 = target.indexOf("}", indexOf1);
            String replace = target.substring(
                    indexOf1, indexOf2 + 1);
            String key = replace.substring(
                    2, replace.length() - 1);
            String value = replacements.get(key);
            target = target.replace(replace, value);

            if (oldTarget.equals(target)) {
                throw new IllegalArgumentException();
            }
        }

        return target;
    }
}
