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
package com.rollsoftware.br.test.util;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.SessionContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
public class CDITest {

    protected static Weld WELD;
    protected WeldContainer weldContainer;

    public CDITest() {
    }

    @BeforeClass
    public static void setUpClass() {
        WELD = new Weld();
        WELD
                .disableDiscovery()
                .property("org.jboss.weld.construction.relaxed", true);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        weldContainer = WELD
                .containerId(getClass().getSimpleName())
                .initialize();
    }

    @After
    public void tearDown() {
        weldContainer.close();
    }

    public final <T> T getManagedBean(Class<T> type) {
        return this.weldContainer.instance()
                .select(type)
                .get();
    }

    public final void runAtRequestScoped(final Runnable runnable) {
        RequestContext context = weldContainer
                .instance()
                .select(RequestContext.class, UnboundLiteral.INSTANCE)
                .get();
        try {
            context.activate();
            runnable.run();
        } finally {
            context.deactivate();
        }
    }

    public final void runAtSessionScoped(final Runnable runnable) {
        SessionContext context = weldContainer
                .instance()
                .select(SessionContext.class, UnboundLiteral.INSTANCE)
                .get();
        try {
            context.activate();
            runnable.run();
        } finally {
            context.deactivate();
        }
    }
}
