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
package com.rollsoftware.br.common.db.internal.service;

import com.rollsoftware.br.common.db.internal.derby.DerbyDatabaseManager;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
@Path("derby")
public class DerbyService {

    @GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response get() {
        return Response
                .ok("Derby service")
                .build();
    }

    @GET
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response create() {
        try {
            DerbyDatabaseManager.getInstance().start();
        } catch (SQLException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        return Response
                .ok("Created")
                .build();
    }
}
