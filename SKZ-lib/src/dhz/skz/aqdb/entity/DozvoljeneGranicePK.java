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
public class DozvoljeneGranicePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ispitne_velicine_id")
    private int ispitneVelicineId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "analiticke_metode_id")
    private int analitickeMetodeId;

    public DozvoljeneGranicePK() {
    }

    public DozvoljeneGranicePK(int ispitneVelicineId, int analitickeMetodeId) {
        this.ispitneVelicineId = ispitneVelicineId;
        this.analitickeMetodeId = analitickeMetodeId;
    }

    public int getIspitneVelicineId() {
        return ispitneVelicineId;
    }

    public void setIspitneVelicineId(int ispitneVelicineId) {
        this.ispitneVelicineId = ispitneVelicineId;
    }

    public int getAnalitickeMetodeId() {
        return analitickeMetodeId;
    }

    public void setAnalitickeMetodeId(int analitickeMetodeId) {
        this.analitickeMetodeId = analitickeMetodeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ispitneVelicineId;
        hash += (int) analitickeMetodeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DozvoljeneGranicePK)) {
            return false;
        }
        DozvoljeneGranicePK other = (DozvoljeneGranicePK) object;
        if (this.ispitneVelicineId != other.ispitneVelicineId) {
            return false;
        }
        if (this.analitickeMetodeId != other.analitickeMetodeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.DozvoljeneGranicePK[ ispitneVelicineId=" + ispitneVelicineId + ", analitickeMetodeId=" + analitickeMetodeId + " ]";
    }
    
}
