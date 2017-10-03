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

import com.rollsoftware.br.common.concurrent.PriorityThreadPoolExecutor.Priority;
import com.rollsoftware.br.common.concurrent.PriorityThreadPoolExecutor.PriorityWorker;
import com.rollsoftware.br.common.properties.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
@Default
@ApplicationScoped
public class ServiceThreadPoolDefault implements ServiceThreadPool {

    private static final int MAX_SERVICES;

    static {
        int value = 10;
        try {
            value = Integer.parseInt(Resource.getProperty(
                    "roll.software.br.application"
                    + ".database.threadpool.maxServices"));
        } catch (Throwable ex) {
        }
        MAX_SERVICES = value;
    }

    private ExecutorService executorService;

    private Future submit(Runnable runnable) {
        Future future;

        try {
            future = executorService.submit(
                    new PriorityWorker(Priority.LOWEST.getValue(), runnable));
        } finally {
        }

        return future;
    }

    private Future submit(Callable callable) {
        Future future;

        try {
            future = executorService.submit(
                    new PriorityWorker(Priority.LOWEST.getValue(), callable));
        } finally {
        }

        return future;
    }

    @Override
    public void invokeLater(Runnable runnable) {
        submit(runnable);
    }

    @Override
    public Future invokeLater(Callable callable) {
        return submit(callable);
    }

    @Override
    @PostConstruct
    public void startup() {
        if (executorService == null) {
            executorService = new PriorityThreadPoolExecutor(
                    MAX_SERVICES, MAX_SERVICES, 0L, TimeUnit.MILLISECONDS,
                    new PriorityBlockingQueue(MAX_SERVICES),
                    new DefaultThreadFactory("Account-Manager"));
        }
    }

    @Override
    @PreDestroy
    public void shutdown() {
        if (executorService == null) {
            executorService.shutdown();
        }
    }
}
