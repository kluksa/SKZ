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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "nivo_validacije", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivoValidacije.findAll", query = "SELECT n FROM NivoValidacije n"),
    @NamedQuery(name = "NivoValidacije.findById", query = "SELECT n FROM NivoValidacije n WHERE n.id = :id"),
    @NamedQuery(name = "NivoValidacije.findByOznaka", query = "SELECT n FROM NivoValidacije n WHERE n.oznaka = :oznaka")})
public class NivoValidacije implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Short id;
    @Size(max = 45)
    @Column(length = 45)
    private String oznaka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivoValidacijeId")
    private Collection<Podatak> podatakCollection;

    public NivoValidacije() {
    }

    public NivoValidacije(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
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
        if (!(object instanceof NivoValidacije)) {
            return false;
        }
        NivoValidacije other = (NivoValidacije) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.NivoValidacije[ id=" + id + " ]";
    }
    
}
