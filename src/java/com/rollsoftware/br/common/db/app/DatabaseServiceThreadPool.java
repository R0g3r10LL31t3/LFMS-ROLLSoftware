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
package com.rollsoftware.br.common.db.app;

import com.rollsoftware.br.common.properties.Resource;
import com.rollsoftware.br.util.Utils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Rogério
 * @date November, 2016
 */
public final class DatabaseServiceThreadPool {

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
    private ReentrantLock lock;

    private DatabaseServiceThreadPool() {
        lock = new ReentrantLock(true);

        lock.lock();
        try {
            if (executorService == null) {
                ThreadFactory threadFactory = new ThreadFactory() {

                    final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

                    @Override
                    public Thread newThread(final Runnable runnable) {
                        String className = DatabaseServiceThreadPool.class.getSimpleName();
                        Thread thread = defaultFactory.newThread(runnable);
                        thread.setName(className + ":" + thread.getId());
                        thread.setDaemon(true);
                        return thread;
                    }
                };
                executorService = new PriorityThreadPoolExecutor(
                        MAX_SERVICES, MAX_SERVICES, 0L, TimeUnit.MILLISECONDS,
                        new PriorityBlockingQueue(MAX_SERVICES),
                        threadFactory);

                Utils.addShutdownHook(() -> {
                    executorService.shutdown();
                });

                executorService.submit(() -> {
                    while (true) {
                        System.gc();
                        Thread.sleep(3600000);
                    }
                });

            }
        } finally {
            lock.unlock();
        }
    }

    private static DatabaseServiceThreadPool getInstance() {
        return Singleton.INSTANCE;
    }

    private ExecutorService getExecutorService() {
        return executorService;
    }

    private Future submitAndWait(Runnable runnable) {
        Future future;

        lock.lock();
        try {

            future = getExecutorService().submit(
                    new PriorityWorker(Priority.HIGHEST.getValue(), runnable));
            while (!future.isDone()) {
            }

        } finally {
            lock.unlock();
        }

        return future;
    }

    private Future submitAndRun(Runnable runnable) {
        Future future;

        lock.lock();
        try {
            future = getExecutorService().submit(
                    new PriorityWorker(Priority.LOWEST.getValue(), runnable));
        } finally {
            lock.unlock();
        }

        return future;
    }

    public static void changeMaxServices(int maxServices) {
        PriorityThreadPoolExecutor instance
                = (PriorityThreadPoolExecutor) getInstance().getExecutorService();
        instance.setCorePoolSize(maxServices);
        instance.setMaximumPoolSize(maxServices);
    }

    public static void invokeAndWait(Runnable runnable) {

        getInstance().submitAndWait(runnable);

    }

    public static void invokeLater(Runnable runnable) {

        getInstance().submitAndRun(runnable);

    }

    private static interface Singleton {

        public DatabaseServiceThreadPool INSTANCE
                = new DatabaseServiceThreadPool();
    }

    private static class FutureTaskWithPriority<T> extends FutureTask<T>
            implements Comparable {

        private final int priority;

        public FutureTaskWithPriority(
                int priority, Runnable runnable, T result) {
            super(runnable, result);
            this.priority = priority;
        }

        public FutureTaskWithPriority(int priority, Callable callable) {
            super(callable);
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Object o) {

            if (o == null) {
                return -1;
            } else if (o instanceof FutureTaskWithPriority) {
                FutureTaskWithPriority other = (FutureTaskWithPriority) o;

                int p1 = this.getPriority();
                int p2 = other.getPriority();

                return p1 > p2 ? 1 : (p1 == p2 ? 0 : -1);
            }

            return 0;
        }

    }

    private static class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

        public PriorityThreadPoolExecutor(
                int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue,
                ThreadFactory threadFactory,
                RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize,
                    keepAliveTime, unit,
                    workQueue, threadFactory, handler);
        }

        private PriorityThreadPoolExecutor(
                int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue,
                ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize,
                    keepAliveTime, unit,
                    workQueue, threadFactory);
        }

        @Override
        protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
            int priority = 0;
            if (callable instanceof PriorityWorker) {
                priority = ((PriorityWorker) callable).getPriority();
            }
            return new FutureTaskWithPriority(priority, callable);
        }

        @Override
        protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
            return new FutureTaskWithPriority(0, runnable, value);
        }
    }

    private static class PriorityWorker<T> implements Callable<T> {

        private final int priority;
        private final Object worker;

        public PriorityWorker(int priority, Callable<T> callable) {
            this.priority = priority;
            worker = callable;
        }

        public PriorityWorker(int priority, Runnable callable) {
            this.priority = priority;
            worker = callable;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public T call() throws Exception {
            if (worker instanceof Callable) {
                return ((Callable<T>) worker).call();
            } else if (worker instanceof Runnable) {
                ((Runnable) worker).run();
            }
            return null;
        }
    }

    public static enum Priority {

        HIGHEST(0),
        HIGH(1),
        MEDIUM(2),
        LOW(3),
        LOWEST(4);

        int value;

        Priority(int val) {
            this.value = val;
        }

        public int getValue() {
            return value;
        }
    }
}
