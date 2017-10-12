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
public class StorageLocationTest extends ObjectDataTest {

    public StorageLocationTest() {
    }

    @Override
    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) StorageLocation.class;
    }

    @Override
    protected <T extends ObjectData> T createObjectData() {
        StorageLocation storageLocation = new StorageLocation();

        storageLocation.setUUID("uuid" + Math.random());

        storageLocation.setName("name" + Math.random());
        storageLocation.setAddress("address" + Math.random());

        storageLocation.generateUUID();

        return (T) storageLocation;
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
    public void testBasicStorageLocationInfo() {

        System.out.println("testBasicStorageLocationInfo");

        StorageLocation storageLocation = load();

        System.out.println("StorageLocation: " + storageLocation);
        System.out.println("StorageLocation UUID: " + storageLocation.getUUID());
        System.out.println("StorageLocation ODPK: " + storageLocation.getODPK());

        assertEquals(getObjectDataPK(), storageLocation.getODPK());
    }

    @Test
    public void testStorageLocationToXML() throws JAXBException {

        System.out.println("testStorageLocationToXML");

        StorageLocation storageLocation = createObjectData();

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(storageLocation, System.out);
        System.out.println();
    }

    @Test
    public void testStorageLocationToJSON() throws JAXBException {

        System.out.println("testStorageLocationToJSON");

        JAXBContext jc = JAXBContext.newInstance(getObjectDataClass());

        StorageLocation storageLocation = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(storageLocation, System.out);
        System.out.println();
    }
}
