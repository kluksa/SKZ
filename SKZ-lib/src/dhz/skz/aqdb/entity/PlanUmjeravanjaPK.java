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
public class PlanUmjeravanjaPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "oprema_id")
    private int opremaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vrsta_id")
    private int vrstaId;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date datum;

    public PlanUmjeravanjaPK() {
    }

    public PlanUmjeravanjaPK(int opremaId, int vrstaId, Date datum) {
        this.opremaId = opremaId;
        this.vrstaId = vrstaId;
        this.datum = datum;
    }

    public int getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(int opremaId) {
        this.opremaId = opremaId;
    }

    public int getVrstaId() {
        return vrstaId;
    }

    public void setVrstaId(int vrstaId) {
        this.vrstaId = vrstaId;
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
        hash += (int) vrstaId;
        hash += (datum != null ? datum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanUmjeravanjaPK)) {
            return false;
        }
        PlanUmjeravanjaPK other = (PlanUmjeravanjaPK) object;
        if (this.opremaId != other.opremaId) {
            return false;
        }
        if (this.vrstaId != other.vrstaId) {
            return false;
        }
        if ((this.datum == null && other.datum != null) || (this.datum != null && !this.datum.equals(other.datum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PlanUmjeravanjaPK[ opremaId=" + opremaId + ", vrstaId=" + vrstaId + ", datum=" + datum + " ]";
    }
    
}
