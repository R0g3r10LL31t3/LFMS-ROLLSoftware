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

import com.rollsoftware.br.test.util.CDITest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author Rogério
 * @date December, 2016
 */
public class EMProducerDefaultTest extends EMProducerTest {

    public EMProducerDefaultTest() {
    }

    @Override
    protected void addContextCDI() {
        super.addContextCDI();
        WELD.addBeanClass(EMFProducerDefault.class);
        WELD.addBeanClass(getEMProducerClass());
    }

    @Override
    protected <T extends EMProducer>
            Class<T> getEMProducerClass() {
        return (Class<T>) EMProducerDefault.class;
    }

    @BeforeClass
    public static void setUpClass() {
        CDITest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() {
        CDITest.tearDownClass();
    }
}
