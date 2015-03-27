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
@Table(name = "vrsta_odrzavanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VrstaOdrzavanja.findAll", query = "SELECT v FROM VrstaOdrzavanja v"),
    @NamedQuery(name = "VrstaOdrzavanja.findById", query = "SELECT v FROM VrstaOdrzavanja v WHERE v.id = :id"),
    @NamedQuery(name = "VrstaOdrzavanja.findByNaziv", query = "SELECT v FROM VrstaOdrzavanja v WHERE v.naziv = :naziv")})
public class VrstaOdrzavanja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer id;
    @Size(max = 45)
    private String naziv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaOdrzavanjaId")
    private Collection<Odrzavanje> odrzavanjeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaOdrzavanja")
    private Collection<PlanOdrzavanja> planOdrzavanjaCollection;

    public VrstaOdrzavanja() {
    }

    public VrstaOdrzavanja(Integer id) {
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
    public Collection<Odrzavanje> getOdrzavanjeCollection() {
        return odrzavanjeCollection;
    }

    public void setOdrzavanjeCollection(Collection<Odrzavanje> odrzavanjeCollection) {
        this.odrzavanjeCollection = odrzavanjeCollection;
    }

    @XmlTransient
    public Collection<PlanOdrzavanja> getPlanOdrzavanjaCollection() {
        return planOdrzavanjaCollection;
    }

    public void setPlanOdrzavanjaCollection(Collection<PlanOdrzavanja> planOdrzavanjaCollection) {
        this.planOdrzavanjaCollection = planOdrzavanjaCollection;
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
        if (!(object instanceof VrstaOdrzavanja)) {
            return false;
        }
        VrstaOdrzavanja other = (VrstaOdrzavanja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.VrstaOdrzavanja[ id=" + id + " ]";
    }
    
}
