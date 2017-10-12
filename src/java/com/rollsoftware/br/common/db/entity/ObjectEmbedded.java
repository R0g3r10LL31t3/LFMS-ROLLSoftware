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

import java.util.Objects;
import javax.persistence.Transient;
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
 * @date March, 2017
 */
@XmlRootElement(name = "objectEmbedded")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "objectEmbedded")
public abstract class ObjectEmbedded implements ObjectInterface {

    @Transient
    private ObjectInterface objectInterface;

    public ObjectEmbedded() {
        this(null);
    }

    private ObjectEmbedded(ObjectInterface objectInterface) {
        this.objectInterface = objectInterface;
    }

    public void setParent(ObjectInterface objectInterface) {
        this.objectInterface = objectInterface;
    }

    @XmlTransient
    public ObjectInterface getParent() {
        return objectInterface;
    }

    @Override
    public void generateUUID() {
        if (getParent() != null) {
            getParent().generateUUID();
        }
    }

    @Override
    @XmlElement(name = "id")
    public Integer getId() {
        if (getParent() != null) {
            return getParent().getId();
        }

        return null;
    }

    @Override
    @XmlID
    @XmlAttribute(name = "uuid")
    public String getUUID() {
        if (getParent() != null) {
            return getParent().getUUID();
        }

        return null;
    }

    @Override
    public <T extends ObjectDataInterfacePK> T getODPK() {
        if (getParent() != null) {
            return (T) getParent().getODPK();
        }

        return (T) null;
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
        return _hash;
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
        if (!(object instanceof ObjectEmbedded)) {
            return false;
        }
        final ObjectEmbedded other = (ObjectEmbedded) object;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getUUID(), other.getUUID())) {
            return false;
        }
        return true;
    }
}
