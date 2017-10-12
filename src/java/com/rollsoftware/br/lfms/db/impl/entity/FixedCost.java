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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rogério
 * @date September, 2017
 *
 * Logistics and Finances Management System
 */
@Entity
@Table(name = "CST_COST",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CST_UUID_FK", "CST_TYPE"})
            ,@UniqueConstraint(columnNames = {"CST_NAME"})
        }
)
@DiscriminatorValue("FixedCost")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "CST_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "CST_TYPE", referencedColumnName = "OBJ_TYPE")
})
@XmlRootElement(name = "fixedCost")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fixedCost")
public class FixedCost extends Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    public FixedCost() {
        this("");
    }

    public FixedCost(String uuid) {
        this(0, "FixedCost", uuid,
                null,
                "", "",
                new ArrayList(), new ArrayList(), new ArrayList(),
                0);
    }

    public FixedCost(
            Integer id, String type, String uuid,
            StockInfo stockInfoOwner,
            String name, String description,
            List<CostFeature> featureList,
            List<StockInfo> stockInfoList,
            List<StockTree> stockTreeList,
            Integer cstVersion) {
        super(id, type, uuid,
                stockInfoOwner,
                name, description,
                featureList, stockInfoList, stockTreeList,
                cstVersion);
    }

    @Override
    public String toString() {
        return "Fixed" + super.toString();
    }
}
