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
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Rogério
 * @date December, 2016
 *
 * @param <T> entity type
 * @param <ID> uuid type
 * @param <R> result type
 */
public interface Repository<T extends ObjectInterface, ID, R> {

    public R create(EntityManager em, T entity)
            throws SQLException, Exception;

    public R edit(EntityManager em, T entity)
            throws SQLException, Exception;

    public R edit(EntityManager em, ID id, T entity)
            throws SQLException, Exception;

    public R remove(EntityManager em, ID id)
            throws SQLException, Exception;

    public R remove(EntityManager em, ID id, T entity)
            throws SQLException, Exception;

    public T find(EntityManager em, ID id)
            throws SQLException, Exception;

    public List<T> findAll(EntityManager em)
            throws SQLException, Exception;

    public List<T> findRange(EntityManager em, Integer from, Integer to)
            throws SQLException, Exception;

    public Integer count(EntityManager em)
            throws SQLException, Exception;

    public String countToString(EntityManager em)
            throws SQLException, Exception;
}
