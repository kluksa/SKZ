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
public class UmjerneTockePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "komponenta_id")
    private int komponentaId;

    public UmjerneTockePK() {
    }

    public UmjerneTockePK(int id, int komponentaId) {
        this.id = id;
        this.komponentaId = komponentaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        hash += (int) id;
        hash += (int) komponentaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjerneTockePK)) {
            return false;
        }
        UmjerneTockePK other = (UmjerneTockePK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.komponentaId != other.komponentaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjerneTockePK[ id=" + id + ", komponentaId=" + komponentaId + " ]";
    }
    
}
