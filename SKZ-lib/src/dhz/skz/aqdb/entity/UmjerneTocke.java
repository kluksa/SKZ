/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjerne_tocke")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjerneTocke.findAll", query = "SELECT u FROM UmjerneTocke u"),
    @NamedQuery(name = "UmjerneTocke.findById", query = "SELECT u FROM UmjerneTocke u WHERE u.umjerneTockePK.id = :id"),
    @NamedQuery(name = "UmjerneTocke.findByKomponentaId", query = "SELECT u FROM UmjerneTocke u WHERE u.umjerneTockePK.komponentaId = :komponentaId"),
    @NamedQuery(name = "UmjerneTocke.findByReferentnaVrijednost", query = "SELECT u FROM UmjerneTocke u WHERE u.referentnaVrijednost = :referentnaVrijednost"),
    @NamedQuery(name = "UmjerneTocke.findByMjerenaVrijednost", query = "SELECT u FROM UmjerneTocke u WHERE u.mjerenaVrijednost = :mjerenaVrijednost"),
    @NamedQuery(name = "UmjerneTocke.findByMjernaNesigurnost", query = "SELECT u FROM UmjerneTocke u WHERE u.mjernaNesigurnost = :mjernaNesigurnost")})
public class UmjerneTocke implements Serializable {
    @Size(max = 1)
    private String vrsta;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UmjerneTockePK umjerneTockePK;
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
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Umjeravanje umjeravanjeId;

    public UmjerneTocke() {
    }

    public UmjerneTocke(UmjerneTockePK umjerneTockePK) {
        this.umjerneTockePK = umjerneTockePK;
    }

    public UmjerneTocke(UmjerneTockePK umjerneTockePK, long referentnaVrijednost, long mjerenaVrijednost, long mjernaNesigurnost) {
        this.umjerneTockePK = umjerneTockePK;
        this.referentnaVrijednost = referentnaVrijednost;
        this.mjerenaVrijednost = mjerenaVrijednost;
        this.mjernaNesigurnost = mjernaNesigurnost;
    }

    public UmjerneTocke(int id, int komponentaId) {
        this.umjerneTockePK = new UmjerneTockePK(id, komponentaId);
    }

    public UmjerneTockePK getUmjerneTockePK() {
        return umjerneTockePK;
    }

    public void setUmjerneTockePK(UmjerneTockePK umjerneTockePK) {
        this.umjerneTockePK = umjerneTockePK;
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

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    public MjerneJedinice getMjerneJediniceId() {
        return mjerneJediniceId;
    }

    public void setMjerneJediniceId(MjerneJedinice mjerneJediniceId) {
        this.mjerneJediniceId = mjerneJediniceId;
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
        hash += (umjerneTockePK != null ? umjerneTockePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjerneTocke)) {
            return false;
        }
        UmjerneTocke other = (UmjerneTocke) object;
        if ((this.umjerneTockePK == null && other.umjerneTockePK != null) || (this.umjerneTockePK != null && !this.umjerneTockePK.equals(other.umjerneTockePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjerneTocke[ umjerneTockePK=" + umjerneTockePK + " ]";
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
    
}
