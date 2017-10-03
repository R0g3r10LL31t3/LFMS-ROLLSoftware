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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Rogério
 * @date March, 2016
 *
 * @param <P> parent
 * @param <T>
 * @param <ID>
 */
public abstract class EmbeddedAbstractRepository<
        P extends ObjectInterface, T extends ObjectInterface, ID>
        implements Repository<T, ID, String> {

    private final AbstractRepository<P, ID> delegate;

    protected EmbeddedAbstractRepository(AbstractRepository<P, ID> delegate) {
        this.delegate = delegate;
    }

    protected abstract P extractParent(T entity);

    protected abstract T extractChild(P entity);

    protected List<T> extractChildren(List<P> list) {
        List<T> children = new ArrayList();

        for (P p : list) {
            children.add(extractChild(p));
        }

        return children;
    }

    @Override
    public String create(EntityManager em, T entity)
            throws SQLException, Exception {
        P parent = extractParent(entity);
        return delegate.create(em, parent);
    }

    @Override
    public String edit(EntityManager em, T entity)
            throws SQLException, Exception {
        P parent = extractParent(entity);
        return delegate.edit(em, parent);
    }

    @Override
    public String edit(EntityManager em, ID id, T entity)
            throws SQLException, Exception {
        P parent = extractParent(entity);
        return delegate.edit(em, id, parent);
    }

    @Override
    public String remove(EntityManager em, ID id)
            throws SQLException, Exception {
        return delegate.remove(em, id);
    }

    @Override
    public String remove(EntityManager em, ID id, T entity)
            throws SQLException, Exception {
        P parent = extractParent(entity);
        return delegate.remove(em, id, parent);
    }

    @Override
    public T find(EntityManager em, ID id)
            throws SQLException, Exception {
        P parent = delegate.find(em, id);
        return extractChild(parent);
    }

    @Override
    public List<T> findAll(EntityManager em)
            throws SQLException, Exception {
        List<P> parents = delegate.findAll(em);
        return extractChildren(parents);
    }

    @Override
    public List<T> findRange(EntityManager em, Integer from, Integer to)
            throws SQLException, Exception {
        List<P> parents = delegate.findRange(em, from, to);
        return extractChildren(parents);
    }

    @Override
    public Integer count(EntityManager em)
            throws SQLException, Exception {
        return delegate.count(em);
    }

    @Override
    public String countToString(EntityManager em)
            throws SQLException, Exception {
        return delegate.countToString(em);
    }
}
