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
public class PlanOdrzavanjaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "oprema_id")
    private int opremaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrsta_odrzavanja_id")
    private int vrstaOdrzavanjaId;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date datum;

    public PlanOdrzavanjaPK() {
    }

    public PlanOdrzavanjaPK(int opremaId, int vrstaOdrzavanjaId, Date datum) {
        this.opremaId = opremaId;
        this.vrstaOdrzavanjaId = vrstaOdrzavanjaId;
        this.datum = datum;
    }

    public int getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(int opremaId) {
        this.opremaId = opremaId;
    }

    public int getVrstaOdrzavanjaId() {
        return vrstaOdrzavanjaId;
    }

    public void setVrstaOdrzavanjaId(int vrstaOdrzavanjaId) {
        this.vrstaOdrzavanjaId = vrstaOdrzavanjaId;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) opremaId;
        hash += (int) vrstaOdrzavanjaId;
        hash += (datum != null ? datum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanOdrzavanjaPK)) {
            return false;
        }
        PlanOdrzavanjaPK other = (PlanOdrzavanjaPK) object;
        if (this.opremaId != other.opremaId) {
            return false;
        }
        if (this.vrstaOdrzavanjaId != other.vrstaOdrzavanjaId) {
            return false;
        }
        if ((this.datum == null && other.datum != null) || (this.datum != null && !this.datum.equals(other.datum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PlanOdrzavanjaPK[ opremaId=" + opremaId + ", vrstaOdrzavanjaId=" + vrstaOdrzavanjaId + ", datum=" + datum + " ]";
    }
    
}
