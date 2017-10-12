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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
@Table(name = "STI_STOCK_INFO",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"STI_UUID_FK", "STI_TYPE"})
            ,@UniqueConstraint(columnNames = {"STI_UUID_FK"})
        }
)
@DiscriminatorValue("StockInfo")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "STI_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "STI_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "StockInfo.findAll",
            query = "SELECT s FROM StockInfo s")
    , @NamedQuery(name = "StockInfo.findByDateInput",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.inputDate = :dateInput")
    , @NamedQuery(name = "StockInfo.findByDateOutput",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.outputDate = :dateOutput")
    , @NamedQuery(name = "StockInfo.findBetween",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.outputDate BETWEEN :dateInput AND :dateOutput")
    , @NamedQuery(name = "StockInfo.findByStockState",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.stockState = :stockState")
    , @NamedQuery(name = "StockInfo.findByQuantityMinorThat",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.quantity < :quantity")
    , @NamedQuery(name = "StockInfo.findByQuantityMajorThat",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.quantity > :quantity")
    , @NamedQuery(name = "StockInfo.findByInvestor",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.stockInvestor.uuid = :uuid")
    , @NamedQuery(name = "StockInfo.findByStorageLocation",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.storageLocation.uuid = :uuid")
    , @NamedQuery(name = "StockInfo.findByCost",
            query = "SELECT s FROM StockInfo s"
            + " WHERE s.cost.uuid = :uuid")
})
@XmlRootElement(name = "stockInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stockInfo", propOrder = {
    "cost", "stockInvestor", "storageLocation",
    "inputDate", "outputDate",
    "quantity", "stockState",
    "unitCost", "totalCost",
    "unitCostWithOthersCosts", "totalCostWithOthersCosts",
    "unitSold", "totalSold",
    "otherCostsByUnitList"
})
public class StockInfo
        extends ObjectData
        implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "STI_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer stiVersion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STI_INPUTDATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date inputDate;

    @Basic(optional = true)
    @Column(name = "STI_OUTPUTDATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date outputDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STI_STATE", nullable = false, length = 256)
    @Enumerated(EnumType.STRING)
    protected StockState stockState;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STI_QUANTITY")
    protected Double quantity;

    @Embedded
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_UNITCOSTCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_UNITCOSTVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "unitCostCoin")
        ,@XmlElement(name = "unitCostValue")
    })
    protected Price unitCost;

    @Embedded
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_TOTALCOSTCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_TOTALCOSTVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "totalCostCoin")
        ,@XmlElement(name = "totalCostValue")
    })
    protected Price totalCost;

    @Embedded
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_UNITCOSTWITHOTHERSCOSTSCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_UNITCOSTWITHOTHERSCOSTSVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "unitCostWithOthersCostsCoin")
        ,@XmlElement(name = "unitCostWithOthersCostsValue")
    })
    protected Price unitCostWithOthersCosts;

    @Embedded
    @NotNull
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_TOTALCOSTWITHOTHERSCOSTSCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_TOTALCOSTWITHOTHERSCOSTSVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "totalCostWithOthersCostsCoin")
        ,@XmlElement(name = "totalCostWithOthersCostsValue")
    })
    protected Price totalCostWithOthersCosts;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_UNITSOLDCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_UNITSOLDVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "unitSoldCoin")
        ,@XmlElement(name = "unitSoldValue")
    })
    protected Price unitSold;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "coin",
                column = @Column(
                        name = "STI_TOTALSOLDCOIN",
                        nullable = false, length = 256))
        ,@AttributeOverride(name = "value",
                column = @Column(
                        name = "STI_TOTALSOLDVALUE",
                        nullable = false, length = 256))
    })
    @XmlElements({
        @XmlElement(name = "totalSoldCoin")
        ,@XmlElement(name = "totalSoldValue")
    })
    protected Price totalSold;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STI_CST_UUID_FK", referencedColumnName = "CST_UUID_FK")
        ,@JoinColumn(name = "STI_CST_TYPE", referencedColumnName = "OBJ_TYPE")})
    @XmlElement(name = "cost")
    @XmlIDREF
    protected Cost cost;

    @OneToMany(
            mappedBy = "stockInfoOwner",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @OrderBy("dateCreated ASC")
    @XmlElement(name = "otherCost")
    @XmlIDREF
    protected List<Cost> otherCostsByUnitList;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STI_INV_UUID_FK", referencedColumnName = "INV_UUID_FK")
        ,@JoinColumn(name = "STI_INV_TYPE", referencedColumnName = "INV_TYPE")})
    @XmlElement(name = "investor")
    @XmlIDREF
    protected StockInvestor stockInvestor;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumns({
        @JoinColumn(name = "STI_STL_UUID_FK", referencedColumnName = "STL_UUID_FK")
        ,@JoinColumn(name = "STI_STL_TYPE", referencedColumnName = "STL_TYPE")})
    @XmlElement(name = "storageLocation")
    @XmlIDREF
    protected StorageLocation storageLocation;

    public StockInfo() {
        this("");
    }

    public StockInfo(String uuid) {
        this(0, "StockInfo", uuid,
                Calendar.getInstance().getTime(), null,
                StockState.ST_INPUT, 0.0,
                new Price(), new Price(),
                new Price(), new Price(),
                null, null,
                new StockInvestor(),
                new StorageLocation(),
                null,
                new ArrayList(),
                0);
    }

    public StockInfo(
            Integer id, String type, String uuid,
            Date inputDate, Date outputDate, StockState stockState,
            Double quantity,
            Price unitCost,
            Price totalCost,
            Price unitCostWithOthersCosts,
            Price totalCostWithOthersCosts,
            Price unitSold,
            Price totalSold,
            StockInvestor stockInvestor, StorageLocation storageLocation,
            Cost cost, List<Cost> otherCostsByUnitList,
            Integer stiVersion) {
        super(id, type, uuid);
        this.stiVersion = stiVersion;
        this.cost = cost;
        this.stockInvestor = stockInvestor;
        this.storageLocation = storageLocation;
        this.inputDate = inputDate;
        this.outputDate = outputDate;
        this.stockState = stockState;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
        this.unitCostWithOthersCosts = unitCostWithOthersCosts;
        this.totalCostWithOthersCosts = totalCostWithOthersCosts;
        this.unitSold = unitSold;
        this.totalSold = totalSold;
        this.otherCostsByUnitList = otherCostsByUnitList;

        init();
    }

    /**
     * Init object.
     */
    private void init() {
        setUnitCost(unitCost);
        setUnitCostWithOthersCosts(unitCostWithOthersCosts);
        setUnitSold(unitSold);
        setTotalCost(totalCost);
        setTotalCostWithOthersCosts(totalCostWithOthersCosts);
        setTotalSold(totalSold);

        for (Cost otherCost : otherCostsByUnitList) {
            otherCost.setStockInfoOwner(this);
        }
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public StockInvestor getStockInvestor() {
        return stockInvestor;
    }

    public void setStockInvestor(StockInvestor stockInvestor) {
        this.stockInvestor = stockInvestor;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        Objects.requireNonNull(inputDate, "InputDate is null.");
        this.inputDate = inputDate;
    }

    public Date getOutputDate() {
        return outputDate;
    }

    public void setOutputDate(Date outputDate) {
        Objects.requireNonNull(outputDate, "OutputDate is null.");
        StockUtils.checkInputAndOutputDate(inputDate, outputDate);
        this.outputDate = outputDate;
    }

    public StockState getStockState() {
        return stockState;
    }

    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        StockUtils.checkQuantity(quantity);
        this.quantity = quantity;
    }

    public Price getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Price unitCost) {
        Objects.requireNonNull(unitCost, "UnitCost is null.");
        StockUtils.checkUnitCost(unitCost);

        if (unitCost != null && unitCost.getParent() != this) {
            unitCost.setParent(this);
        }
        this.unitCost = unitCost;
    }

    public Price getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Price totalCost) {
        if (totalCost != null
                && totalCost.getParent() != this) {
            totalCost.setParent(this);
        }
        this.totalCost = totalCost;
    }

    public Price getUnitCostWithOthersCosts() {
        return unitCostWithOthersCosts;
    }

    public void setUnitCostWithOthersCosts(Price unitCostWithOthersCosts) {
        if (unitCostWithOthersCosts != null
                && unitCostWithOthersCosts.getParent() != this) {
            unitCostWithOthersCosts.setParent(this);
        }
        this.unitCostWithOthersCosts = unitCostWithOthersCosts;
    }

    public Price getTotalCostWithOthersCosts() {
        return totalCostWithOthersCosts;
    }

    public void setTotalCostWithOthersCosts(Price totalCostWithOthersCosts) {
        if (totalCostWithOthersCosts != null
                && totalCostWithOthersCosts.getParent() != this) {
            totalCostWithOthersCosts.setParent(this);
        }
        this.totalCostWithOthersCosts = totalCostWithOthersCosts;
    }

    public Price getUnitSold() {
        if (unitSold == null) {
            unitSold = new Price(getUnitCost().getCoin(), 0.0);
        }
        return unitSold;
    }

    public void setUnitSold(Price unitSold) {
        if (unitSold != null
                && unitSold.getParent() != this) {
            unitSold.setParent(this);
        }
        this.unitSold = unitSold;
    }

    public Price getTotalSold() {
        if (totalSold == null) {
            totalSold = new Price(getUnitCost().getCoin(), 0.0);
        }
        return totalSold;
    }

    public void setTotalSold(Price totalSold) {
        if (totalSold != null
                && totalSold.getParent() != this) {
            totalSold.setParent(this);
        }
        this.totalSold = totalSold;
    }

    public List<Cost> getOtherCostsByUnitList() {
        return otherCostsByUnitList;
    }

    public void setOtherCostsByUnitList(List<Cost> otherCostsByUnitList) {
        Objects.requireNonNull(otherCostsByUnitList,
                "OtherCostsByUnitList is null.");
        this.otherCostsByUnitList = otherCostsByUnitList;
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getStockState());
        _uuid += Objects.hashCode(getQuantity());
        _uuid += Objects.hashCode(getInputDate());
        _uuid += Objects.hashCode(getOutputDate());
        _uuid += Objects.hashCode(getUnitCost());
        _uuid += Objects.hashCode(getUnitCostWithOthersCosts());
        _uuid += Objects.hashCode(getUnitSold());
        _uuid += Objects.hashCode(getTotalCost());
        _uuid += Objects.hashCode(getTotalCostWithOthersCosts());
        _uuid += Objects.hashCode(getTotalSold());
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
        if (!(object instanceof StockInfo)) {
            return false;
        }
        final StockInfo other = (StockInfo) object;
        if (!Objects.equals(this.getStockState(), other.getStockState())) {
            return false;
        }
        if (!Objects.equals(this.getQuantity(), other.getQuantity())) {
            return false;
        }
        if (!Objects.equals(this.getInputDate(), other.getInputDate())) {
            return false;
        }
        if (!Objects.equals(this.getOutputDate(), other.getOutputDate())) {
            return false;
        }
        if (!Objects.equals(this.getUnitCost(), other.getUnitCost())) {
            return false;
        }
        if (!Objects.equals(this.getUnitCostWithOthersCosts(),
                other.getUnitCostWithOthersCosts())) {
            return false;
        }
        if (!Objects.equals(this.getUnitSold(), other.getUnitSold())) {
            return false;
        }
        if (!Objects.equals(this.getTotalCost(), other.getTotalCost())) {
            return false;
        }
        if (!Objects.equals(this.getTotalCostWithOthersCosts(),
                other.getTotalCostWithOthersCosts())) {
            return false;
        }
        if (!Objects.equals(this.getTotalSold(), other.getTotalSold())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "StockInfo[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + stiVersion
                + ",\n\tquantity=" + getQuantity()
                + ",\n\tunitCost=" + getUnitCost()
                + ",\n\tunitCostWithOthersCosts=" + getUnitCostWithOthersCosts()
                + "\n]";
    }
}
