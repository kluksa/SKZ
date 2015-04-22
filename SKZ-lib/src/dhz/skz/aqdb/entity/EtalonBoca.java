/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "etalon_boca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtalonBoca.findAll", query = "SELECT e FROM EtalonBoca e"),
    @NamedQuery(name = "EtalonBoca.findByOpremaId", query = "SELECT e FROM EtalonBoca e WHERE e.etalonBocaPK.opremaId = :opremaId"),
    @NamedQuery(name = "EtalonBoca.findByKomponentaId", query = "SELECT e FROM EtalonBoca e WHERE e.etalonBocaPK.komponentaId = :komponentaId"),
    @NamedQuery(name = "EtalonBoca.findByKoncentracija", query = "SELECT e FROM EtalonBoca e WHERE e.koncentracija = :koncentracija"),
    @NamedQuery(name = "EtalonBoca.findByNesigurnost", query = "SELECT e FROM EtalonBoca e WHERE e.nesigurnost = :nesigurnost"),
    @NamedQuery(name = "EtalonBoca.findBySljedivost", query = "SELECT e FROM EtalonBoca e WHERE e.sljedivost = :sljedivost")})
public class EtalonBoca implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EtalonBocaPK etalonBocaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double koncentracija;
    private Double nesigurnost;
    @Size(max = 90)
    private String sljedivost;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjaj;

    public EtalonBoca() {
    }

    public EtalonBoca(EtalonBocaPK etalonBocaPK) {
        this.etalonBocaPK = etalonBocaPK;
    }

    public EtalonBoca(int opremaId, int komponentaId) {
        this.etalonBocaPK = new EtalonBocaPK(opremaId, komponentaId);
    }

    public EtalonBocaPK getEtalonBocaPK() {
        return etalonBocaPK;
    }

    public void setEtalonBocaPK(EtalonBocaPK etalonBocaPK) {
        this.etalonBocaPK = etalonBocaPK;
    }

    public Double getKoncentracija() {
        return koncentracija;
    }

    public void setKoncentracija(Double koncentracija) {
        this.koncentracija = koncentracija;
    }

    public Double getNesigurnost() {
        return nesigurnost;
    }

    public void setNesigurnost(Double nesigurnost) {
        this.nesigurnost = nesigurnost;
    }

    public String getSljedivost() {
        return sljedivost;
    }

    public void setSljedivost(String sljedivost) {
        this.sljedivost = sljedivost;
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

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etalonBocaPK != null ? etalonBocaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonBoca)) {
            return false;
        }
        EtalonBoca other = (EtalonBoca) object;
        if ((this.etalonBocaPK == null && other.etalonBocaPK != null) || (this.etalonBocaPK != null && !this.etalonBocaPK.equals(other.etalonBocaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonBoca[ etalonBocaPK=" + etalonBocaPK + " ]";
    }
    
}
