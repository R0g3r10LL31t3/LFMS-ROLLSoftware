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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
@ApplicationScoped
public class EMFProducerDefault implements EMFProducer {

    @Inject
    @DatabaseInjection
    private String persistentUnit;

    private EntityManagerFactory emf;

    @PostConstruct
    public void create() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(persistentUnit);
        }
    }

    @PreDestroy
    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }

    @Override
    @Produces
    @Default
    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }

        return emf;
    }
}
