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
public class InvestorTest extends ObjectDataTest {

    public InvestorTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) Investor.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        Investor investor = new Investor();

        investor.setUUID("uuid" + Math.random());

        investor.setName("name" + Math.random());

        investor.generateUUID();

        return (T) investor;
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
    public void testBasicInvestorInfo() {

        System.out.println("testBasicInvestorInfo");

        Investor investor = load();

        System.out.println("Investor: " + investor);
        System.out.println("Investor UUID: " + investor.getUUID());
        System.out.println("Investor ODPK: " + investor.getODPK());

        assertEquals(getObjectDataPK(), investor.getODPK());
    }

    @Test
    public void testInvestorToXML() throws JAXBException {

        System.out.println("testInvestorToXML");

        Investor investor = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(investor, System.out);
        System.out.println();
    }

    @Test
    public void testInvestorToJSON() throws JAXBException {

        System.out.println("testInvestorToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Investor investor = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(investor, System.out);
        System.out.println();
    }
}
