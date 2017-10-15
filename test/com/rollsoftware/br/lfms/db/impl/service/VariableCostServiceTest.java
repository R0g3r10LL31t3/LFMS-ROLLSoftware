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
package com.rollsoftware.br.lfms.db.impl.service;

import com.rollsoftware.br.common.db.entity.ObjectInterface;
import com.rollsoftware.br.common.db.service.AbstractServiceFacade;
import com.rollsoftware.br.common.db.service.ObjectDataService;
import com.rollsoftware.br.common.db.service.ServiceFacade;
import com.rollsoftware.br.common.db.service.ServiceFacadeTest;
import com.rollsoftware.br.lfms.db.impl.entity.VariableCost;
import com.rollsoftware.br.lfms.db.impl.repo.VariableCostRepository;
import com.rollsoftware.br.test.util.EntityManagerInterface;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Rogério
 * @date October, 2017
 */
public class VariableCostServiceTest extends ServiceFacadeTest {

    private AbstractServiceFacade rest;

    private ObjectInterface.ObjectDataInterfacePK entityPK;
    private ObjectInterface entity;

    public VariableCostServiceTest(EntityManagerInterface emInterface) {
        super(emInterface);
    }

    @Override
    public AbstractServiceFacade getInstance() {
        return rest;
    }

    @Override
    public AbstractServiceFacade getNewInstance(EntityManager em) {
        return createServiceFacade(em);
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK() {
        return (ODPK) entityPK;
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK_NotFound() {
        return (ODPK) new VariableCost.ObjectDataPK("", "");
    }

    @Override
    public <T extends ObjectInterface> Class<T> getEntityClass() {
        return (Class<T>) VariableCost.class;
    }

    protected <T extends ServiceFacade>
            T createServiceFacade() {
        return createServiceFacade(
                new VariableCostRepository(), getEntityManager());
    }

    protected <T extends ServiceFacade>
            T createServiceFacade(EntityManager em) {
        return createServiceFacade(
                new VariableCostRepository(), em);
    }

    protected <T extends ServiceFacade>
            T createServiceFacade(
                    VariableCostRepository repo, EntityManager em) {
        return (T) new VariableCostService(repo, em);
    }

    @Override
    public <T extends ObjectInterface> T getEntity() {
        return (T) entity;
    }

    @Override
    public <T extends ObjectInterface> T createEntity() {
        VariableCost cost = new VariableCost();

        cost.setUUID("uuid" + Math.random());

        cost.setName("name" + Math.random());
        cost.setDescription("description" + Math.random());

        cost.generateUUID();

        return (T) cost;
    }

    @BeforeClass
    public static void setUpClass() {
        ServiceFacadeTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
        ServiceFacadeTest.tearDownClass();
    }

    @Before
    @Override
    public void setUp() throws SQLException {
        try {
            super.setUp();
            entity = createEntity();

            save(entity);

            entityPK = entity.getODPK();

            rest = createServiceFacade();
        } catch (Throwable ex) {
            throw ex;
        }
    }

    @After
    @Override
    public void tearDown() throws SQLException {
        super.tearDown();
    }
}
