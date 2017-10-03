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

import com.rollsoftware.br.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
public class DerbyProperties {

    private static final String USER_DIR
            = System.getProperty("user.dir");
    private static final String JETTY_BASE
            = System.getProperty("jetty.base");
    private static final String JETTY_HOME
            = System.getProperty("jetty.home");
    private static final String CATALINA_BASE
            = System.getProperty("catalina.base");
    private static final String CATALINA_HOME
            = System.getProperty("catalina.home");

    private DerbyProperties() {
    }

    private static void regenerate(Map map) {
        Map copy = new HashMap(map);
        copy.entrySet().forEach((item) -> {
            Map.Entry entry = (Map.Entry) item;
            String getKey = (String) entry.getKey();
            String getValue = (String) entry.getValue();

            if (getKey.endsWith(".encrypted")) {
                map.remove(getKey);
                getKey = getKey.replace(".encrypted", "");
                getValue
                        = com.rollsoftware.br.util.CypherUtils.decrypt(getValue);
                map.put(getKey, getValue);
            }
        });
    }

    private static void copy(Map src, Map dst) {
        src.entrySet().forEach((item) -> {
            Map.Entry entry = (Map.Entry) item;
            String getKey = (String) entry.getKey();
            String getValue = (String) entry.getValue();
            dst.put(getKey, getValue);
        });
    }

    private static void updateDerbySystemHome(Map map) {
        File path = null;

        if (CATALINA_BASE != null) {
            path = new File(CATALINA_BASE
                    + File.separator + map.get("derby.system.home"));
        } else if (JETTY_BASE != null) {
            path = new File(JETTY_BASE
                    + File.separator + map.get("derby.system.home"));
        } else {
            path = new File(USER_DIR
                    + File.separator + map.get("derby.system.home"));
        }
        if (path != null) {
            map.put("derby.system.home", path.toPath().toString());
            File pathLog = new File(path.toPath()
                    + File.separator + map.get("derby.stream.error.file"));
            map.put("derby.stream.error.file", pathLog.toPath().toString());
            System.setProperty("derby.system.home",
                    (String) map.get("derby.system.home"));
            System.setProperty("derby.stream.error.file",
                    (String) map.get("derby.stream.error.file"));
        }
    }

    public static Properties getProperties() {
        if (Singleton.INSTANCE.isEmpty()) {
            try {

                Properties properties = new Properties();
                Map<String, String> replacements = new HashMap();

                java.io.InputStream inputStream = Utils.getResourceAsStream(
                        "com/rollsoftware/br/common"
                        + "/db/internal/derby/derby.properties");
                properties.load(inputStream);

                regenerate(properties);

                copy(properties, replacements);

                properties.entrySet().forEach((entry) -> {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    String oldKey = key;
                    key = Utils.replace(key, replacements);
                    if (!oldKey.equals(key)) {
                        replacements.remove(oldKey);
                        replacements.put(key, value);
                    }
                });

                properties.clear();
                copy(replacements, properties);

                properties.entrySet().forEach((entry) -> {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    String oldValue = value;
                    value = Utils.replace(value, replacements);
                    if (!oldValue.equals(value)) {
                        replacements.put(key, value);
                    }
                });

                copy(replacements, Singleton.INSTANCE);

                updateDerbySystemHome(Singleton.INSTANCE);

            } catch (IOException ex) {
                throw new Error(ex);
            }
        }
        return Singleton.INSTANCE;
    }

    private static interface Singleton {

        public static Properties INSTANCE = new Properties();
    }

}
