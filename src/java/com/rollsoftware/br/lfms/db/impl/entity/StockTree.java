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
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
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
@Table(name = "STR_STOCK_TREE",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"STR_UUID_FK", "STR_TYPE"})
            ,@UniqueConstraint(columnNames = {"STR_UUID_FK"})
        }
)
@DiscriminatorValue("StockTree")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "STR_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "STR_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "StockTree.findAll",
            query = "SELECT s FROM StockTree s")
    , @NamedQuery(name = "StockTree.findByRoot",
            query = "SELECT s FROM StockTree s"
            + " WHERE s.root.uuid = :uuid")
    , @NamedQuery(name = "StockTree.findByCost",
            query = "SELECT s FROM StockTree s"
            + " WHERE s.cost.uuid = :uuid")
})
@XmlRootElement(name = "stockTree")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stockTree", propOrder = {
    "cost",
    "root",
    "stockTreeNodeList"
})
public class StockTree
        extends ObjectData
        implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "STR_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer strVersion;

    @NotNull
    @OneToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STR_ROOT_STN_UUID_FK", referencedColumnName = "STN_UUID_FK")
        ,@JoinColumn(name = "STR_ROOT_STN_TYPE", referencedColumnName = "STN_TYPE")})
    @XmlElement(name = "root")
    @XmlIDREF
    protected StockTreeNode root;

    @OneToMany(
            mappedBy = "stockTree",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("parent ASC, dateCreated ASC")
    @XmlElement(name = "node")
    @XmlIDREF
    protected List<StockTreeNode> stockTreeNodeList;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STR_CST_UUID_FK", referencedColumnName = "CST_UUID_FK")
        ,@JoinColumn(name = "STR_CST_TYPE", referencedColumnName = "CST_TYPE")})
    @XmlElement(name = "cost")
    @XmlIDREF
    protected Cost cost;

    public StockTree() {
        this("");
    }

    public StockTree(String uuid) {
        this(0, "StockTree", uuid,
                null, new ArrayList(),
                null,
                0);
    }

    public StockTree(Integer id, String type, String uuid,
            StockTreeNode root, List<StockTreeNode> stockTreeNodeList, Cost cost,
            Integer strVersion) {
        super(id, type, uuid);
        this.strVersion = strVersion;
        this.root = root;
        this.stockTreeNodeList = stockTreeNodeList;
        this.cost = cost;
    }

    public StockTreeNode getRoot() {
        return root;
    }

    public void setRoot(StockTreeNode root) {
        this.root = root;
    }

    public List<StockTreeNode> getStockTreeNodeList() {
        return stockTreeNodeList;
    }

    public void setStockTreeNodeList(List<StockTreeNode> stockTreeNodeList) {
        this.stockTreeNodeList = stockTreeNodeList;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    @Override
    public void generateUUID() {
        if (getRoot() != null) {
            getRoot().generateUUID();
            setUUID(getRoot().getUUID());
        }
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getRoot());
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
        if (!(object instanceof StockTree)) {
            return false;
        }
        final StockTree other = (StockTree) object;
        if (!Objects.equals(this.getRoot(), other.getRoot())) {
            return false;
        }
        if (!Objects.equals(this.getStockTreeNodeList(), other.getStockTreeNodeList())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StockTree[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + strVersion
                + ",\n\troot=" + getRoot()
                + "\n]";
    }
}
