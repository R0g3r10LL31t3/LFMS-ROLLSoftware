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
public class CostFeatureTest extends ObjectDataTest {

    private Cost.ObjectDataPK costPKSaved;

    public CostFeatureTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) CostFeature.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        Cost cost = load(Cost.class, costPKSaved);

        CostFeature costFeature = new CostFeature();

        costFeature.setUUID("uuid" + Math.random());

        costFeature.setLabel("label" + Math.random());
        costFeature.setValue("value" + Math.random());

        costFeature.setCost(cost);

        costFeature.generateUUID();

        return (T) costFeature;
    }

    private ObjectData.ObjectDataPK saveCost() {

        Cost cost = new Cost();

        cost.setUUID("uuid" + Math.random());

        cost.setName("name" + Math.random());
        cost.setDescription("description" + Math.random());

        cost.generateUUID();

        save(cost);

        System.out.println("Save Cost: " + cost.getUUID());

        return cost.getODPK();
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
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBasicCostFeatureInfo() {

        System.out.println("testBasicCostFeatureInfo");

        CostFeature costFeature = load();

        System.out.println("CostFeature: " + costFeature);
        System.out.println("CostFeature UUID: " + costFeature.getUUID());
        System.out.println("CostFeature ODPK: " + costFeature.getODPK());

        assertEquals(getObjectDataPK(), costFeature.getODPK());
    }

    @Test
    public void testCostFeatureToXML() throws JAXBException {

        System.out.println("testCostFeatureToXML");

        CostFeature costFeature = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(costFeature, System.out);
        System.out.println();
    }

    @Test
    public void testCostFeatureToJSON() throws JAXBException {

        System.out.println("testCostFeatureToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        CostFeature costFeature = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(costFeature, System.out);
        System.out.println();
    }
}
