/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjeravanje_podaci", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjeravanjePodaci.findAll", query = "SELECT u FROM UmjeravanjePodaci u"),
    @NamedQuery(name = "UmjeravanjePodaci.findById", query = "SELECT u FROM UmjeravanjePodaci u WHERE u.id = :id"),
    @NamedQuery(name = "UmjeravanjePodaci.findByVrijeme", query = "SELECT u FROM UmjeravanjePodaci u WHERE u.vrijeme = :vrijeme"),
    @NamedQuery(name = "UmjeravanjePodaci.findByVrijednost", query = "SELECT u FROM UmjeravanjePodaci u WHERE u.vrijednost = :vrijednost")})
public class UmjeravanjePodaci implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private long vrijednost;
    @JoinColumn(name = "umjerne_to\u010dke_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private UmjerneTocke umjerneTočkeId;

    public UmjeravanjePodaci() {
    }

    public UmjeravanjePodaci(Integer id) {
        this.id = id;
    }

    public UmjeravanjePodaci(Integer id, Date vrijeme, long vrijednost) {
        this.id = id;
        this.vrijeme = vrijeme;
        this.vrijednost = vrijednost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public long getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(long vrijednost) {
        this.vrijednost = vrijednost;
    }

    public UmjerneTocke getUmjerneTočkeId() {
        return umjerneTočkeId;
    }

    public void setUmjerneTočkeId(UmjerneTocke umjerneTočkeId) {
        this.umjerneTočkeId = umjerneTočkeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjePodaci)) {
            return false;
        }
        UmjeravanjePodaci other = (UmjeravanjePodaci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjeravanjePodaci[ id=" + id + " ]";
    }
    
}
