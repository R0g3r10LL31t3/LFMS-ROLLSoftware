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

import com.rollsoftware.br.common.concurrent.ServiceThreadPool;
import com.rollsoftware.br.common.db.em.Synchronization;
import com.rollsoftware.br.common.db.em.Synchronization.SyncType;
import com.rollsoftware.br.common.db.entity.ObjectInterface;
import com.rollsoftware.br.common.db.repo.Repository;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rogério
 * @date November, 2016
 *
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractServiceFacadeAsync<T extends ObjectInterface, ID>
        implements ServiceFacadeAsync<T, ID, String> {

    @Inject
    @Synchronization(SyncType.ASYNC)
    private Event<EntityManager> emEvent;

    @Inject
    private ServiceThreadPool serviceThreadPool;

    public AbstractServiceFacadeAsync() {
    }

    protected abstract Repository<T, ID, String> getRepository();

    protected abstract EntityManager getEntityManager();

    @Override
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public void create(
            @Suspended final AsyncResponse asyncResponse,
            T entity) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.create(em, entity));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            } finally {
                emEvent.fire(getEntityManager());
            }
        });
    }

    @Override
    @PUT
    @Path("{id}")
    @Consumes(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.TEXT_PLAIN})
    public void edit(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam(value = "id") ID id, T entity) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();

                asyncResponse.resume(repo.edit(em, id, entity));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @DELETE
    @Override
    @Path("async/{id}")
    @Produces({MediaType.TEXT_PLAIN})
    public void remove(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam(value = "id") ID id) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.remove(em, id));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @Override
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void find(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam(value = "id") ID id) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.find(em, id));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @Override
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void findAll(
            @Suspended final AsyncResponse asyncResponse) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.findAll(em));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @Override
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void findRange(
            @Suspended final AsyncResponse asyncResponse,
            @PathParam("from") Integer from, @PathParam("to") Integer to) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.findRange(em, from, to));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @Override
    public void count(@Suspended final AsyncResponse asyncResponse) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.count(em));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }

    @Override
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public void countToString(
            @Suspended final AsyncResponse asyncResponse) {
        serviceThreadPool.invokeLater(() -> {
            try {
                Repository<T, ID, String> repo = getRepository();
                EntityManager em = getEntityManager();
                asyncResponse.resume(repo.countToString(em));
            } catch (Exception ex) {
                asyncResponse.resume(ex);
            }
        });
    }
}
