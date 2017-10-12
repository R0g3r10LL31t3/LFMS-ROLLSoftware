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
package com.rollsoftware.br.lfms.db.impl.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rogério
 * @date September, 2017
 *
 * Logistics and Finances Management System
 */
@XmlType(name = "stockState")
@XmlEnum
public enum StockState {

    @XmlEnumValue("Input")
    ST_INPUT("Input"),
    @XmlEnumValue("Dead")
    ST_DEAD("Dead"),
    @XmlEnumValue("Alive")
    ST_ALIVE("Alive"),
    @XmlEnumValue("Output")
    ST_OUTPUT("Output");

    private final String value;

    private StockState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value();
    }

    public String value() {
        return value;
    }
}
