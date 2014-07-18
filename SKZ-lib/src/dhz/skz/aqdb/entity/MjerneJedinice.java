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
@Table(name = "mjerne_jedinice", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MjerneJedinice.findAll", query = "SELECT m FROM MjerneJedinice m"),
    @NamedQuery(name = "MjerneJedinice.findById", query = "SELECT m FROM MjerneJedinice m WHERE m.id = :id"),
    @NamedQuery(name = "MjerneJedinice.findByOznaka", query = "SELECT m FROM MjerneJedinice m WHERE m.oznaka = :oznaka")})
public class MjerneJedinice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "oznaka")
    private String oznaka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mjerneJediniceId")
    private Collection<Komponenta> komponentaCollection;

    public MjerneJedinice() {
    }

    public MjerneJedinice(Integer id) {
        this.id = id;
    }

    public MjerneJedinice(Integer id, String oznaka) {
        this.id = id;
        this.oznaka = oznaka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    @XmlTransient
    public Collection<Komponenta> getKomponentaCollection() {
        return komponentaCollection;
    }

    public void setKomponentaCollection(Collection<Komponenta> komponentaCollection) {
        this.komponentaCollection = komponentaCollection;
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
        if (!(object instanceof MjerneJedinice)) {
            return false;
        }
        MjerneJedinice other = (MjerneJedinice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.MjerneJedinice[ id=" + id + " ]";
    }
    
}
