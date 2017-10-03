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
package com.rollsoftware.br.common.concurrent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
@WebListener
@ApplicationScoped
public class ServiceThreadPoolWebListener implements ServletContextListener {

    @Inject
    @Default
    private ServiceThreadPool serviceThreadPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (serviceThreadPool == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }
        serviceThreadPool.startup();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (serviceThreadPool == null) {
            throw new IllegalStateException("Context is not initialized yet.");
        }
        serviceThreadPool.shutdown();
    }
}
