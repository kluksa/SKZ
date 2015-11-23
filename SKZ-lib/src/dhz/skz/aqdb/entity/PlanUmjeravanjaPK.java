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
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @Column(name = "ispitna_velicina_id")
    private int ispitnaVelicinaId;

    public PlanUmjeravanjaPK() {
    }

    public PlanUmjeravanjaPK(int opremaId, Date datum, int ispitnaVelicinaId) {
        this.opremaId = opremaId;
        this.datum = datum;
        this.ispitnaVelicinaId = ispitnaVelicinaId;
    }

    public int getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(int opremaId) {
        this.opremaId = opremaId;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public int getIspitnaVelicinaId() {
        return ispitnaVelicinaId;
    }

    public void setIspitnaVelicinaId(int ispitnaVelicinaId) {
        this.ispitnaVelicinaId = ispitnaVelicinaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) opremaId;
        hash += (datum != null ? datum.hashCode() : 0);
        hash += (int) ispitnaVelicinaId;
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
        if ((this.datum == null && other.datum != null) || (this.datum != null && !this.datum.equals(other.datum))) {
            return false;
        }
        if (this.ispitnaVelicinaId != other.ispitnaVelicinaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PlanUmjeravanjaPK[ opremaId=" + opremaId + ", datum=" + datum + ", ispitnaVelicinaId=" + ispitnaVelicinaId + " ]";
    }
    
}
