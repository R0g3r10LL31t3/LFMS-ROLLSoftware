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
package com.rollsoftware.br.common.db.service;

import com.rollsoftware.br.common.db.entity.ObjectInterface;
import com.rollsoftware.br.common.db.repo.NotFoundEntityException;
import com.rollsoftware.br.test.util.EntityManagerInterface;
import com.rollsoftware.br.test.util.EntityManagerShared;
import com.rollsoftware.br.test.util.EntityManagerSingle;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Rogério
 * @date October, 2017
 */
@RunWith(Parameterized.class)
public abstract class ServiceFacadeTest {

    private final EntityManagerInterface emInterface;
    private EntityManager em;

    @Parameterized.Parameters
    public static Collection<Object[]> entityManagerParameters() {
        return Arrays.asList(new Object[][]{
            {new EntityManagerShared()},
            {new EntityManagerSingle()}
        });
    }

    protected ServiceFacadeTest() {
        this(new EntityManagerShared());
    }

    public ServiceFacadeTest(EntityManagerInterface emInterface) {
        this.emInterface = emInterface;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public abstract <T extends ObjectInterface, ID extends ObjectInterface.ObjectDataInterfacePK, R>
            ServiceFacade<T, ID, R> getInstance();

    public abstract <T extends ObjectInterface, ID extends ObjectInterface.ObjectDataInterfacePK, R>
            ServiceFacade<T, ID, R> getNewInstance(EntityManager em);

    public abstract <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK();

    public abstract <ODPK extends ObjectInterface.ObjectDataInterfacePK>
            ODPK getEntityPK_NotFound();

    public abstract <T extends ObjectInterface> Class<T> getEntityClass();

    public abstract <T extends ObjectInterface> T getEntity();

    public abstract <T extends ObjectInterface> T createEntity();

    public <T extends ObjectInterface>
            void save(T objectInterface) {
        em.getTransaction().begin();

        em.createNativeQuery("set schema LFMS_DB");

        em.persist(objectInterface);
        em.flush();

        em.getTransaction().commit();
    }

    public <T extends ObjectInterface>
            T load(EntityManager em, Class<T> clazz, Object id) {
        ObjectInterface _objectInterface
                = em.find(clazz, id);
        if (_objectInterface != null) {
            em.refresh(_objectInterface);
        }
        return (T) _objectInterface;
    }

    public <T extends ObjectInterface>
            T load(Class<T> clazz, Object id) {
        return (T) load(em, clazz, id);
    }

    public <T extends ObjectInterface> T load(Object id) {
        return (T) load(getEntityClass(), id);
    }

    public <T extends ObjectInterface> T load(EntityManager em, Object id) {
        return (T) load(em, getEntityClass(), id);
    }

    @BeforeClass
    public static void setUpClass() {
        DriverManager.setLogWriter(new java.io.PrintWriter(System.out));
    }

    @AfterClass
    public static void tearDownClass() {
        DriverManager.setLogWriter(null);
    }

    @Before
    public void setUp() throws SQLException {
        System.out.println("setUp is finishing.");
        emInterface.setUp();
        em = emInterface.getEntityManager();
        System.out.println(emInterface.getDescription());
        System.out.println("setUp is finished.");
    }

    @After
    public void tearDown() throws SQLException {
        System.out.println("tearDown is finishing.");
        emInterface.tearDown();
        System.out.println("tearDown is finished.");
    }

    /**
     * Test of create method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws SQLException, Exception {
        System.out.println("create");
        ObjectInterface entity = createEntity();

        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        instance.create(entity);

        System.out.println("Entity: " + entity);
    }

    /**
     * Test of edit method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testEditByUUID() throws SQLException, Exception {
        System.out.println("editByUUID");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK();
        ObjectInterface entity = getEntity();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        instance.edit(odpk, entity);

        System.out.println("Entity PK: " + odpk + ", Entity: " + entity);
    }

    /**
     * Test of remove method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testRemoveByUUID() throws SQLException, Exception {
        System.out.println("removeByUUID");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        instance.remove(odpk);

        System.out.println("Entity PK: " + odpk);
    }

    /**
     * Test of remove method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test(expected = com.rollsoftware.br.common.db.repo.NotFoundEntityException.class)
    public void testRemove_2x() throws SQLException, Exception {
        System.out.println("remove_2x");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        instance.remove(odpk);
        instance.remove(odpk);

        fail("Expected exception.");

        System.out.println("Entity PK: " + odpk);
    }

    /**
     * Test of remove method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test(expected = NotFoundEntityException.class)
    public void testRemoveID_NotFound() throws SQLException, Exception {
        System.out.println("removeID_NotFound");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK_NotFound();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        instance.remove(odpk);

        fail("Expected exception.");

        System.out.println("Entity PK: " + odpk);
    }

    /**
     * Test of find method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFind() throws SQLException, Exception {
        System.out.println("find");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        Object result = instance.find(odpk);

        assertNotNull(result);

        System.out.println("Entity: " + result);
    }

    /**
     * Test of find method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test(expected = NotFoundEntityException.class)
    public void testFind_NofFound() throws SQLException, Exception {
        System.out.println("find_NofFound");
        ObjectInterface.ObjectDataInterfacePK odpk = getEntityPK_NotFound();
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        Object result = instance.find(odpk);

        fail("Expected expection");

        System.out.println("Entity: " + result);
    }

    /**
     * Test of findAll method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFindAll() throws SQLException, Exception {
        System.out.println("findAll");
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        List result = instance.findAll();

        System.out.println("Finded: " + result.size());

        assertNotNull(result);
    }

    /**
     * Test of findRange method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFindRange() throws SQLException, Exception {
        System.out.println("findRange");
        int from = 0;
        int to = 1;
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        List expResult = null;
        List result = instance.findRange(from, to);

        assertNotEquals(expResult, result);
        assertFalse(result.isEmpty());

        System.out.println("Entities: " + result.size());
    }

    /**
     * Test of findRange method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test(expected = PersistenceException.class)
    public void testFindRange_from2to1() throws SQLException, Exception {
        System.out.println("findRange_from2to1");
        int from = 2;
        int to = 1;
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();

        List result = instance.findRange(from, to);

        fail("Expected exception.");

        System.out.println("Entities: " + result.size());
    }

    /**
     * Test of count method, of class ServiceFacade.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testCount() throws SQLException, Exception {
        System.out.println("count");
        ServiceFacade<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance
                = getInstance();
        int expResult = 0;
        int result = instance.count();

        System.out.println("Counted: " + result);

        assertNotEquals(expResult, result);
    }
}
