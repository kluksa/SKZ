/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
@Table(name = "etalon_cisti_zrak_kvaliteta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtalonCistiZrakKvaliteta.findAll", query = "SELECT e FROM EtalonCistiZrakKvaliteta e"),
    @NamedQuery(name = "EtalonCistiZrakKvaliteta.findByOpremaId", query = "SELECT e FROM EtalonCistiZrakKvaliteta e WHERE e.etalonCistiZrakKvalitetaPK.opremaId = :opremaId"),
    @NamedQuery(name = "EtalonCistiZrakKvaliteta.findByKomponentaId", query = "SELECT e FROM EtalonCistiZrakKvaliteta e WHERE e.etalonCistiZrakKvalitetaPK.komponentaId = :komponentaId"),
    @NamedQuery(name = "EtalonCistiZrakKvaliteta.findByMaxUdio", query = "SELECT e FROM EtalonCistiZrakKvaliteta e WHERE e.maxUdio = :maxUdio")})
public class EtalonCistiZrakKvaliteta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EtalonCistiZrakKvalitetaPK etalonCistiZrakKvalitetaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "max_udio")
    private Float maxUdio;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjaj;

    public EtalonCistiZrakKvaliteta() {
    }

    public EtalonCistiZrakKvaliteta(EtalonCistiZrakKvalitetaPK etalonCistiZrakKvalitetaPK) {
        this.etalonCistiZrakKvalitetaPK = etalonCistiZrakKvalitetaPK;
    }

    public EtalonCistiZrakKvaliteta(int opremaId, int komponentaId) {
        this.etalonCistiZrakKvalitetaPK = new EtalonCistiZrakKvalitetaPK(opremaId, komponentaId);
    }

    public EtalonCistiZrakKvalitetaPK getEtalonCistiZrakKvalitetaPK() {
        return etalonCistiZrakKvalitetaPK;
    }

    public void setEtalonCistiZrakKvalitetaPK(EtalonCistiZrakKvalitetaPK etalonCistiZrakKvalitetaPK) {
        this.etalonCistiZrakKvalitetaPK = etalonCistiZrakKvalitetaPK;
    }

    public Float getMaxUdio() {
        return maxUdio;
    }

    public void setMaxUdio(Float maxUdio) {
        this.maxUdio = maxUdio;
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
        hash += (etalonCistiZrakKvalitetaPK != null ? etalonCistiZrakKvalitetaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonCistiZrakKvaliteta)) {
            return false;
        }
        EtalonCistiZrakKvaliteta other = (EtalonCistiZrakKvaliteta) object;
        if ((this.etalonCistiZrakKvalitetaPK == null && other.etalonCistiZrakKvalitetaPK != null) || (this.etalonCistiZrakKvalitetaPK != null && !this.etalonCistiZrakKvalitetaPK.equals(other.etalonCistiZrakKvalitetaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonCistiZrakKvaliteta[ etalonCistiZrakKvalitetaPK=" + etalonCistiZrakKvalitetaPK + " ]";
    }
    
}
