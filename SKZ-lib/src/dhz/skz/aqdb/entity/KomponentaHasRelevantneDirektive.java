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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "komponenta_has_relevantne_direktive")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KomponentaHasRelevantneDirektive.findAll", query = "SELECT k FROM KomponentaHasRelevantneDirektive k"),
    @NamedQuery(name = "KomponentaHasRelevantneDirektive.findByKomponentaId", query = "SELECT k FROM KomponentaHasRelevantneDirektive k WHERE k.komponentaHasRelevantneDirektivePK.komponentaId = :komponentaId"),
    @NamedQuery(name = "KomponentaHasRelevantneDirektive.findByRelevantneDirektiveId", query = "SELECT k FROM KomponentaHasRelevantneDirektive k WHERE k.komponentaHasRelevantneDirektivePK.relevantneDirektiveId = :relevantneDirektiveId")})
public class KomponentaHasRelevantneDirektive implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KomponentaHasRelevantneDirektivePK komponentaHasRelevantneDirektivePK;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;

    public KomponentaHasRelevantneDirektive() {
    }

    public KomponentaHasRelevantneDirektive(KomponentaHasRelevantneDirektivePK komponentaHasRelevantneDirektivePK) {
        this.komponentaHasRelevantneDirektivePK = komponentaHasRelevantneDirektivePK;
    }

    public KomponentaHasRelevantneDirektive(int komponentaId, int relevantneDirektiveId) {
        this.komponentaHasRelevantneDirektivePK = new KomponentaHasRelevantneDirektivePK(komponentaId, relevantneDirektiveId);
    }

    public KomponentaHasRelevantneDirektivePK getKomponentaHasRelevantneDirektivePK() {
        return komponentaHasRelevantneDirektivePK;
    }

    public void setKomponentaHasRelevantneDirektivePK(KomponentaHasRelevantneDirektivePK komponentaHasRelevantneDirektivePK) {
        this.komponentaHasRelevantneDirektivePK = komponentaHasRelevantneDirektivePK;
    }

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (komponentaHasRelevantneDirektivePK != null ? komponentaHasRelevantneDirektivePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KomponentaHasRelevantneDirektive)) {
            return false;
        }
        KomponentaHasRelevantneDirektive other = (KomponentaHasRelevantneDirektive) object;
        if ((this.komponentaHasRelevantneDirektivePK == null && other.komponentaHasRelevantneDirektivePK != null) || (this.komponentaHasRelevantneDirektivePK != null && !this.komponentaHasRelevantneDirektivePK.equals(other.komponentaHasRelevantneDirektivePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.KomponentaHasRelevantneDirektive[ komponentaHasRelevantneDirektivePK=" + komponentaHasRelevantneDirektivePK + " ]";
    }
    
}
