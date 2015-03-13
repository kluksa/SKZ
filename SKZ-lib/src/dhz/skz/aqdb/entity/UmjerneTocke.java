/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjerne_tocke")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjerneTocke.findAll", query = "SELECT u FROM UmjerneTocke u"),
    @NamedQuery(name = "UmjerneTocke.findById", query = "SELECT u FROM UmjerneTocke u WHERE u.id = :id"),
    @NamedQuery(name = "UmjerneTocke.findByReferentnaVrijednost", query = "SELECT u FROM UmjerneTocke u WHERE u.referentnaVrijednost = :referentnaVrijednost"),
    @NamedQuery(name = "UmjerneTocke.findByMjerenaVrijednost", query = "SELECT u FROM UmjerneTocke u WHERE u.mjerenaVrijednost = :mjerenaVrijednost"),
    @NamedQuery(name = "UmjerneTocke.findByMjernaNesigurnost", query = "SELECT u FROM UmjerneTocke u WHERE u.mjernaNesigurnost = :mjernaNesigurnost")})
public class UmjerneTocke implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "referentna_vrijednost")
    private long referentnaVrijednost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mjerena_vrijednost")
    private long mjerenaVrijednost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mjerna_nesigurnost")
    private long mjernaNesigurnost;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "umjerneTo\u010dkeId")
    private Collection<UmjeravanjePodaci> umjeravanjePodaciCollection;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Umjeravanje umjeravanjeId;

    public UmjerneTocke() {
    }

    public UmjerneTocke(Integer id) {
        this.id = id;
    }

    public UmjerneTocke(Integer id, long referentnaVrijednost, long mjerenaVrijednost, long mjernaNesigurnost) {
        this.id = id;
        this.referentnaVrijednost = referentnaVrijednost;
        this.mjerenaVrijednost = mjerenaVrijednost;
        this.mjernaNesigurnost = mjernaNesigurnost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getReferentnaVrijednost() {
        return referentnaVrijednost;
    }

    public void setReferentnaVrijednost(long referentnaVrijednost) {
        this.referentnaVrijednost = referentnaVrijednost;
    }

    public long getMjerenaVrijednost() {
        return mjerenaVrijednost;
    }

    public void setMjerenaVrijednost(long mjerenaVrijednost) {
        this.mjerenaVrijednost = mjerenaVrijednost;
    }

    public long getMjernaNesigurnost() {
        return mjernaNesigurnost;
    }

    public void setMjernaNesigurnost(long mjernaNesigurnost) {
        this.mjernaNesigurnost = mjernaNesigurnost;
    }

    @XmlTransient
    public Collection<UmjeravanjePodaci> getUmjeravanjePodaciCollection() {
        return umjeravanjePodaciCollection;
    }

    public void setUmjeravanjePodaciCollection(Collection<UmjeravanjePodaci> umjeravanjePodaciCollection) {
        this.umjeravanjePodaciCollection = umjeravanjePodaciCollection;
    }

    public Umjeravanje getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(Umjeravanje umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
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
        if (!(object instanceof UmjerneTocke)) {
            return false;
        }
        UmjerneTocke other = (UmjerneTocke) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjerneTocke[ id=" + id + " ]";
    }
    
}
