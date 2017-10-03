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

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rogério
 * @date November, 2016
 *
 * @param <T> entity type
 * @param <ID> uuid type
 * @param <R> result type
 */
public interface ServiceFacade<T, ID, R> {

    public R create(T entity)
            throws SQLException, Exception;

    public R edit(ID id, T entity)
            throws SQLException, Exception;

    public R remove(ID id)
            throws SQLException, Exception;

    public T find(ID id)
            throws SQLException, Exception;

    public List<T> findAll()
            throws SQLException, Exception;

    public List<T> findRange(Integer from, Integer to)
            throws SQLException, Exception;

    public Integer count()
            throws SQLException, Exception;

    public String countToString()
            throws SQLException, Exception;
}
