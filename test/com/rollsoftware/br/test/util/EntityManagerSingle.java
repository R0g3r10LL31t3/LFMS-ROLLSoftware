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
package com.rollsoftware.br.test.util;

import com.rollsoftware.br.common.properties.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Rogério
 * @date November, 2016
 */
public class EntityManagerSingle implements EntityManagerInterface {

    private static final String PU
            = Resource.getProperty("roll.software.br.application.database.PU");
    private static final Properties DB_PROPS
            = Resource.getDatabaseProperties();

    private EntityManagerFactory emf;
    private List<EntityManager> emList;

    public EntityManagerSingle() {
        emList = new ArrayList();
    }

    @Override
    public void setUp() throws SQLException {
        emf = Persistence.createEntityManagerFactory(PU);
    }

    @Override
    public void tearDown() throws SQLException {
        EntityManagerFactory _emf = emf;
        List<EntityManager> _emList = emList;
        emf = null;
        emList.clear();
        try {
            for (EntityManager em : _emList) {
                if (em != null && em.isOpen()) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    em.close();
                }
            }
        } finally {
            _emf.close();
        }
    }

    @Override
    public EntityManager getEntityManager() throws SQLException {
        return createEntityManager();
    }

    @Override
    public EntityManager createEntityManager() throws SQLException {
        EntityManager em = emf.createEntityManager(DB_PROPS);
        emList.add(em);
        return em;
    }

    @Override
    public String getDescription() {
        return "Using: " + getClass().getSimpleName();
    }
}
