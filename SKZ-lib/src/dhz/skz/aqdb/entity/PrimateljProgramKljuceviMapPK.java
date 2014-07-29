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

/**
 *
 * @author kraljevic
 */
@Embeddable
public class PrimateljProgramKljuceviMapPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "program_mjerenja_id")
    private int programMjerenjaId;
    @Basic(optional = false)
    @Column(name = "primatelj_id")
    private int primateljId;

    public PrimateljProgramKljuceviMapPK() {
    }

    public PrimateljProgramKljuceviMapPK(int programMjerenjaId, int primateljId) {
        this.programMjerenjaId = programMjerenjaId;
        this.primateljId = primateljId;
    }

    public int getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(int programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public int getPrimateljId() {
        return primateljId;
    }

    public void setPrimateljId(int primateljId) {
        this.primateljId = primateljId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) programMjerenjaId;
        hash += (int) primateljId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrimateljProgramKljuceviMapPK)) {
            return false;
        }
        PrimateljProgramKljuceviMapPK other = (PrimateljProgramKljuceviMapPK) object;
        if (this.programMjerenjaId != other.programMjerenjaId) {
            return false;
        }
        if (this.primateljId != other.primateljId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PrimateljProgramKljuceviMapPK[ programMjerenjaId=" + programMjerenjaId + ", primateljId=" + primateljId + " ]";
    }

}
