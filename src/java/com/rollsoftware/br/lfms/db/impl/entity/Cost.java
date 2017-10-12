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
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "CST_COST",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"CST_UUID_FK", "CST_TYPE"})
            ,@UniqueConstraint(columnNames = {"CST_NAME"})
        }
)
@DiscriminatorValue("Cost")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "CST_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "CST_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "Cost.findAll",
            query = "SELECT c FROM Cost c")
    , @NamedQuery(name = "Cost.findByName",
            query = "SELECT c FROM Cost c"
            + " WHERE c.name = :name")
    , @NamedQuery(name = "Cost.findByDescription",
            query = "SELECT c FROM Cost c"
            + " WHERE c.description = :description")
    , @NamedQuery(name = "Cost.findStockInfoOwner",
            query = "SELECT c FROM Cost c"
            + " WHERE c.stockInfoOwner = :stockInfoOwner")
})
@XmlRootElement(name = "cost")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cost", propOrder = {
    "stockInfoOwner",
    "name", "description",
    "featureList", "stockTreeList",
    "stockInfoList"
})
public class Cost extends ObjectData implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "CST_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer cstVersion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CST_NAME", nullable = false, length = 256)
    protected String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CST_DESCRIPTION", nullable = false, length = 256)
    protected String description;

    @OneToMany(
            mappedBy = "cost",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("dateCreated ASC")
    @XmlElement(name = "feature")
    @XmlIDREF
    protected List<CostFeature> featureList;

    @OneToMany(
            mappedBy = "cost",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("dateCreated ASC")
    @XmlElement(name = "stock")
    @XmlIDREF
    protected List<StockInfo> stockInfoList;

    @OneToMany(
            mappedBy = "cost",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("dateCreated ASC")
    @XmlElement(name = "stockTree")
    @XmlIDREF
    protected List<StockTree> stockTreeList;

    @ManyToOne(
            optional = true,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "CST_STI_UUID_FK", referencedColumnName = "STI_UUID_FK")
        ,@JoinColumn(name = "CST_STI_TYPE", referencedColumnName = "STI_TYPE")
    })
    @XmlElement(name = "stockOwner")
    @XmlIDREF
    protected StockInfo stockInfoOwner;

    public Cost() {
        this("");
    }

    public Cost(String uuid) {
        this(0, "Cost", uuid,
                null,
                "", "",
                new ArrayList(),
                new ArrayList(),
                new ArrayList(),
                0);
    }

    public Cost(Integer id, String type, String uuid,
            StockInfo stockInfoOwner,
            String name, String description,
            List<CostFeature> featureList,
            List<StockInfo> stockInfoList,
            List<StockTree> stockTreeList,
            Integer cstVersion) {
        super(id, type, uuid);
        this.name = name;
        this.description = description;
        this.featureList = featureList;
        this.stockInfoList = stockInfoList;
        this.stockTreeList = stockTreeList;
        this.stockInfoOwner = stockInfoOwner;
        this.cstVersion = cstVersion;

        init();
    }

    private void init() {
        for (Feature feature : getCostFeatureList()) {
            feature.setCost(this);
        }

        for (StockTree stock : getStockTreeList()) {
            stock.setCost(this);
        }

        for (StockInfo stockInfo : getStockInfoList()) {
            stockInfo.setCost(this);
        }
    }

    public StockInfo getStockInfoOwner() {
        return stockInfoOwner;
    }

    public void setStockInfoOwner(StockInfo stockInfoOwner) {
        this.stockInfoOwner = stockInfoOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CostFeature> getCostFeatureList() {
        return featureList;
    }

    public void setCostFeatureList(List<CostFeature> featureList) {
        this.featureList = featureList;
    }

    public List<StockInfo> getStockInfoList() {
        return stockInfoList;
    }

    public void setStockInfoList(List<StockInfo> stockInfoList) {
        this.stockInfoList = stockInfoList;
    }

    public List<StockTree> getStockTreeList() {
        return stockTreeList;
    }

    public void setStockTreeList(List<StockTree> stockTreeList) {
        this.stockTreeList = stockTreeList;
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getName());
        _uuid += Objects.hashCode(getDescription());
        _uuid += Objects.hashCode(getCostFeatureList());
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
        if (!(object instanceof Cost)) {
            return false;
        }
        final Cost other = (Cost) object;
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getDescription(), other.getDescription())) {
            return false;
        }
        if (!Objects.equals(this.getCostFeatureList(), other.getCostFeatureList())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cost[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + cstVersion
                + ",\n\tname=" + getName()
                + ",\n\tdescription=" + getDescription()
                + "\n]";
    }
}
