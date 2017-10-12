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
import com.rollsoftware.br.common.db.entity.ObjectEmbedded;
import com.rollsoftware.br.common.db.entity.ObjectEmbeddedTest;
import com.rollsoftware.br.common.db.entity.ObjectInterface;
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
public class PriceTest extends ObjectEmbeddedTest {

    public PriceTest() {
    }

    protected <T extends ObjectInterface>
            Class<T> getObjectInterfaceClass() {
        return (Class<T>) Price.class;
    }

    @Override
    protected <T extends ObjectEmbedded>
            Class<T> getObjectEmbeddedClass() {
        return (Class<T>) Price.class;
    }

    @Override
    protected Price createObjectEmbedded() {
        Price price = new Price();

        price.setCoin("REAL");
        price.setValue(Math.random());
        price.setParent(new ObjectData());

        price.generateUUID();

        return price;
    }

    @BeforeClass
    public static void setUpClass() {
        ObjectEmbeddedTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
        ObjectEmbeddedTest.tearDownClass();
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
    public void testBasicPrice() {

        System.out.println("testBasicPrice");

        Price price = createObjectEmbedded();

        System.out.println("Price: " + price);
        System.out.println("Price UUID: " + price.getUUID());

        assertNotNull(price);
    }

    @Test
    public void testPriceToXML() throws JAXBException {

        System.out.println("testPriceToXML");

        JAXBContext jc = JAXBContext.newInstance(getObjectEmbeddedClass());

        Price price = createObjectEmbedded();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(price, System.out);
        System.out.println();
    }

    @Test
    public void testPriceToJSON() throws JAXBException {

        System.out.println("testPriceToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectEmbeddedClass());

        Price price = createObjectEmbedded();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(price, System.out);
        System.out.println();
    }
}
