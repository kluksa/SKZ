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

/**
 *
 * @author kraljevic
 */
@Embeddable
public class StanjeMjerenjaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "program_mjerenja_id")
    private int programMjerenjaId;
    @Basic(optional = false)
    @Column(name = "pocetak_primjene")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetakPrimjene;
    @Basic(optional = false)
    private int status;

    public StanjeMjerenjaPK() {
    }

    public StanjeMjerenjaPK(int programMjerenjaId, Date pocetakPrimjene, int status) {
        this.programMjerenjaId = programMjerenjaId;
        this.pocetakPrimjene = pocetakPrimjene;
        this.status = status;
    }

    public int getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(int programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Date getPocetakPrimjene() {
        return pocetakPrimjene;
    }

    public void setPocetakPrimjene(Date pocetakPrimjene) {
        this.pocetakPrimjene = pocetakPrimjene;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) programMjerenjaId;
        hash += (pocetakPrimjene != null ? pocetakPrimjene.hashCode() : 0);
        hash += (int) status;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StanjeMjerenjaPK)) {
            return false;
        }
        StanjeMjerenjaPK other = (StanjeMjerenjaPK) object;
        if (this.programMjerenjaId != other.programMjerenjaId) {
            return false;
        }
        if ((this.pocetakPrimjene == null && other.pocetakPrimjene != null) || (this.pocetakPrimjene != null && !this.pocetakPrimjene.equals(other.pocetakPrimjene))) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.StanjeMjerenjaPK[ programMjerenjaId=" + programMjerenjaId + ", pocetakPrimjene=" + pocetakPrimjene + ", status=" + status + " ]";
    }
    
}
