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
package com.rollsoftware.br.lfms.db.impl.entity;

import com.rollsoftware.br.common.db.entity.ObjectData;
import com.rollsoftware.br.common.db.entity.ObjectDataTest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rogério
 * @date October, 2017
 */
public class StockTreeNodeTest extends ObjectDataTest {

    private Cost.ObjectDataPK costPKSaved;
    private StockInvestor.ObjectDataInterfacePK stockInvestorPKSaved;
    private StorageLocation.ObjectDataInterfacePK storageLocationPKSaved;
    private StockInfo.ObjectDataPK stockInfoPKSaved;

    public StockTreeNodeTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) StockTreeNode.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        StockInfo stockInfo = load(StockInfo.class, stockInfoPKSaved);

        StockTreeNode stockTreeNode = new StockTreeNode();

        stockTreeNode.setUUID("uuid" + Math.random());

        stockTreeNode.setStockInfo(stockInfo);

        stockTreeNode.generateUUID();

        return (T) stockTreeNode;
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

        StockUtils.calculateAllCosts(stockInfo);

        stockInfo.setCost(cost);
        stockInfo.setStockInvestor(stockInvestor);
        stockInfo.setStorageLocation(storageLocation);

        stockInfo.generateUUID();

        save(stockInfo);

        System.out.println("Save StockInfo: " + stockInfo.getUUID());

        return stockInfo.getODPK();
    }

    @BeforeClass
    public static void setUpClass() {
        ObjectDataTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
        ObjectDataTest.tearDownClass();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        costPKSaved = saveCost();
        stockInvestorPKSaved = saveStockInvestor();
        storageLocationPKSaved = saveStorageLocation();
        stockInfoPKSaved = saveStockInfo();
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBasicStockTreeNodeInfo() {

        System.out.println("testBasicStockTreeNodeInfo");

        StockTreeNode stockTreeNode = load();

        System.out.println("StockTreeNode: " + stockTreeNode);
        System.out.println("StockTreeNode UUID: " + stockTreeNode.getUUID());
        System.out.println("StockTreeNode ODPK: " + stockTreeNode.getODPK());

        assertEquals(getObjectDataPK(), stockTreeNode.getODPK());
    }

    @Test
    public void testStockTreeNodeToXML() throws JAXBException {

        System.out.println("testStockTreeNodeToXML");

        StockTreeNode stockTreeNode = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(stockTreeNode, System.out);
        System.out.println();
    }

    @Test
    public void testStockTreeNodeToJSON() throws JAXBException {

        System.out.println("testStockTreeNodeToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        StockTreeNode stockTreeNode = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(stockTreeNode, System.out);
        System.out.println();
    }

    @Override
    @Test(expected = javax.persistence.PersistenceException.class)
    public void testEquals() {
        super.testEquals();
    }

    @Override
    @Test(expected = java.lang.AssertionError.class)
    public void testNotEquals() {
        super.testNotEquals();
    }

    public void testEquals2() {
        ObjectData objectData1 = createObjectData();

        ObjectData objectData2 = load(getObjectDataClass(),
                objectData1.getODPK());

        assertEquals(objectData1, objectData2);
    }
}
