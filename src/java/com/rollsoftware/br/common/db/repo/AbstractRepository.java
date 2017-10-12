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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockScope;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author Rogério
 * @date December, 2016
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractRepository<
        T extends ObjectInterface, ID extends ObjectInterface.ObjectDataInterfacePK>
        implements Repository<T, ID, String> {

    private final Class<T> entityClass;

    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String create(EntityManager em, T entity)
            throws SQLException, Exception {
        Callable callable = () -> {
            entity.generateUUID();
            em.persist(entity);
            return entity;
        };

        return "Created(entity): " + transaction(em, callable);
    }

    @Override
    public String edit(EntityManager em, ID id, T entity)
            throws SQLException, Exception {
        if (!entity.equalsODPK(id)) {
            throw new IllegalArgumentException(
                    "ID " + id + " is not equals "
                    + "to Entity.ID " + entity.getUUID());
        }
        return edit(em, entity);
    }

    @Override
    public String edit(EntityManager em, T entity)
            throws SQLException, Exception {
        Callable callable = () -> {
            em.merge(entity);
            return entity;
        };

        return "Edited(entity): " + transaction(em, callable);
    }

    @Override
    public String remove(EntityManager em, ID id)
            throws SQLException, Exception {
        T entity = find(em, id);
        remove(em, id, entity);
        return "Removed(id): " + entity;
    }

    @Override
    public String remove(EntityManager em, ID id, T entity)
            throws SQLException, Exception {

        Callable callable = () -> {
            T _entity = em.merge(entity);
            tryLockEntity(em, _entity);
            em.remove(_entity);
            return _entity;
        };

        transaction(em, callable);
        return "Removed(entity): " + entity;
    }

    @Override
    public T find(EntityManager em, ID id)
            throws SQLException, Exception {
        Callable<T> callable = () -> {
            T t = em.find(entityClass, id);
            if (t == null) {
                throw new NotFoundEntityException("Not find " + id + ".");
            }
            em.refresh(t);
            return t;
        };

        return transaction(em, callable);
    }

    @Override
    public List<T> findAll(EntityManager em)
            throws SQLException, Exception {
        javax.persistence.criteria.CriteriaQuery cq
                = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<T> findRange(EntityManager em, Integer from, Integer to)
            throws SQLException, Exception {
        javax.persistence.criteria.CriteriaQuery cq
                = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(to - from + 1);
        q.setFirstResult(from);
        return q.getResultList();
    }

    @Override
    public Integer count(EntityManager em)
            throws SQLException, Exception {
        javax.persistence.criteria.CriteriaQuery cq
                = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public String countToString(EntityManager em)
            throws SQLException, Exception {
        return String.valueOf(count(em));
    }

    private void tryLockEntity(EntityManager em, T entity)
            throws SQLException, Exception {
        Map<String, Object> props = new HashMap();

        //props.put(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 10);
        props.put(QueryHints.PESSIMISTIC_LOCK_SCOPE,
                PessimisticLockScope.EXTENDED);

        if (!em.getLockMode(entity)
                .equals(LockModeType.OPTIMISTIC_FORCE_INCREMENT)) {
            em.lock(entity,
                    LockModeType.OPTIMISTIC_FORCE_INCREMENT, props);
        }
    }

    private <R extends Object> R transaction(
            EntityManager em, Callable<R> statement)
            throws SQLException, Exception {
        R result = null;
        try {
            em.getTransaction().begin();
            result = statement.call();
            em.getTransaction().commit();
        } catch (Throwable ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        }

        return result;
    }
}
