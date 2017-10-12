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
package com.rollsoftware.br.lfms.db.impl.repo;

import com.rollsoftware.br.common.db.entity.ObjectInterface;
import com.rollsoftware.br.common.db.repo.AbstractRepository;
import com.rollsoftware.br.common.db.repo.RepositoryTest;
import com.rollsoftware.br.lfms.db.impl.entity.Investor;
import com.rollsoftware.br.test.util.EntityManagerInterface;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Rogério
 * @date October, 2017
 */
public class InvestorRepositoryTest extends RepositoryTest {

    private ObjectInterface.ObjectDataInterfacePK entityPK;
    private ObjectInterface entity;

    public InvestorRepositoryTest(EntityManagerInterface emInterface) {
        super(emInterface);
    }

    @Override
    public AbstractRepository getNewInstance() {
        return new InvestorRepository();
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK() {
        return (ODPK) entityPK;
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK_NotFound() {
        return (ODPK) new Investor.ObjectDataPK("", "");
    }

    @Override
    public <T extends ObjectInterface> Class<T> getEntityClass() {
        return (Class<T>) Investor.class;
    }

    @Override
    public <T extends ObjectInterface> T getEntity() {
        return (T) entity;
    }

    @Override
    public <T extends ObjectInterface> T createEntity() {
        Investor investor = new Investor();

        investor.setUUID("uuid" + Math.random());

        investor.setName("name" + Math.random());

        investor.generateUUID();

        return (T) investor;
    }

    @BeforeClass
    public static void setUpClass() {
        RepositoryTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
        RepositoryTest.tearDownClass();
    }

    @Before
    @Override
    public void setUp() throws SQLException {
        try {
            super.setUp();
            entity = createEntity();

            save(entity);

            entityPK = entity.getODPK();
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
