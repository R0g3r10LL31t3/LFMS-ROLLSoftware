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
public class StockInvestorTest extends ObjectDataTest {

    public StockInvestorTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) StockInvestor.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        StockInvestor stockInvestor = new StockInvestor();

        stockInvestor.setUUID("uuid" + Math.random());

        stockInvestor.setName("name" + Math.random());

        stockInvestor.generateUUID();

        return (T) stockInvestor;
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
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBasicStockInvestorInfo() {

        System.out.println("testBasicStockInvestorInfo");

        StockInvestor stockInvestor = load();

        System.out.println("StockInvestor: " + stockInvestor);
        System.out.println("StockInvestor UUID: " + stockInvestor.getUUID());
        System.out.println("StockInvestor ODPK: " + stockInvestor.getODPK());

        assertEquals(getObjectDataPK(), stockInvestor.getODPK());
    }

    @Test
    public void testStockInvestorToXML() throws JAXBException {

        System.out.println("testStockInvestorToXML");

        StockInvestor stockInvestor = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(stockInvestor, System.out);
        System.out.println();
    }

    @Test
    public void testStockInvestorToJSON() throws JAXBException {

        System.out.println("testStockInvestorToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        StockInvestor stockInvestor = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(stockInvestor, System.out);
        System.out.println();
    }
}
