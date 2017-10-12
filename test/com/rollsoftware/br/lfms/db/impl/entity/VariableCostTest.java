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
public class VariableCostTest extends ObjectDataTest {

    public VariableCostTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) VariableCost.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        VariableCost variableCost = new VariableCost();

        variableCost.setUUID("uuid" + Math.random());

        variableCost.setName("name" + Math.random());
        variableCost.setDescription("description" + Math.random());

        variableCost.generateUUID();

        return (T) variableCost;
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
    public void testBasicVariableCostInfo() {

        System.out.println("testBasicVariableCostInfo");

        VariableCost variableCost = load();

        System.out.println("VariableCost: " + variableCost);
        System.out.println("VariableCost UUID: " + variableCost.getUUID());
        System.out.println("VariableCost ODPK: " + variableCost.getODPK());

        assertEquals(getObjectDataPK(), variableCost.getODPK());
    }

    @Test
    public void testVariableCostToXML() throws JAXBException {

        System.out.println("testVariableCostToXML");

        VariableCost variableCost = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(variableCost, System.out);
        System.out.println();
    }

    @Test
    public void testVariableCostToJSON() throws JAXBException {

        System.out.println("testVariableCostToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        VariableCost variableCost = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(variableCost, System.out);
        System.out.println();
    }
}
