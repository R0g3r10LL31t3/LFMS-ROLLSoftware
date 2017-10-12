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
@Table(name = "CFT_COST_FEATURE",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CFT_UUID_FK", "CFT_TYPE"})
            ,@UniqueConstraint(columnNames = {"CFT_KEY", "CFT_VALUE"})
        }
)
@DiscriminatorValue("CostFeature")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "CFT_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "CFT_TYPE", referencedColumnName = "OBJ_TYPE")
})
@XmlRootElement(name = "costFeature")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "costFeature")
public class CostFeature extends Feature implements Serializable {

    private static final long serialVersionUID = 1L;

    public CostFeature() {
        this("");
    }

    public CostFeature(String uuid) {
        this(0, "CostFeature", uuid,
                "", "",
                null,
                0);
    }

    public CostFeature(String key, String value) {
        this(0, "Feature", "",
                key, value,
                null, 0);
    }

    public CostFeature(
            Integer id, String type, String uuid,
            String key, String value,
            Cost cost,
            Integer cftVersion) {
        super(id, type, uuid,
                key, value,
                cost,
                cftVersion);
    }

    @Override
    public String toString() {
        return "Cost" + super.toString();
    }
}
