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
@Table(name = "vrsta_mreze")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VrstaMreze.findAll", query = "SELECT v FROM VrstaMreze v"),
    @NamedQuery(name = "VrstaMreze.findById", query = "SELECT v FROM VrstaMreze v WHERE v.id = :id"),
    @NamedQuery(name = "VrstaMreze.findByTerenskaOznaka", query = "SELECT v FROM VrstaMreze v WHERE v.terenskaOznaka = :terenskaOznaka"),
    @NamedQuery(name = "VrstaMreze.findByOpisRazine", query = "SELECT v FROM VrstaMreze v WHERE v.opisRazine = :opisRazine")})
public class VrstaMreze implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "terenska_oznaka")
    private String terenskaOznaka;
    @Size(max = 255)
    @Column(name = "opis_razine")
    private String opisRazine;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaId")
    private Collection<Mreza> mrezaCollection;

    public VrstaMreze() {
    }

    public VrstaMreze(Integer id) {
        this.id = id;
    }

    public VrstaMreze(Integer id, String terenskaOznaka) {
        this.id = id;
        this.terenskaOznaka = terenskaOznaka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTerenskaOznaka() {
        return terenskaOznaka;
    }

    public void setTerenskaOznaka(String terenskaOznaka) {
        this.terenskaOznaka = terenskaOznaka;
    }

    public String getOpisRazine() {
        return opisRazine;
    }

    public void setOpisRazine(String opisRazine) {
        this.opisRazine = opisRazine;
    }

    @XmlTransient
    public Collection<Mreza> getMrezaCollection() {
        return mrezaCollection;
    }

    public void setMrezaCollection(Collection<Mreza> mrezaCollection) {
        this.mrezaCollection = mrezaCollection;
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
        if (!(object instanceof VrstaMreze)) {
            return false;
        }
        VrstaMreze other = (VrstaMreze) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.VrstaMreze[ id=" + id + " ]";
    }
    
}
