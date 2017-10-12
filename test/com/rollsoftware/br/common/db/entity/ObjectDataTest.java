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

import com.rollsoftware.br.common.properties.Resource;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
 * @date October, 2016
 */
public class ObjectDataTest {

    protected static final String PU
            = Resource.getProperty("roll.software.br.application.database.PU");
    protected static final Properties DB_PROPS
            = Resource.getDatabaseProperties();

    protected static EntityManagerFactory EMF;
    protected static EntityManager EM;

    private ObjectData.ObjectDataPK objectDataPK;

    public ObjectDataTest() {
    }

    protected <T extends ObjectData.ObjectDataPK>
            T getObjectDataPK() {
        return (T) objectDataPK;
    }

    protected <T extends ObjectData>
            Class<T> getObjectDataClass() {
        return (Class<T>) ObjectData.class;
    }

    protected <T extends ObjectData>
            T createObjectData() {
        ObjectData objectData = new ObjectData();
        objectData.setUUID("uuid" + Math.random());
        objectData.generateUUID();
        return (T) objectData;
    }

    public <T extends ObjectData>
            void save(T objectData) {
        EM.getTransaction().begin();

        EM.createNativeQuery("set schema LFMS_DB");

        EM.persist(objectData);
        EM.flush();

        EM.refresh(objectData);

        EM.getTransaction().commit();
    }

    public <T extends ObjectData>
            T load() {
        return load(getObjectDataClass(), getObjectDataPK());
    }

    public <T extends ObjectData>
            T load(Class<T> clazz, Object id) {
        ObjectData objectData
                = EM.find(clazz, id);
        if (objectData != null) {
            EM.refresh(objectData);
        }
        return (T) objectData;
    }

    @BeforeClass
    public static void setUpClass() {
        DriverManager.setLogWriter(new java.io.PrintWriter(System.out));
        EMF = Persistence.createEntityManagerFactory(PU);
        EM = EMF.createEntityManager(DB_PROPS);
    }

    @AfterClass
    public static void tearDownClass() {
        EM.close();
        EMF.close();
        DriverManager.setLogWriter(null);
    }

    @Before
    public void setUp() throws Exception {

        try {

            ObjectData objectData = createObjectData();

            save(objectData);

            objectDataPK = objectData.getODPK();
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }

    @After
    public void tearDown() throws Exception {
        if (EM.getTransaction().isActive()) {
            EM.getTransaction().rollback();
        }
    }

    @Test
    public void testBasic() {
        System.out.println("testBasic");

        ObjectData objectData = load();

        System.out.println("Object Data: " + objectData);
        System.out.println("Object Data UUID: " + objectData.getUUID());
        System.out.println("Object Data ODPK: " + objectData.getODPK());

        assertEquals(getObjectDataPK(), objectData.getODPK());
    }

    @Test
    public void testObjectDataToXML() throws JAXBException {

        System.out.println("testObjectDataToXML");

        JAXBContext jc = JAXBContext.newInstance(
                ObjectDataTest.this.getObjectDataClass());

        ObjectData objectData = createObjectData();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(objectData, System.out);
        System.out.println();
    }

    @Test
    public void testObjectData2ToXML() throws JAXBException {

        System.out.println("testObjectData2ToXML");

        JAXBContext jc = JAXBContext.newInstance(ObjectData.class);

        ObjectData objectData = createObjectData();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        System.out.println("XML Output:");
        marshaller.marshal(objectData, System.out);
        System.out.println();
    }

    @Test
    public void testObjectDataToJSON() throws JAXBException {

        System.out.println("testObjectDataToJSON");

        JAXBContext jc = JAXBContext.newInstance(
                ObjectDataTest.this.getObjectDataClass());

        ObjectData objectData = createObjectData();

        Marshaller marshaller = jc.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(
                MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        System.out.println("JSON Output:");
        marshaller.marshal(objectData, System.out);
        System.out.println();
    }

    @Test
    public void testEquals() {
        ObjectData objectData1 = createObjectData();

        save(objectData1);

        ObjectData objectData2 = load(getObjectDataClass(),
                objectData1.getODPK());

        assertEquals(objectData1, objectData2);
    }

    @Test
    public void testNotEquals() {
        ObjectData objectData1 = createObjectData();
        ObjectData objectData2 = createObjectData();

        assertNotEquals(objectData1, objectData2);
    }

    @Test
    public void testType() {
        ObjectData objectData = load();

        String className = objectData.getClass().getSimpleName();
        String type = objectData.getType();

        assertTrue(Objects.equals(className, type));
    }
}
