/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "zemljopisne_karakteristike", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZemljopisneKarakteristike.findAll", query = "SELECT z FROM ZemljopisneKarakteristike z"),
    @NamedQuery(name = "ZemljopisneKarakteristike.findById", query = "SELECT z FROM ZemljopisneKarakteristike z WHERE z.id = :id"),
    @NamedQuery(name = "ZemljopisneKarakteristike.findByOpis", query = "SELECT z FROM ZemljopisneKarakteristike z WHERE z.opis = :opis")})
public class ZemljopisneKarakteristike implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "opis")
    private String opis;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zemljopisneKarakteristike")
    private Collection<ZemljopisneKarakteristikePostajaLink> zemljopisneKarakteristikePostajaLinkCollection;

    public ZemljopisneKarakteristike() {
    }

    public ZemljopisneKarakteristike(Integer id) {
        this.id = id;
    }

    public ZemljopisneKarakteristike(Integer id, String opis) {
        this.id = id;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @XmlTransient
    public Collection<ZemljopisneKarakteristikePostajaLink> getZemljopisneKarakteristikePostajaLinkCollection() {
        return zemljopisneKarakteristikePostajaLinkCollection;
    }

    public void setZemljopisneKarakteristikePostajaLinkCollection(Collection<ZemljopisneKarakteristikePostajaLink> zemljopisneKarakteristikePostajaLinkCollection) {
        this.zemljopisneKarakteristikePostajaLinkCollection = zemljopisneKarakteristikePostajaLinkCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZemljopisneKarakteristike)) {
            return false;
        }
        ZemljopisneKarakteristike other = (ZemljopisneKarakteristike) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ZemljopisneKarakteristike[ id=" + id + " ]";
    }

}
