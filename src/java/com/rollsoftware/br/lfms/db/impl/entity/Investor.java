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

import com.rollsoftware.br.common.db.entity.ObjectData;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rogério
 * @date September, 2017
 *
 * Logistics and Finances Management System
 */
@Entity
@Table(name = "INV_INVESTOR",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"INV_UUID_FK", "INV_TYPE"})
            ,@UniqueConstraint(columnNames = {"INV_NAME"})
        }
)
@DiscriminatorValue("Investor")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "INV_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "INV_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "Investor.findAll",
            query = "SELECT i FROM Investor i")
    , @NamedQuery(name = "Investor.findByName",
            query = "SELECT i FROM Investor i"
            + " WHERE i.name = :name")
})
@XmlRootElement(name = "investor")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "investor", propOrder = {
    "name"
})
public class Investor extends ObjectData implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "INV_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer invVersion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "INV_NAME", nullable = false, length = 256)
    protected String name;

    public Investor() {
        this("");
    }

    public Investor(String uuid) {
        this(0, "Investor", uuid,
                "", 0);
    }

    public Investor(Integer id, String type, String uuid,
            String name,
            Integer invVersion) {
        super(id, type, uuid);
        this.name = name;
        this.invVersion = invVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getName());
        return _uuid;
    }

    @Override
    public boolean equals(Object object) {

        if (object == null) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (!(object instanceof Investor)) {
            return false;
        }
        final Investor other = (Investor) object;
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Investor[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + invVersion
                + ",\n\tname=" + getName()
                + "\n]";
    }
}
