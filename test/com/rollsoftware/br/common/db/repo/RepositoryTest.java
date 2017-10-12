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
package com.rollsoftware.br.common.db.repo;

import com.rollsoftware.br.common.db.entity.ObjectInterface;
import com.rollsoftware.br.test.util.EntityManagerInterface;
import com.rollsoftware.br.test.util.EntityManagerShared;
import com.rollsoftware.br.test.util.EntityManagerSingle;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
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
 * @date January, 2017
 */
@RunWith(Parameterized.class)
public abstract class RepositoryTest {

    private final EntityManagerInterface emInterface;
    private EntityManager em;

    @Parameterized.Parameters
    public static Collection<Object[]> entityManagerParameters() {
        return Arrays.asList(new Object[][]{
            {new EntityManagerShared()},
            {new EntityManagerSingle()}
        });
    }

    protected RepositoryTest() {
        this(new EntityManagerShared());
    }

    public RepositoryTest(EntityManagerInterface emInterface) {
        this.emInterface = emInterface;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public abstract <T extends ObjectInterface, ID extends ObjectInterface.ObjectDataInterfacePK, R>
            Repository<T, ID, R> getNewInstance();

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
     * Test of create method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testCreate() throws SQLException, Exception {
        System.out.println("create");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = createEntity();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.create(entityManager, entity);

        assertNotNull(entity.getUUID());
        assertNotNull(entity.getODPK());
    }

    /**
     * Test of edit method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testEdit_byUUID() throws SQLException, Exception {
        System.out.println("edit");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = getEntity();
        ObjectInterface.ObjectDataInterfacePK id = getEntityPK();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.edit(entityManager, id, entity);

        assertNotNull(entity.getUUID());
        assertNotNull(entity.getODPK());
    }

    /**
     * Test of edit method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testEdit_byEntity() throws SQLException, Exception {
        System.out.println("edit");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = getEntity();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.edit(entityManager, entity);

        assertNotNull(entity.getUUID());
        assertNotNull(entity.getODPK());
    }

    /**
     * Test of remove method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testRemove_byUUID() throws SQLException, Exception {
        System.out.println("remove");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = getEntity();
        ObjectInterface.ObjectDataInterfacePK id = getEntityPK();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.remove(entityManager, id);

        assertNotNull(entity.getUUID());
        assertNotNull(entity.getODPK());
    }

    /**
     * Test of remove method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testRemove_byEntity() throws SQLException, Exception {
        System.out.println("remove");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = getEntity();
        ObjectInterface.ObjectDataInterfacePK id = getEntityPK();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.remove(entityManager, id, entity);

        assertNotNull(entity.getUUID());
        assertNotNull(entity.getODPK());
    }

    /**
     * Test of find method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFind() throws SQLException, Exception {
        System.out.println("find");
        EntityManager entityManager = getEntityManager();
        ObjectInterface entity = getEntity();
        ObjectInterface.ObjectDataInterfacePK id = getEntityPK();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        ObjectInterface result = instance.find(entityManager, id);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    /**
     * Test of findAll method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFindAll() throws SQLException, Exception {
        System.out.println("findAll");
        EntityManager entityManager = getEntityManager();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        List result = instance.findAll(entityManager);

        assertNotNull(result);
    }

    /**
     * Test of findRange method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testFindRange() throws SQLException, Exception {
        System.out.println("findRange");
        EntityManager entityManager = getEntityManager();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();
        Integer from = 1;
        Integer to = 1;

        List result = instance.findRange(entityManager, from, to);

        assertNotNull(result);
    }

    /**
     * Test of count method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testCount() throws SQLException, Exception {
        System.out.println("count");
        EntityManager entityManager = getEntityManager();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        Integer result = instance.count(entityManager);

        assertNotNull(result);
    }

    /**
     * Test of countToString method, of class Repository.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    @Test
    public void testCountToString() throws SQLException, Exception {
        System.out.println("countToString");
        EntityManager entityManager = getEntityManager();
        Repository<ObjectInterface, ObjectInterface.ObjectDataInterfacePK, String> instance = getNewInstance();

        String result = instance.countToString(entityManager);

        assertNotNull(result);
    }
}
