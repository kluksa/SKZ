/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "relevantne_direktive")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelevantneDirektive.findAll", query = "SELECT r FROM RelevantneDirektive r"),
    @NamedQuery(name = "RelevantneDirektive.findById", query = "SELECT r FROM RelevantneDirektive r WHERE r.id = :id"),
    @NamedQuery(name = "RelevantneDirektive.findByNaziv", query = "SELECT r FROM RelevantneDirektive r WHERE r.naziv = :naziv")})
public class RelevantneDirektive implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Size(max = 90)
    private String naziv;
    @JoinTable(name = "komponenta_has_relevantne_direktive", joinColumns = {
        @JoinColumn(name = "relevantne_direktive_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "komponenta_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Komponenta> komponentaCollection;

    public RelevantneDirektive() {
    }

    public RelevantneDirektive(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
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
        if (!(object instanceof RelevantneDirektive)) {
            return false;
        }
        RelevantneDirektive other = (RelevantneDirektive) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.RelevantneDirektive[ id=" + id + " ]";
    }
    
}
