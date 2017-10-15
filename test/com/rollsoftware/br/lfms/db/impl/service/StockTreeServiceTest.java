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
import com.rollsoftware.br.common.db.service.ServiceFacade;
import com.rollsoftware.br.common.db.service.ServiceFacadeTest;
import com.rollsoftware.br.lfms.db.impl.entity.Cost;
import com.rollsoftware.br.lfms.db.impl.entity.StockInfo;
import com.rollsoftware.br.lfms.db.impl.entity.StockInvestor;
import com.rollsoftware.br.lfms.db.impl.entity.StockTree;
import com.rollsoftware.br.lfms.db.impl.entity.StockTreeNode;
import com.rollsoftware.br.lfms.db.impl.entity.StorageLocation;
import com.rollsoftware.br.lfms.db.impl.repo.StockTreeRepository;
import com.rollsoftware.br.test.util.EntityManagerInterface;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rogério
 * @date October, 2017
 */
public class StockTreeServiceTest extends ServiceFacadeTest {

    private AbstractServiceFacade rest;

    private ObjectInterface.ObjectDataInterfacePK costPKSaved;
    private ObjectInterface.ObjectDataInterfacePK stockInvestorPKSaved;
    private ObjectInterface.ObjectDataInterfacePK storageLocationPKSaved;
    private ObjectInterface.ObjectDataInterfacePK stockInfoPKSaved;
    private ObjectInterface.ObjectDataInterfacePK stockTreeNodePKSaved;
    private ObjectInterface.ObjectDataInterfacePK entityPK;
    private ObjectInterface entity;

    public StockTreeServiceTest(EntityManagerInterface emInterface) {
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
        return (ODPK) new StockTree.ObjectDataPK("", "");
    }

    @Override
    public <T extends ObjectInterface> Class<T> getEntityClass() {
        return (Class<T>) StockTree.class;
    }

    protected <T extends ServiceFacade>
            T createServiceFacade() {
        return createServiceFacade(
                new StockTreeRepository(), getEntityManager());
    }

    protected <T extends ServiceFacade>
            T createServiceFacade(EntityManager em) {
        return createServiceFacade(
                new StockTreeRepository(), em);
    }

    protected <T extends ServiceFacade>
            T createServiceFacade(
                    StockTreeRepository repo, EntityManager em) {
        return (T) new StockTreeService(repo, em);
    }

    @Override
    public <T extends ObjectInterface> T getEntity() {
        return (T) entity;
    }

    @Override
    public <T extends ObjectInterface> T createEntity() {
        Cost cost = load(Cost.class, costPKSaved);
        StockTreeNode stockTreeNode = load(StockTreeNode.class, stockTreeNodePKSaved);

        StockTree stockTree = new StockTree();

        stockTree.setUUID("uuid" + Math.random());

        stockTree.setRoot(stockTreeNode);
        stockTree.setCost(cost);

        stockTree.generateUUID();

        return (T) stockTree;
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

    private StockInfo.ObjectDataPK saveStockInfo() {

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

        save(stockInfo);

        System.out.println("Save StockInfo: " + stockInfo.getUUID());

        return stockInfo.getODPK();
    }

    private StockTreeNode.ObjectDataPK saveStockTreeNode() {

        StockInfo stockInfo = load(StockInfo.class, stockInfoPKSaved);

        StockTreeNode stockTreeNode = new StockTreeNode();

        stockTreeNode.setUUID("uuid" + Math.random());

        stockTreeNode.setStockInfo(stockInfo);

        stockTreeNode.generateUUID();

        save(stockTreeNode);

        System.out.println("Save StockTreeNode: " + stockTreeNode.getUUID());

        return stockTreeNode.getODPK();
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
            costPKSaved = saveCost();
            stockInvestorPKSaved = saveStockInvestor();
            storageLocationPKSaved = saveStorageLocation();
            stockInfoPKSaved = saveStockInfo();
            stockTreeNodePKSaved = saveStockTreeNode();
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

    /**
     * StockTree.uuid use same root.StockInfo.uuid, then create a duplicate on
     * reinsertion
     *
     * @throws java.sql.SQLException
     */
    @Override
    @Test(expected = javax.persistence.RollbackException.class)
    public void testCreate() throws SQLException, Exception {
        super.testCreate();
    }
}
