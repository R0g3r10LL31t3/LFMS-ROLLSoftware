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
package com.rollsoftware.br.lfms.db.impl.service;

import com.rollsoftware.br.common.db.DBResourceWebListener;
import com.rollsoftware.br.common.db.em.Synchronization;
import com.rollsoftware.br.common.db.em.Synchronization.SyncType;
import com.rollsoftware.br.common.db.repo.Repository;
import com.rollsoftware.br.common.db.service.*;
import com.rollsoftware.br.lfms.db.impl.entity.StockTreeNode;
import com.rollsoftware.br.lfms.db.impl.repo.StockTreeNodeRepository;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Path;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
//@javax.ejb.Stateless
@RequestScoped
@Path("/db/stocktreenode")
public class StockTreeNodeService
        extends AbstractServiceFacade<StockTreeNode, StockTreeNode.ObjectDataPK> {

    //@PersistenceContext(unitName = "LFMSPU")
    @Inject
    private StockTreeNodeRepository repo;

    @Inject
    @Synchronization(SyncType.SYNC)
    private EntityManager em;

    public StockTreeNodeService() {
    }

    public StockTreeNodeService(StockTreeNodeRepository repo, EntityManager em) {
        this.repo = repo;
        this.em = em;
    }

    @Override
    public Repository<StockTreeNode, StockTreeNode.ObjectDataPK, String>
            getRepository() {
        return repo;
    }

    @Override
    public EntityManager getEntityManager() {
        if (em == null) {
            //warning, this using with non EJB implemented server!
            em = DBResourceWebListener.getEntityManager();
            System.out.println(
                    "Warning: this using with non EJB or CDI implemented server!");
        }

        return em;
    }
}
