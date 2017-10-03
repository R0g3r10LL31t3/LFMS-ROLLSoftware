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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    public PriorityThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue);
    }

    public PriorityThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue, threadFactory);
    }

    public PriorityThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                handler);
    }

    public PriorityThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                threadFactory,
                handler);
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

    public static class PriorityWorker<T> implements Callable<T> {

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

        private final int value;

        private Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
