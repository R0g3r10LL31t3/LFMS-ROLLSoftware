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
package com.rollsoftware.br.common.db;

import com.rollsoftware.br.common.db.em.DatabaseInjection;
import com.rollsoftware.br.common.properties.Resource;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
@WebListener
@ApplicationScoped
public class DBResourceWebListener implements ServletContextListener {

    private static final String PERSISTENT_UNIT;
    private static final Map DATABASE_PROPS;

    private static final ThreadLocal<EntityManager> THREADLOCAL_EM;
    private static EntityManagerFactory EMF;

    static {
        PERSISTENT_UNIT = Resource.getProperty(
                "roll.software.br.application.database.PU");
        DATABASE_PROPS = Resource.getDatabaseProperties();
        THREADLOCAL_EM = new ThreadLocal();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            if (THREADLOCAL_EM.get() != null && THREADLOCAL_EM.get().isOpen()) {
                THREADLOCAL_EM.get().close();
                THREADLOCAL_EM.set(null);
            }
            if (EMF != null && EMF.isOpen()) {
                EMF.close();
                EMF = null;
            }
        } catch (Throwable ex) {
            throw ex;
        } finally {
            THREADLOCAL_EM.remove();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (EMF == null) {
            EMF = Persistence.createEntityManagerFactory(PERSISTENT_UNIT);
        }

        if (EMF == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return EMF;
    }

    public static EntityManager getEntityManager() {
        EntityManagerFactory emf = getEntityManagerFactory();

        if (THREADLOCAL_EM.get() == null) {
            THREADLOCAL_EM.set(emf.createEntityManager(DATABASE_PROPS));
        }

        if (THREADLOCAL_EM.get() == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return THREADLOCAL_EM.get();
    }

    @Produces
    @DatabaseInjection
    public static Map getDatabaseProperties() {
        return DATABASE_PROPS;
    }

    @Produces
    @DatabaseInjection
    public static String getPersistentUnit() {
        return PERSISTENT_UNIT;
    }
}
