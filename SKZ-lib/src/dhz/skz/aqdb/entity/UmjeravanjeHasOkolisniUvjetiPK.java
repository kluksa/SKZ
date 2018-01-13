/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kraljevic
 */
@Embeddable
public class UmjeravanjeHasOkolisniUvjetiPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "umjeravanje_id")
    private int umjeravanjeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "komponenta_id")
    private int komponentaId;

    public UmjeravanjeHasOkolisniUvjetiPK() {
    }

    public UmjeravanjeHasOkolisniUvjetiPK(int umjeravanjeId, int komponentaId) {
        this.umjeravanjeId = umjeravanjeId;
        this.komponentaId = komponentaId;
    }

    public int getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(int umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public int getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(int komponentaId) {
        this.komponentaId = komponentaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) umjeravanjeId;
        hash += (int) komponentaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjeHasOkolisniUvjetiPK)) {
            return false;
        }
        UmjeravanjeHasOkolisniUvjetiPK other = (UmjeravanjeHasOkolisniUvjetiPK) object;
        if (this.umjeravanjeId != other.umjeravanjeId) {
            return false;
        }
        if (this.komponentaId != other.komponentaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.novo.UmjeravanjeHasOkolisniUvjetiPK[ umjeravanjeId=" + umjeravanjeId + ", komponentaId=" + komponentaId + " ]";
    }
    
}
