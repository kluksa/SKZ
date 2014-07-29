/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjerni_koeficijenti", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjerniKoeficijenti.findAll", query = "SELECT u FROM UmjerniKoeficijenti u"),
    @NamedQuery(name = "UmjerniKoeficijenti.findByUmjeravanjeId", query = "SELECT u FROM UmjerniKoeficijenti u WHERE u.umjeravanjeId = :umjeravanjeId"),
    @NamedQuery(name = "UmjerniKoeficijenti.findBySlope", query = "SELECT u FROM UmjerniKoeficijenti u WHERE u.slope = :slope"),
    @NamedQuery(name = "UmjerniKoeficijenti.findByOffset", query = "SELECT u FROM UmjerniKoeficijenti u WHERE u.offset = :offset")})
public class UmjerniKoeficijenti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "umjeravanje_id")
    private Integer umjeravanjeId;
    @Basic(optional = false)
    @Column(name = "slope")
    private long slope;
    @Basic(optional = false)
    @Column(name = "offset")
    private long offset;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Umjeravanje umjeravanje;

    public UmjerniKoeficijenti() {
    }

    public UmjerniKoeficijenti(Integer umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public UmjerniKoeficijenti(Integer umjeravanjeId, long slope, long offset) {
        this.umjeravanjeId = umjeravanjeId;
        this.slope = slope;
        this.offset = offset;
    }

    public Integer getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(Integer umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public long getSlope() {
        return slope;
    }

    public void setSlope(long slope) {
        this.slope = slope;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
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
        hash += (umjeravanjeId != null ? umjeravanjeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjerniKoeficijenti)) {
            return false;
        }
        UmjerniKoeficijenti other = (UmjerniKoeficijenti) object;
        if ((this.umjeravanjeId == null && other.umjeravanjeId != null) || (this.umjeravanjeId != null && !this.umjeravanjeId.equals(other.umjeravanjeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjerniKoeficijenti[ umjeravanjeId=" + umjeravanjeId + " ]";
    }

}
