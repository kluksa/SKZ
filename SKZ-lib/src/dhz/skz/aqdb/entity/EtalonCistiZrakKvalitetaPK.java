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
public class EtalonCistiZrakKvalitetaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "oprema_id")
    private int opremaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "komponenta_id")
    private int komponentaId;

    public EtalonCistiZrakKvalitetaPK() {
    }

    public EtalonCistiZrakKvalitetaPK(int opremaId, int komponentaId) {
        this.opremaId = opremaId;
        this.komponentaId = komponentaId;
    }

    public int getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(int opremaId) {
        this.opremaId = opremaId;
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
        hash += (int) opremaId;
        hash += (int) komponentaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonCistiZrakKvalitetaPK)) {
            return false;
        }
        EtalonCistiZrakKvalitetaPK other = (EtalonCistiZrakKvalitetaPK) object;
        if (this.opremaId != other.opremaId) {
            return false;
        }
        if (this.komponentaId != other.komponentaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonCistiZrakKvalitetaPK[ opremaId=" + opremaId + ", komponentaId=" + komponentaId + " ]";
    }
    
}
