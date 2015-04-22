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
@Table(name = "vrsta_umjeravanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VrstaUmjeravanja.findAll", query = "SELECT v FROM VrstaUmjeravanja v"),
    @NamedQuery(name = "VrstaUmjeravanja.findById", query = "SELECT v FROM VrstaUmjeravanja v WHERE v.id = :id"),
    @NamedQuery(name = "VrstaUmjeravanja.findByNaziv", query = "SELECT v FROM VrstaUmjeravanja v WHERE v.naziv = :naziv")})
public class VrstaUmjeravanja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    private String naziv;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaUmjeravanjaId")
    private Collection<Umjeravanje> umjeravanjeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vrstaUmjeravanja")
    private Collection<PlanUmjeravanja> planUmjeravanjaCollection;

    public VrstaUmjeravanja() {
    }

    public VrstaUmjeravanja(Integer id) {
        this.id = id;
    }

    public VrstaUmjeravanja(Integer id, String naziv) {
        this.id = id;
        this.naziv = naziv;
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
    public Collection<Umjeravanje> getUmjeravanjeCollection() {
        return umjeravanjeCollection;
    }

    public void setUmjeravanjeCollection(Collection<Umjeravanje> umjeravanjeCollection) {
        this.umjeravanjeCollection = umjeravanjeCollection;
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
        if (!(object instanceof VrstaUmjeravanja)) {
            return false;
        }
        VrstaUmjeravanja other = (VrstaUmjeravanja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.VrstaUmjeravanja[ id=" + id + " ]";
    }
    
}
