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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjeravanje_has_okolisni_uvjeti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjeravanjeHasOkolisniUvjeti.findAll", query = "SELECT u FROM UmjeravanjeHasOkolisniUvjeti u"),
    @NamedQuery(name = "UmjeravanjeHasOkolisniUvjeti.findByUmjeravanjeId", query = "SELECT u FROM UmjeravanjeHasOkolisniUvjeti u WHERE u.umjeravanjeHasOkolisniUvjetiPK.umjeravanjeId = :umjeravanjeId"),
    @NamedQuery(name = "UmjeravanjeHasOkolisniUvjeti.findByKomponentaId", query = "SELECT u FROM UmjeravanjeHasOkolisniUvjeti u WHERE u.umjeravanjeHasOkolisniUvjetiPK.komponentaId = :komponentaId"),
    @NamedQuery(name = "UmjeravanjeHasOkolisniUvjeti.findByMin", query = "SELECT u FROM UmjeravanjeHasOkolisniUvjeti u WHERE u.min = :min"),
    @NamedQuery(name = "UmjeravanjeHasOkolisniUvjeti.findByMax", query = "SELECT u FROM UmjeravanjeHasOkolisniUvjeti u WHERE u.max = :max")})
public class UmjeravanjeHasOkolisniUvjeti implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UmjeravanjeHasOkolisniUvjetiPK umjeravanjeHasOkolisniUvjetiPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "min")
    private Double min;
    @Column(name = "max")
    private Double max;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    @JoinColumn(name = "mjerna_jedinica_id", referencedColumnName = "id")
    @ManyToOne
    private MjerneJedinice mjernaJedinicaId;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Umjeravanje umjeravanje;

    public UmjeravanjeHasOkolisniUvjeti() {
    }

    public UmjeravanjeHasOkolisniUvjeti(UmjeravanjeHasOkolisniUvjetiPK umjeravanjeHasOkolisniUvjetiPK) {
        this.umjeravanjeHasOkolisniUvjetiPK = umjeravanjeHasOkolisniUvjetiPK;
    }

    public UmjeravanjeHasOkolisniUvjeti(int umjeravanjeId, int komponentaId) {
        this.umjeravanjeHasOkolisniUvjetiPK = new UmjeravanjeHasOkolisniUvjetiPK(umjeravanjeId, komponentaId);
    }

    public UmjeravanjeHasOkolisniUvjetiPK getUmjeravanjeHasOkolisniUvjetiPK() {
        return umjeravanjeHasOkolisniUvjetiPK;
    }

    public void setUmjeravanjeHasOkolisniUvjetiPK(UmjeravanjeHasOkolisniUvjetiPK umjeravanjeHasOkolisniUvjetiPK) {
        this.umjeravanjeHasOkolisniUvjetiPK = umjeravanjeHasOkolisniUvjetiPK;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    public MjerneJedinice getMjernaJedinicaId() {
        return mjernaJedinicaId;
    }

    public void setMjernaJedinicaId(MjerneJedinice mjernaJedinicaId) {
        this.mjernaJedinicaId = mjernaJedinicaId;
    }

    public Umjeravanje getUmjeravanje() {
        return umjeravanje;
    }

    public void setUmjeravanje(Umjeravanje umjeravanje) {
        this.umjeravanje = umjeravanje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (umjeravanjeHasOkolisniUvjetiPK != null ? umjeravanjeHasOkolisniUvjetiPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjeHasOkolisniUvjeti)) {
            return false;
        }
        UmjeravanjeHasOkolisniUvjeti other = (UmjeravanjeHasOkolisniUvjeti) object;
        if ((this.umjeravanjeHasOkolisniUvjetiPK == null && other.umjeravanjeHasOkolisniUvjetiPK != null) || (this.umjeravanjeHasOkolisniUvjetiPK != null && !this.umjeravanjeHasOkolisniUvjetiPK.equals(other.umjeravanjeHasOkolisniUvjetiPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.novo.UmjeravanjeHasOkolisniUvjeti[ umjeravanjeHasOkolisniUvjetiPK=" + umjeravanjeHasOkolisniUvjetiPK + " ]";
    }
    
}
