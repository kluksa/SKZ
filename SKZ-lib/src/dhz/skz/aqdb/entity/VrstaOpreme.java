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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "vrsta_opreme")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VrstaOpreme.findAll", query = "SELECT v FROM VrstaOpreme v"),
    @NamedQuery(name = "VrstaOpreme.findById", query = "SELECT v FROM VrstaOpreme v WHERE v.id = :id"),
    @NamedQuery(name = "VrstaOpreme.findByOznaka", query = "SELECT v FROM VrstaOpreme v WHERE v.oznaka = :oznaka")})
public class VrstaOpreme implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    private String oznaka;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaOpremeId")
    private Collection<Uredjaj> uredjajCollection;

    public VrstaOpreme() {
    }

    public VrstaOpreme(Integer id) {
        this.id = id;
    }

    public VrstaOpreme(Integer id, String oznaka) {
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
    public Collection<Uredjaj> getUredjajCollection() {
        return uredjajCollection;
    }

    public void setUredjajCollection(Collection<Uredjaj> uredjajCollection) {
        this.uredjajCollection = uredjajCollection;
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
        if (!(object instanceof VrstaOpreme)) {
            return false;
        }
        VrstaOpreme other = (VrstaOpreme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.VrstaOpreme[ id=" + id + " ]";
    }
    
}
