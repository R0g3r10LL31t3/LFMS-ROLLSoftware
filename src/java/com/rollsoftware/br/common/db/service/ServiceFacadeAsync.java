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

import javax.ws.rs.container.AsyncResponse;

/**
 *
 * @author Rogério
 * @date November, 2016
 *
 * @param <T> entity type
 * @param <ID> uuid type
 * @param <R> result type
 */
public interface ServiceFacadeAsync<T, ID, R> {

    public void create(final AsyncResponse asyncResponse, T entity);

    public void edit(final AsyncResponse asyncResponse, ID id, T entity);

    public void remove(final AsyncResponse asyncResponse, ID id);

    public void find(final AsyncResponse asyncResponse, ID id);

    public void findAll(final AsyncResponse asyncResponse);

    public void findRange(final AsyncResponse asyncResponse,
            Integer from, Integer to);

    public void count(final AsyncResponse asyncResponse);

    public void countToString(final AsyncResponse asyncResponse);
}
