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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agregacije.findAll", query = "SELECT a FROM Agregacije a"),
    @NamedQuery(name = "Agregacije.findById", query = "SELECT a FROM Agregacije a WHERE a.id = :id"),
    @NamedQuery(name = "Agregacije.findByOznaka", query = "SELECT a FROM Agregacije a WHERE a.oznaka = :oznaka")})
public class Agregacije implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String oznaka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agregacijeId")
    private Collection<Granice> graniceCollection;

    public Agregacije() {
    }

    public Agregacije(Integer id) {
        this.id = id;
    }

    public Agregacije(Integer id, String oznaka) {
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
    public Collection<Granice> getGraniceCollection() {
        return graniceCollection;
    }

    public void setGraniceCollection(Collection<Granice> graniceCollection) {
        this.graniceCollection = graniceCollection;
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
        if (!(object instanceof Agregacije)) {
            return false;
        }
        Agregacije other = (Agregacije) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Agregacije[ id=" + id + " ]";
    }
    
}
