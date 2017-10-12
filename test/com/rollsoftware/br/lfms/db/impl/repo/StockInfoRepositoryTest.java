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
import com.rollsoftware.br.lfms.db.impl.entity.Cost;
import com.rollsoftware.br.lfms.db.impl.entity.StockInfo;
import com.rollsoftware.br.lfms.db.impl.entity.StockInvestor;
import com.rollsoftware.br.lfms.db.impl.entity.StorageLocation;
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
public class StockInfoRepositoryTest extends RepositoryTest {

    private ObjectInterface.ObjectDataInterfacePK costPKSaved;
    private ObjectInterface.ObjectDataInterfacePK stockInvestorPKSaved;
    private ObjectInterface.ObjectDataInterfacePK storageLocationPKSaved;
    private ObjectInterface.ObjectDataInterfacePK entityPK;
    private ObjectInterface entity;

    public StockInfoRepositoryTest(EntityManagerInterface emInterface) {
        super(emInterface);
    }

    @Override
    public AbstractRepository getNewInstance() {
        return new StockInfoRepository();
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK() {
        return (ODPK) entityPK;
    }

    @Override
    public <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK_NotFound() {
        return (ODPK) new StockInfo.ObjectDataPK("", "");
    }

    @Override
    public <T extends ObjectInterface> Class<T> getEntityClass() {
        return (Class<T>) StockInfo.class;
    }

    @Override
    public <T extends ObjectInterface> T getEntity() {
        return (T) entity;
    }

    @Override
    public <T extends ObjectInterface> T createEntity() {
        Cost cost = load(Cost.class, costPKSaved);
        StockInvestor stockInvestor
                = load(StockInvestor.class, stockInvestorPKSaved);
        StorageLocation storageLocation
                = load(StorageLocation.class, storageLocationPKSaved);

        StockInfo stockInfo = new StockInfo();

        stockInfo.setUUID("uuid" + Math.random());

        stockInfo.setQuantity(Math.random());

        stockInfo.getUnitCost().setCoin("R$");
        stockInfo.getUnitCost().setValue(Math.random());

        stockInfo.getUnitCostWithOthersCosts().setCoin("R$");
        stockInfo.getUnitCostWithOthersCosts().setValue(Math.random());

        stockInfo.getUnitSold().setCoin("R$");
        stockInfo.getUnitSold().setValue(Math.random());

        stockInfo.getTotalCost().setCoin("R$");
        stockInfo.getTotalCost().setValue(Math.random());

        stockInfo.getTotalCostWithOthersCosts().setCoin("R$");
        stockInfo.getTotalCostWithOthersCosts().setValue(Math.random());

        stockInfo.getTotalSold().setCoin("R$");
        stockInfo.getTotalSold().setValue(Math.random());

        stockInfo.setCost(cost);
        stockInfo.setStockInvestor(stockInvestor);
        stockInfo.setStorageLocation(storageLocation);

        stockInfo.generateUUID();

        return (T) stockInfo;
    }

    private Cost.ObjectDataPK saveCost() {

        Cost cost = new Cost();

        cost.setUUID("uuid" + Math.random());

        cost.setName("name" + Math.random());
        cost.setDescription("description" + Math.random());

        cost.generateUUID();

        save(cost);

        System.out.println("Save Cost: " + cost.getUUID());

        return cost.getODPK();
    }

    private StockInvestor.ObjectDataPK saveStockInvestor() {

        StockInvestor stockInvestor = new StockInvestor();

        stockInvestor.setUUID("uuid" + Math.random());

        stockInvestor.setName("name" + Math.random());

        stockInvestor.generateUUID();

        save(stockInvestor);

        System.out.println("Save StockInvestor: " + stockInvestor.getUUID());

        return stockInvestor.getODPK();
    }

    private StorageLocation.ObjectDataPK saveStorageLocation() {

        StorageLocation storageLocation = new StorageLocation();

        storageLocation.setUUID("uuid" + Math.random());

        storageLocation.setName("name" + Math.random());
        storageLocation.setAddress("address" + Math.random());

        storageLocation.generateUUID();

        save(storageLocation);

        System.out.println("Save StorageLocation: " + storageLocation.getUUID());

        return storageLocation.getODPK();
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
            costPKSaved = saveCost();
            stockInvestorPKSaved = saveStockInvestor();
            storageLocationPKSaved = saveStorageLocation();
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
