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
public class ZemljopisneKarakteristikePostajaLinkPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "zemljopisne_karakteristike_id")
    private int zemljopisneKarakteristikeId;
    @Basic(optional = false)
    @Column(name = "postaja_id")
    private int postajaId;

    public ZemljopisneKarakteristikePostajaLinkPK() {
    }

    public ZemljopisneKarakteristikePostajaLinkPK(int zemljopisneKarakteristikeId, int postajaId) {
        this.zemljopisneKarakteristikeId = zemljopisneKarakteristikeId;
        this.postajaId = postajaId;
    }

    public int getZemljopisneKarakteristikeId() {
        return zemljopisneKarakteristikeId;
    }

    public void setZemljopisneKarakteristikeId(int zemljopisneKarakteristikeId) {
        this.zemljopisneKarakteristikeId = zemljopisneKarakteristikeId;
    }

    public int getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(int postajaId) {
        this.postajaId = postajaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) zemljopisneKarakteristikeId;
        hash += (int) postajaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZemljopisneKarakteristikePostajaLinkPK)) {
            return false;
        }
        ZemljopisneKarakteristikePostajaLinkPK other = (ZemljopisneKarakteristikePostajaLinkPK) object;
        if (this.zemljopisneKarakteristikeId != other.zemljopisneKarakteristikeId) {
            return false;
        }
        if (this.postajaId != other.postajaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ZemljopisneKarakteristikePostajaLinkPK[ zemljopisneKarakteristikeId=" + zemljopisneKarakteristikeId + ", postajaId=" + postajaId + " ]";
    }
    
}
