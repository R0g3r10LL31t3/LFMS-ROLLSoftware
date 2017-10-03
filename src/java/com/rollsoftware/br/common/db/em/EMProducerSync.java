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
package com.rollsoftware.br.common.db.em;

import com.rollsoftware.br.common.db.em.Synchronization.SyncType;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
@RequestScoped
public class EMProducerSync implements EMProducer {

    @Inject
    @Default
    private EntityManagerFactory emf;

    @Inject
    @DatabaseInjection
    private Map databaseProperties;

    @Override
    @Produces
    @Synchronization(SyncType.SYNC)
    public EntityManager createEntityManager() {

        EntityManager em = emf.createEntityManager(databaseProperties);

        System.out.println("em.open " + em);

        return em;
    }

    @Override
    public void closeEntityManager(
            @Disposes @Synchronization(SyncType.SYNC) EntityManager em) {
        try {
            if (em != null) {

                System.out.println("em.close " + em);
                em.close();
            }
        } catch (Throwable ex) {
            throw ex;
        }
    }
}
