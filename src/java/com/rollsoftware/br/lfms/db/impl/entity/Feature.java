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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
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
@Table(name = "CFT_COST_FEATURE",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CFT_UUID_FK", "CFT_TYPE"})
            ,@UniqueConstraint(columnNames = {"CFT_KEY", "CFT_VALUE"})
        }
)
@DiscriminatorValue("Feature")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "CFT_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "CFT_TYPE", referencedColumnName = "OBJ_TYPE")
})
@XmlRootElement(name = "feature")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "feature", propOrder = {
    "cost", "label", "value"
})
@NamedQueries({
    @NamedQuery(name = "Feature.findAll",
            query = "SELECT f FROM Feature f")
    , @NamedQuery(name = "Feature.findByKey",
            query = "SELECT f FROM Feature f"
            + " WHERE f.label = :label")
    , @NamedQuery(name = "Feature.findByValue",
            query = "SELECT f FROM Feature f"
            + " WHERE f.value = :value")
    , @NamedQuery(name = "Feature.findByCost",
            query = "SELECT f FROM Feature f"
            + " WHERE f.cost.uuid = :uuid")
})
public class Feature extends ObjectData implements Serializable {
//Flavor, Brand, Commercial Kind, Description, Packing, Manufacturer

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "CFT_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer cftVersion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CFT_LABEL", nullable = false, length = 256)
    protected String label;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CFT_VALUE", nullable = false, length = 256)
    protected String value;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "CFT_CST_UUID_FK", referencedColumnName = "CST_UUID_FK")
        ,@JoinColumn(name = "CFT_CST_TYPE", referencedColumnName = "CST_TYPE")})
    @XmlElement(name = "cost")
    @XmlIDREF
    protected Cost cost;

    public Feature() {
        this("", "");
    }

    public Feature(String uuid) {
        this(0, "Feature", uuid,
                "", "",
                null, 0);
    }

    public Feature(String label, String value) {
        this(0, "Feature", "",
                label, value,
                null, 0);
    }

    public Feature(
            Integer id, String type, String uuid,
            String label, String value,
            Cost cost,
            Integer cftVersion) {
        super(id, type, uuid);
        this.cftVersion = cftVersion;
        this.label = label;
        this.value = value;
        this.cost = cost;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getLabel());
        _uuid += Objects.hashCode(getValue());
        return _uuid;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work
        // in the case the id fields are not set
        if (object == null) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (!(object instanceof Feature)) {
            return false;
        }
        final Feature other = (Feature) object;
        if (!Objects.equals(this.getLabel(), other.getLabel())) {
            return false;
        }
        if (!Objects.equals(this.getValue(), other.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Feature[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + cftVersion
                + ",\n\tkey=" + getLabel()
                + ",\n\tvalue=" + getValue()
                + "\n]";
    }
}
