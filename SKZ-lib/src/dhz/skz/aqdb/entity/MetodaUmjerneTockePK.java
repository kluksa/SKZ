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
public class MetodaUmjerneTockePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "metoda_id")
    private int metodaId;
    @Basic(optional = false)
    @Column(name = "tocka_num")
    private int tockaNum;

    public MetodaUmjerneTockePK() {
    }

    public MetodaUmjerneTockePK(int metodaId, int tockaNum) {
        this.metodaId = metodaId;
        this.tockaNum = tockaNum;
    }

    public int getMetodaId() {
        return metodaId;
    }

    public void setMetodaId(int metodaId) {
        this.metodaId = metodaId;
    }

    public int getTockaNum() {
        return tockaNum;
    }

    public void setTockaNum(int tockaNum) {
        this.tockaNum = tockaNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) metodaId;
        hash += (int) tockaNum;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetodaUmjerneTockePK)) {
            return false;
        }
        MetodaUmjerneTockePK other = (MetodaUmjerneTockePK) object;
        if (this.metodaId != other.metodaId) {
            return false;
        }
        if (this.tockaNum != other.tockaNum) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.MetodaUmjerneTockePK[ metodaId=" + metodaId + ", tockaNum=" + tockaNum + " ]";
    }
    
}
