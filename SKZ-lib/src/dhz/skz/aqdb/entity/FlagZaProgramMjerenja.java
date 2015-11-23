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
@Table(name = "flag_za_program_mjerenja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlagZaProgramMjerenja.findAll", query = "SELECT f FROM FlagZaProgramMjerenja f"),
    @NamedQuery(name = "FlagZaProgramMjerenja.findById", query = "SELECT f FROM FlagZaProgramMjerenja f WHERE f.id = :id"),
    @NamedQuery(name = "FlagZaProgramMjerenja.findByOpis", query = "SELECT f FROM FlagZaProgramMjerenja f WHERE f.opis = :opis")})
public class FlagZaProgramMjerenja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer id;
    private String opis;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flagZaProgramMjerenja")
    private Collection<StanjeMjerenja> stanjeMjerenjaCollection;

    public FlagZaProgramMjerenja() {
    }

    public FlagZaProgramMjerenja(Integer id) {
        this.id = id;
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
    public Collection<StanjeMjerenja> getStanjeMjerenjaCollection() {
        return stanjeMjerenjaCollection;
    }

    public void setStanjeMjerenjaCollection(Collection<StanjeMjerenja> stanjeMjerenjaCollection) {
        this.stanjeMjerenjaCollection = stanjeMjerenjaCollection;
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
        if (!(object instanceof FlagZaProgramMjerenja)) {
            return false;
        }
        FlagZaProgramMjerenja other = (FlagZaProgramMjerenja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.FlagZaProgramMjerenja[ id=" + id + " ]";
    }
    
}
