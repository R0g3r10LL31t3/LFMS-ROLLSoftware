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
@Table(name = "STN_STOCK_TREE_NODE",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"STN_UUID_FK", "STN_TYPE"})
            ,@UniqueConstraint(columnNames = {"STN_UUID_FK"})
        }
)
@DiscriminatorValue("StockTreeNode")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "STN_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "STN_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "StockTreeNode.findAll",
            query = "SELECT s FROM StockTreeNode s")
    , @NamedQuery(name = "StockTreeNode.findByParent",
            query = "SELECT s FROM StockTreeNode s"
            + " WHERE s.parent.uuid = :uuid")
    , @NamedQuery(name = "StockTreeNode.findRoots",
            query = "SELECT s FROM StockTreeNode s"
            + " WHERE s.parent IS NULL")
    , @NamedQuery(name = "StockTreeNode.findByStockInfo",
            query = "SELECT s FROM StockTreeNode s"
            + " WHERE s.stockInfo.uuid = :uuid")
    , @NamedQuery(name = "StockTreeNode.findByStockTree",
            query = "SELECT s FROM StockTreeNode s"
            + " WHERE s.stockTree.uuid = :uuid")
})
@XmlRootElement(name = "stockTreeNode")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stockTreeNode", propOrder = {
    "stockTree",
    "parent",
    "childrenList",
    "stockInfo"
})
public class StockTreeNode
        extends ObjectData
        implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "STN_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer stnVersion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STN_STR_UUID_FK", referencedColumnName = "STR_UUID_FK")
        ,@JoinColumn(name = "STN_STR_TYPE", referencedColumnName = "STR_TYPE")})
    @XmlElement(name = "stockTree")
    @XmlIDREF
    protected StockTree stockTree;

    @ManyToOne(
            optional = true,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STN_PARENT_UUID_FK", referencedColumnName = "STN_UUID_FK")
        ,@JoinColumn(name = "STN_PARENT_TYPE", referencedColumnName = "STN_TYPE")})
    @XmlElement(name = "parent")
    @XmlIDREF
    protected StockTreeNode parent;

    @OneToMany(
            mappedBy = "parent",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("dateCreated ASC")
    @XmlElement(name = "child")
    @XmlIDREF
    protected List<StockTreeNode> childrenList;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    @JoinColumns({
        @JoinColumn(name = "STN_STI_UUID_FK", referencedColumnName = "STI_UUID_FK")
        ,@JoinColumn(name = "STN_STI_TYPE", referencedColumnName = "STI_TYPE")})
    @XmlElement(name = "stock")
    @XmlIDREF
    protected StockInfo stockInfo;

    public StockTreeNode() {
        this("");
    }

    public StockTreeNode(String uuid) {
        this(0, "StockTreeNode", uuid,
                null, null,
                new ArrayList(),
                new StockInfo(),
                0);
    }

    public StockTreeNode(StockInfo stockInfo) {
        this(0, "StockTreeNode", "",
                null, null,
                new ArrayList(),
                stockInfo,
                0);
    }

    public StockTreeNode(
            Integer id, String type, String uuid,
            StockTree stockTree,
            StockTreeNode parent, List<StockTreeNode> childrenList,
            StockInfo stockInfo,
            Integer stnVersion) {
        super(id, type, uuid);
        this.stnVersion = stnVersion;
        this.stockTree = stockTree;
        this.parent = parent;
        this.childrenList = childrenList;
        this.stockInfo = stockInfo;
    }

    public StockTree getStockTree() {
        return stockTree;
    }

    public void setStockTree(StockTree stockTree) {
        this.stockTree = stockTree;
    }

    public StockTreeNode getParent() {
        return parent;
    }

    public void setParent(StockTreeNode parent) {
        this.parent = parent;
    }

    public List<StockTreeNode> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<StockTreeNode> childrenList) {
        this.childrenList = childrenList;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }

    @Override
    public void generateUUID() {
        if (getStockInfo() != null) {
            getStockInfo().generateUUID();
            setUUID(getStockInfo().getUUID());
        }
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getStockInfo());
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
        if (!(object instanceof StockTreeNode)) {
            return false;
        }
        final StockTreeNode other = (StockTreeNode) object;
        if (!Objects.equals(this.getStockInfo(), other.getStockInfo())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StockTreeNode[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + stnVersion
                + ",\n\tstock=" + getStockInfo()
                + "\n]";
    }
}
