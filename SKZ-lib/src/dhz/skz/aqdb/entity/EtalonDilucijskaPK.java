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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kraljevic
 */
@Embeddable
public class EtalonDilucijskaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "oprema_id")
    private int opremaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "primjena_od")
    @Temporal(TemporalType.DATE)
    private Date primjenaOd;

    public EtalonDilucijskaPK() {
    }

    public EtalonDilucijskaPK(int opremaId, Date primjenaOd) {
        this.opremaId = opremaId;
        this.primjenaOd = primjenaOd;
    }

    public int getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(int opremaId) {
        this.opremaId = opremaId;
    }

    public Date getPrimjenaOd() {
        return primjenaOd;
    }

    public void setPrimjenaOd(Date primjenaOd) {
        this.primjenaOd = primjenaOd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) opremaId;
        hash += (primjenaOd != null ? primjenaOd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonDilucijskaPK)) {
            return false;
        }
        EtalonDilucijskaPK other = (EtalonDilucijskaPK) object;
        if (this.opremaId != other.opremaId) {
            return false;
        }
        if ((this.primjenaOd == null && other.primjenaOd != null) || (this.primjenaOd != null && !this.primjenaOd.equals(other.primjenaOd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonDilucijskaPK[ opremaId=" + opremaId + ", primjenaOd=" + primjenaOd + " ]";
    }
    
}
