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
public class UmjeravanjeHasIspitneVelicinePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "umjeravanje_id")
    private int umjeravanjeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ispitne_velicine_id")
    private int ispitneVelicineId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "komponenta_id")
    private int komponentaId;

    public UmjeravanjeHasIspitneVelicinePK() {
    }

    public UmjeravanjeHasIspitneVelicinePK(int umjeravanjeId, int ispitneVelicineId, int komponentaId) {
        this.umjeravanjeId = umjeravanjeId;
        this.ispitneVelicineId = ispitneVelicineId;
        this.komponentaId = komponentaId;
    }

    public int getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(int umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public int getIspitneVelicineId() {
        return ispitneVelicineId;
    }

    public void setIspitneVelicineId(int ispitneVelicineId) {
        this.ispitneVelicineId = ispitneVelicineId;
    }

    public int getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(int komponentaId) {
        this.komponentaId = komponentaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) umjeravanjeId;
        hash += (int) ispitneVelicineId;
        hash += (int) komponentaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjeHasIspitneVelicinePK)) {
            return false;
        }
        UmjeravanjeHasIspitneVelicinePK other = (UmjeravanjeHasIspitneVelicinePK) object;
        if (this.umjeravanjeId != other.umjeravanjeId) {
            return false;
        }
        if (this.ispitneVelicineId != other.ispitneVelicineId) {
            return false;
        }
        if (this.komponentaId != other.komponentaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicinePK[ umjeravanjeId=" + umjeravanjeId + ", ispitneVelicineId=" + ispitneVelicineId + ", komponentaId=" + komponentaId + " ]";
    }
    
}
