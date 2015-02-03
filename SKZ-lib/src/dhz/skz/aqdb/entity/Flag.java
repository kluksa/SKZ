/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Flag.findAll", query = "SELECT f FROM Flag f"),
    @NamedQuery(name = "Flag.findById", query = "SELECT f FROM Flag f WHERE f.id = :id"),
    @NamedQuery(name = "Flag.findByOpis", query = "SELECT f FROM Flag f WHERE f.opis = :opis")})
public class Flag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Size(max = 45)
    @Column(length = 45)
    private String opis;
    @JoinTable(name = "podatak_has_flag", joinColumns = {
        @JoinColumn(name = "flag_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "podatak_id", referencedColumnName = "podatak_id", nullable = false)})
    @ManyToMany
    private Collection<Podatak> podatakCollection;

    public Flag() {
    }

    public Flag(Integer id) {
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
    public Collection<Podatak> getPodatakCollection() {
        return podatakCollection;
    }

    public void setPodatakCollection(Collection<Podatak> podatakCollection) {
        this.podatakCollection = podatakCollection;
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
        if (!(object instanceof Flag)) {
            return false;
        }
        Flag other = (Flag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Flag[ id=" + id + " ]";
    }
    
}
