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
package com.rollsoftware.br.common.db.entity;

import com.rollsoftware.br.util.CypherUtils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rogério
 * @date October, 2016
 */
@Entity
@Table(name = "OBJ_OBJECT_DATA",
        schema = "LFMS_DB",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"OBJ_ID_PK"})
            ,@UniqueConstraint(columnNames = {"OBJ_TYPE", "OBJ_UUID_PK"})
        }
)
@IdClass(ObjectData.ObjectDataPK.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
        name = "OBJ_TYPE",
        discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
    @NamedQuery(name = "ObjectData.findAll",
            query = "SELECT o FROM ObjectData o")
    , @NamedQuery(name = "ObjectData.findById",
            query = "SELECT o FROM ObjectData o"
            + " WHERE o.id = :id")
    , @NamedQuery(name = "ObjectData.findByUUID",
            query = "SELECT o FROM ObjectData o"
            + " WHERE o.uuid = :uuid")
    , @NamedQuery(name = "ObjectData.findByType",
            query = "SELECT o FROM ObjectData o"
            + " WHERE o.type = :type")
    , @NamedQuery(name = "ObjectData.findByDateCreated",
            query = "SELECT o FROM ObjectData o"
            + " WHERE o.dateCreated = :dateCreated")
    , @NamedQuery(name = "ObjectData.findByDateAccessed",
            query = "SELECT o FROM ObjectData o"
            + " WHERE o.dateAccessed = :dateAccessed")
})
@XmlRootElement(name = "object")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "object", propOrder = {
    "id", "type", "uuid",
    "dateCreated", "dateAccessed"
})
public class ObjectData
        implements Serializable, ObjectInterface {

    private static final long serialVersionUID = 1L;

    @Version
    @Basic(optional = false)
    @NotNull
    @Column(name = "OBJ_VERSION", nullable = false)
    @XmlTransient
    private Integer objVersion;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OBJ_ID_PK", nullable = false)
    private Integer id;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "OBJ_TYPE", nullable = false, length = 128)
    @XmlElement(name = "objType")
    private String type;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "OBJ_UUID_PK", nullable = false, length = 128)
    @XmlAttribute
    @XmlID
    private String uuid;

    @Basic(optional = false)
    @NotNull
    @Column(name = "OBJ_DATECREATED", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Basic(optional = false)
    @NotNull
    @Column(name = "OBJ_DATEACCESSED", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAccessed;

    public ObjectData() {
        this(0);
    }

    public ObjectData(Integer id) {
        this(id, "ObjectData");
    }

    public ObjectData(Integer id, String uuid) {
        this(id, uuid,
                Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(),
                0);
    }

    public ObjectData(Integer id, String type, String uuid) {
        this(id, type, uuid,
                Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(),
                0);
    }

    private ObjectData(
            Integer id, String type, String uuid,
            Date dateCreated, Date dateAccessed,
            Integer objVersion) {
        this.id = id;
        this.type = type;
        this.uuid = uuid;
        this.dateCreated = dateCreated;
        this.dateAccessed = dateAccessed;
        this.objVersion = objVersion;
    }

    private ObjectData(
            Integer id, String uuid,
            Date dateCreated, Date dateAccessed,
            Integer objVersion) {
        this(id, "", uuid, dateCreated, dateAccessed, objVersion);
        this.type = this.getClass().getSimpleName();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateAccessed() {
        return dateAccessed;
    }

    public void setDateAccessed(Date dateAccessed) {
        this.dateAccessed = dateAccessed;
    }

    @Override
    public void generateUUID() {
        String _uuid = getUUID();
        if (_uuid == null || "".equals(_uuid) || _uuid.length() != 32) {
            _uuid = CypherUtils.generateUUID();
            setUUID(_uuid);
        }
    }

    @Override
    public <T extends ObjectDataInterfacePK> T getODPK() {
        return (T) new ObjectDataPK(getType(), getUUID());
    }

    @Override
    public boolean equalsODPK(ObjectDataInterfacePK odpk) {
        if (odpk == null) {
            return false;
        }
        if (!(odpk instanceof ObjectDataInterfacePK)) {
            return false;
        }
        return Objects.equals(getODPK(), odpk);
    }

    @Override
    public int hashCode() {
        int _hash = super.hashCode();
        _hash += Objects.hashCode(getId());
        _hash += Objects.hashCode(getUUID());
        _hash += Objects.hashCode(getType());
        return _hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work
        // in the case the id fields are not set
        if (object == null) {
            return false;
        }
        if (!(object instanceof ObjectData)) {
            return false;
        }
        final ObjectData other = (ObjectData) object;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getUUID(), other.getUUID())) {
            return false;
        }
        if (!Objects.equals(this.getType(), other.getType())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ObjectData[id=" + getId()
                + ", uuid=" + getUUID()
                + ", type=" + getType() + "]";
    }

    @XmlRootElement(name = "objectDataPK")
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "objectDataPK", propOrder = {
        "type", "uuid"
    })
    public static class ObjectDataPK
            implements ObjectDataInterfacePK, Serializable {

        @Column(name = "TYPE_PK")
        private String type;

        @Column(name = "UUID_PK")
        private String uuid;

        public ObjectDataPK() {
            this("", "");
        }

        public ObjectDataPK(String type, String uuid) {
            this.type = type;
            this.uuid = uuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUUID() {
            return uuid;
        }

        public void setUUID(String uuid) {
            this.uuid = uuid;
        }

        @Override
        public int hashCode() {
            int _hash = super.hashCode();
            _hash += Objects.hashCode(getUUID());
            _hash += Objects.hashCode(getType());
            return _hash;
        }

        @Override
        public boolean equals(Object object) {
            // TODO: Warning - this method won't work
            // in the case the id fields are not set
            if (object == null) {
                return false;
            }
            if (!(object instanceof ObjectDataPK)) {
                return false;
            }
            final ObjectDataPK other = (ObjectDataPK) object;
            if (!Objects.equals(this.getUUID(), other.getUUID())) {
                return false;
            }
            if (!Objects.equals(this.getType(), other.getType())) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "uuid=" + getUUID() + ", type=" + getType();
        }
    }
}
