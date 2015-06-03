/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class KomponentaHasRelevantneDirektivePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "komponenta_id")
    private int komponentaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "relevantne_direktive_id")
    private int relevantneDirektiveId;

    public KomponentaHasRelevantneDirektivePK() {
    }

    public KomponentaHasRelevantneDirektivePK(int komponentaId, int relevantneDirektiveId) {
        this.komponentaId = komponentaId;
        this.relevantneDirektiveId = relevantneDirektiveId;
    }

    public int getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(int komponentaId) {
        this.komponentaId = komponentaId;
    }

    public int getRelevantneDirektiveId() {
        return relevantneDirektiveId;
    }

    public void setRelevantneDirektiveId(int relevantneDirektiveId) {
        this.relevantneDirektiveId = relevantneDirektiveId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) komponentaId;
        hash += (int) relevantneDirektiveId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KomponentaHasRelevantneDirektivePK)) {
            return false;
        }
        KomponentaHasRelevantneDirektivePK other = (KomponentaHasRelevantneDirektivePK) object;
        if (this.komponentaId != other.komponentaId) {
            return false;
        }
        if (this.relevantneDirektiveId != other.relevantneDirektiveId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.KomponentaHasRelevantneDirektivePK[ komponentaId=" + komponentaId + ", relevantneDirektiveId=" + relevantneDirektiveId + " ]";
    }
    
}
