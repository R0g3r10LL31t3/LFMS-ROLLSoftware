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
@Table(name = "STL_STORAGE_LOCATION",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"STL_UUID_FK", "STL_TYPE"})
            ,@UniqueConstraint(columnNames = {"STL_NAME"})
        }
)
@DiscriminatorValue("StorageLocation")
@PrimaryKeyJoinColumns({
    @PrimaryKeyJoinColumn(name = "STL_UUID_FK", referencedColumnName = "OBJ_UUID_PK")
    ,@PrimaryKeyJoinColumn(name = "STL_TYPE", referencedColumnName = "OBJ_TYPE")
})
@NamedQueries({
    @NamedQuery(name = "StorageLocation.findAll",
            query = "SELECT s FROM StorageLocation s")
    , @NamedQuery(name = "StorageLocation.findByName",
            query = "SELECT s FROM StorageLocation s"
            + " WHERE s.name = :name")
    , @NamedQuery(name = "StorageLocation.findByAddress",
            query = "SELECT s FROM StorageLocation s"
            + " WHERE s.address = :address")
})
@XmlRootElement(name = "storageLocation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "storageLocation", propOrder = {
    "name", "address"
})
public class StorageLocation extends ObjectData implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Version
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "STL_VERSION", nullable = false)
    @Transient
    @XmlTransient
    private Integer stlVersion;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "STL_NAME", nullable = false, length = 256)
    protected String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "STL_ADDRESS", nullable = false, length = 256)
    protected String address;

    public StorageLocation() {
        this("");
    }

    public StorageLocation(String uuid) {
        this(0, "StorageLocation", uuid,
                "", "", 0);
    }

    public StorageLocation(Integer id, String type, String uuid,
            String name, String address,
            Integer stlVersion) {
        super(id, type, uuid);
        this.name = name;
        this.address = address;
        this.stlVersion = stlVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int _uuid = super.hashCode();
        _uuid += Objects.hashCode(getName());
        _uuid += Objects.hashCode(getAddress());
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
        if (!(object instanceof StorageLocation)) {
            return false;
        }
        final StorageLocation other = (StorageLocation) object;
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getAddress(), other.getAddress())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StorageLocation[\n\t" + "id=" + getId()
                + ", uuid=" + getUUID()
                + ", version=" + stlVersion
                + ",\n\tname=" + getName()
                + ",\n\taddress=" + getAddress()
                + "\n]";
    }
}
