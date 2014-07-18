/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "zemljopisne_karakteristike_postaja_link", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZemljopisneKarakteristikePostajaLink.findAll", query = "SELECT z FROM ZemljopisneKarakteristikePostajaLink z"),
    @NamedQuery(name = "ZemljopisneKarakteristikePostajaLink.findByZemljopisneKarakteristikeId", query = "SELECT z FROM ZemljopisneKarakteristikePostajaLink z WHERE z.zemljopisneKarakteristikePostajaLinkPK.zemljopisneKarakteristikeId = :zemljopisneKarakteristikeId"),
    @NamedQuery(name = "ZemljopisneKarakteristikePostajaLink.findByPostajaId", query = "SELECT z FROM ZemljopisneKarakteristikePostajaLink z WHERE z.zemljopisneKarakteristikePostajaLinkPK.postajaId = :postajaId")})
public class ZemljopisneKarakteristikePostajaLink implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZemljopisneKarakteristikePostajaLinkPK zemljopisneKarakteristikePostajaLinkPK;
    @JoinColumn(name = "zemljopisne_karakteristike_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ZemljopisneKarakteristike zemljopisneKarakteristike;

    public ZemljopisneKarakteristikePostajaLink() {
    }

    public ZemljopisneKarakteristikePostajaLink(ZemljopisneKarakteristikePostajaLinkPK zemljopisneKarakteristikePostajaLinkPK) {
        this.zemljopisneKarakteristikePostajaLinkPK = zemljopisneKarakteristikePostajaLinkPK;
    }

    public ZemljopisneKarakteristikePostajaLink(int zemljopisneKarakteristikeId, int postajaId) {
        this.zemljopisneKarakteristikePostajaLinkPK = new ZemljopisneKarakteristikePostajaLinkPK(zemljopisneKarakteristikeId, postajaId);
    }

    public ZemljopisneKarakteristikePostajaLinkPK getZemljopisneKarakteristikePostajaLinkPK() {
        return zemljopisneKarakteristikePostajaLinkPK;
    }

    public void setZemljopisneKarakteristikePostajaLinkPK(ZemljopisneKarakteristikePostajaLinkPK zemljopisneKarakteristikePostajaLinkPK) {
        this.zemljopisneKarakteristikePostajaLinkPK = zemljopisneKarakteristikePostajaLinkPK;
    }

    public ZemljopisneKarakteristike getZemljopisneKarakteristike() {
        return zemljopisneKarakteristike;
    }

    public void setZemljopisneKarakteristike(ZemljopisneKarakteristike zemljopisneKarakteristike) {
        this.zemljopisneKarakteristike = zemljopisneKarakteristike;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zemljopisneKarakteristikePostajaLinkPK != null ? zemljopisneKarakteristikePostajaLinkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZemljopisneKarakteristikePostajaLink)) {
            return false;
        }
        ZemljopisneKarakteristikePostajaLink other = (ZemljopisneKarakteristikePostajaLink) object;
        if ((this.zemljopisneKarakteristikePostajaLinkPK == null && other.zemljopisneKarakteristikePostajaLinkPK != null) || (this.zemljopisneKarakteristikePostajaLinkPK != null && !this.zemljopisneKarakteristikePostajaLinkPK.equals(other.zemljopisneKarakteristikePostajaLinkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ZemljopisneKarakteristikePostajaLink[ zemljopisneKarakteristikePostajaLinkPK=" + zemljopisneKarakteristikePostajaLinkPK + " ]";
    }
    
}
