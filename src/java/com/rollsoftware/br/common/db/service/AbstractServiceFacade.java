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
import com.rollsoftware.br.common.db.repo.Repository;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rogério
 * @date October, 2016
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractServiceFacade<T extends ObjectInterface, ID extends ObjectInterface.ObjectDataInterfacePK>
        implements ServiceFacade<T, ID, String> {

    public AbstractServiceFacade() {
    }

    protected abstract Repository<T, ID, String> getRepository();

    protected abstract EntityManager getEntityManager();

    @Override
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String create(T entity)
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.create(em, entity);
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.TEXT_PLAIN})
    public String edit(@PathParam(value = "id") ID id, T entity)
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.edit(em, id, entity);
    }

    @Override
    public String remove(@PathParam(value = "id") ID id)
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.remove(em, id);
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public T find(@PathParam(value = "id") ID id)
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.find(em, id);
    }

    @Override
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<T> findAll()
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.findAll(em);
    }

    @Override
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<T> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to)
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.findRange(em, from, to);
    }

    @Override
    public Integer count()
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.count(em);
    }

    @Override
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countToString()
            throws SQLException, Exception {
        Repository<T, ID, String> repo = getRepository();
        EntityManager em = getEntityManager();
        return repo.countToString(em);
    }
}
