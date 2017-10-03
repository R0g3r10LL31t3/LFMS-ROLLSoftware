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
package com.rollsoftware.br.common.db.entity;

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
 * @date March, 2017
 */
public class ObjectEmbeddedTest {

    public ObjectEmbeddedTest() {
    }

    protected <T extends ObjectEmbedded>
            Class<T> getObjectEmbeddedClass() {
        return (Class<T>) ObjectEmbedded.class;
    }

    protected <T extends ObjectEmbedded>
            T createObjectEmbedded() {
        ObjectEmbedded objectEmbedded = new ObjectEmbedded() {
            @Override
            public ObjectInterface getParent() {
                return null;
            }
        };
        return (T) objectEmbedded;
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testObjectEmbeddedToXML() throws JAXBException {

        System.out.println("testObjectEmbeddedToXML");

        JAXBContext jc = JAXBContext.newInstance(ObjectEmbeddedTest.this.getObjectEmbeddedClass());

        ObjectEmbedded objectEmbedded = createObjectEmbedded();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(objectEmbedded, System.out);
        System.out.println();
    }

    @Test
    public void testObjectEmbedded2ToXML() throws JAXBException {

        System.out.println("testObjectEmbedded2ToXML");

        JAXBContext jc = JAXBContext.newInstance(ObjectEmbedded.class);

        ObjectEmbedded objectEmbedded = createObjectEmbedded();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(objectEmbedded, System.out);
        System.out.println();
    }

    @Test
    public void testObjectDataToJSON() throws JAXBException {

        System.out.println("testObjectEmbeddedToJSON");

        JAXBContext jc = JAXBContext.newInstance(ObjectEmbeddedTest.this.getObjectEmbeddedClass());

        ObjectEmbedded objectEmbedded = createObjectEmbedded();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(objectEmbedded, System.out);
        System.out.println();
    }

    @Test
    public void testEquals() {
        ObjectEmbedded objectEmbedded = createObjectEmbedded();

        assertEquals(objectEmbedded, objectEmbedded);
    }

    @Test
    public void testNotEquals() {
        ObjectEmbedded objectEmbedded1 = createObjectEmbedded();
        ObjectEmbedded objectEmbedded2 = createObjectEmbedded();

        assertNotEquals(objectEmbedded1, objectEmbedded2);
    }
}
