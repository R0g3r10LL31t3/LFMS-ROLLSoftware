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
package com.rollsoftware.br.common.db.em;

import com.rollsoftware.br.common.concurrent.ServiceThreadPool;
import java.io.Serializable;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
//@Priority(Interceptor.Priority.APPLICATION + 50)
@Interceptor
@Asynchronized
public class AsynchronousInterceptor implements Serializable {

    @Inject
    @Default
    private ServiceThreadPool serviceThreadPool;

    @AroundInvoke
    public Object invoke(final InvocationContext ctx) throws Exception {
        System.out.println("Invoking asynchronous method: "
                + ctx.getMethod().getName());
        serviceThreadPool.invokeLater(() -> {
            try {
                System.out.println("Really Invoking asynchronous method: "
                        + ctx.getMethod().getName());
                ctx.proceed();
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        });

        return null;
    }
}
