/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "servisi", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servisi.findAll", query = "SELECT s FROM Servisi s"),
    @NamedQuery(name = "Servisi.findByKvaroviId", query = "SELECT s FROM Servisi s WHERE s.kvaroviId = :kvaroviId"),
    @NamedQuery(name = "Servisi.findByCijenaKn", query = "SELECT s FROM Servisi s WHERE s.cijenaKn = :cijenaKn")})
public class Servisi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kvarovi_id")
    private Integer kvaroviId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "cijena_kn")
    private BigDecimal cijenaKn;
    @JoinColumn(name = "kvarovi_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Kvarovi kvarovi;

    public Servisi() {
    }

    public Servisi(Integer kvaroviId) {
        this.kvaroviId = kvaroviId;
    }

    public Servisi(Integer kvaroviId, BigDecimal cijenaKn) {
        this.kvaroviId = kvaroviId;
        this.cijenaKn = cijenaKn;
    }

    public Integer getKvaroviId() {
        return kvaroviId;
    }

    public void setKvaroviId(Integer kvaroviId) {
        this.kvaroviId = kvaroviId;
    }

    public BigDecimal getCijenaKn() {
        return cijenaKn;
    }

    public void setCijenaKn(BigDecimal cijenaKn) {
        this.cijenaKn = cijenaKn;
    }

    public Kvarovi getKvarovi() {
        return kvarovi;
    }

    public void setKvarovi(Kvarovi kvarovi) {
        this.kvarovi = kvarovi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kvaroviId != null ? kvaroviId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servisi)) {
            return false;
        }
        Servisi other = (Servisi) object;
        if ((this.kvaroviId == null && other.kvaroviId != null) || (this.kvaroviId != null && !this.kvaroviId.equals(other.kvaroviId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Servisi[ kvaroviId=" + kvaroviId + " ]";
    }
    
}
