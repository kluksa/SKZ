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
@Table(name = "ispitne_velicine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IspitneVelicine.findAll", query = "SELECT i FROM IspitneVelicine i"),
    @NamedQuery(name = "IspitneVelicine.findById", query = "SELECT i FROM IspitneVelicine i WHERE i.id = :id"),
    @NamedQuery(name = "IspitneVelicine.findByOznaka", query = "SELECT i FROM IspitneVelicine i WHERE i.oznaka = :oznaka"),
    @NamedQuery(name = "IspitneVelicine.findByNaziv", query = "SELECT i FROM IspitneVelicine i WHERE i.naziv = :naziv")})
public class IspitneVelicine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    private String oznaka;
    @Size(max = 90)
    private String naziv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ispitneVelicine")
    private Collection<DozvoljeneGranice> dozvoljeneGraniceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ispitneVelicine")
    private Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ispitneVelicine")
    private Collection<PlanUmjeravanja> planUmjeravanjaCollection;
    
    public IspitneVelicine() {
    }

    public IspitneVelicine(Integer id) {
        this.id = id;
    }

    public IspitneVelicine(Integer id, String oznaka) {
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

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public Collection<DozvoljeneGranice> getDozvoljeneGraniceCollection() {
        return dozvoljeneGraniceCollection;
    }

    public void setDozvoljeneGraniceCollection(Collection<DozvoljeneGranice> dozvoljeneGraniceCollection) {
        this.dozvoljeneGraniceCollection = dozvoljeneGraniceCollection;
    }

    @XmlTransient
    public Collection<UmjeravanjeHasIspitneVelicine> getUmjeravanjeHasIspitneVelicineCollection() {
        return umjeravanjeHasIspitneVelicineCollection;
    }

    public void setUmjeravanjeHasIspitneVelicineCollection(Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection) {
        this.umjeravanjeHasIspitneVelicineCollection = umjeravanjeHasIspitneVelicineCollection;
    }

    @XmlTransient
    public Collection<PlanUmjeravanja> getPlanUmjeravanjaCollection() {
        return planUmjeravanjaCollection;
    }

    public void setPlanUmjeravanjaCollection(Collection<PlanUmjeravanja> planUmjeravanjaCollection) {
        this.planUmjeravanjaCollection = planUmjeravanjaCollection;
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
        if (!(object instanceof IspitneVelicine)) {
            return false;
        }
        IspitneVelicine other = (IspitneVelicine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.IspitneVelicine[ id=" + id + " ]";
    }
    
}
