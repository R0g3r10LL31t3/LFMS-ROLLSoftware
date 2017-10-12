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
 */
public class FixedCostTest extends ObjectDataTest {

    public FixedCostTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) FixedCost.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        FixedCost fixedCost = new FixedCost();

        fixedCost.setUUID("uuid" + Math.random());

        fixedCost.setName("name" + Math.random());
        fixedCost.setDescription("description" + Math.random());

        fixedCost.generateUUID();

        return (T) fixedCost;
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
    public void testBasicFixedCostInfo() {

        System.out.println("testBasicFixedCostInfo");

        FixedCost fixedCost = load();

        System.out.println("FixedCost: " + fixedCost);
        System.out.println("FixedCost UUID: " + fixedCost.getUUID());
        System.out.println("FixedCost ODPK: " + fixedCost.getODPK());

        assertEquals(getObjectDataPK(), fixedCost.getODPK());
    }

    @Test
    public void testFixedCostToXML() throws JAXBException {

        System.out.println("testFixedCostToXML");

        FixedCost fixedCost = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(fixedCost, System.out);
        System.out.println();
    }

    @Test
    public void testFixedCostToJSON() throws JAXBException {

        System.out.println("testFixedCostToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        FixedCost fixedCost = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(fixedCost, System.out);
        System.out.println();
    }
}
