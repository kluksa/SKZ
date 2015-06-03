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
public class ValidatorModelIzvorPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "model_uredjaja_id")
    private int modelUredjajaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "izvor_podataka_id")
    private int izvorPodatakaId;

    public ValidatorModelIzvorPK() {
    }

    public ValidatorModelIzvorPK(int modelUredjajaId, int izvorPodatakaId) {
        this.modelUredjajaId = modelUredjajaId;
        this.izvorPodatakaId = izvorPodatakaId;
    }

    public int getModelUredjajaId() {
        return modelUredjajaId;
    }

    public void setModelUredjajaId(int modelUredjajaId) {
        this.modelUredjajaId = modelUredjajaId;
    }

    public int getIzvorPodatakaId() {
        return izvorPodatakaId;
    }

    public void setIzvorPodatakaId(int izvorPodatakaId) {
        this.izvorPodatakaId = izvorPodatakaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) modelUredjajaId;
        hash += (int) izvorPodatakaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValidatorModelIzvorPK)) {
            return false;
        }
        ValidatorModelIzvorPK other = (ValidatorModelIzvorPK) object;
        if (this.modelUredjajaId != other.modelUredjajaId) {
            return false;
        }
        if (this.izvorPodatakaId != other.izvorPodatakaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ValidatorModelIzvorPK[ modelUredjajaId=" + modelUredjajaId + ", izvorPodatakaId=" + izvorPodatakaId + " ]";
    }
    
}
