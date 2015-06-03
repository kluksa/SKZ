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
public class DijeloviPK implements Serializable {
    @Basic(optional = false)
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "model_uredjaja_id")
    private int modelUredjajaId;

    public DijeloviPK() {
    }

    public DijeloviPK(int id, int modelUredjajaId) {
        this.id = id;
        this.modelUredjajaId = modelUredjajaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelUredjajaId() {
        return modelUredjajaId;
    }

    public void setModelUredjajaId(int modelUredjajaId) {
        this.modelUredjajaId = modelUredjajaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) modelUredjajaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DijeloviPK)) {
            return false;
        }
        DijeloviPK other = (DijeloviPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.modelUredjajaId != other.modelUredjajaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.DijeloviPK[ id=" + id + ", modelUredjajaId=" + modelUredjajaId + " ]";
    }
    
}
