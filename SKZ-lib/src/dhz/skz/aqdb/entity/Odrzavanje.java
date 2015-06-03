/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Odrzavanje.findAll", query = "SELECT o FROM Odrzavanje o"),
    @NamedQuery(name = "Odrzavanje.findById", query = "SELECT o FROM Odrzavanje o WHERE o.id = :id"),
    @NamedQuery(name = "Odrzavanje.findByDatum", query = "SELECT o FROM Odrzavanje o WHERE o.datum = :datum"),
    @NamedQuery(name = "Odrzavanje.findByOpis", query = "SELECT o FROM Odrzavanje o WHERE o.opis = :opis")})
public class Odrzavanje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    private String opis;
    @ManyToMany(mappedBy = "odrzavanjeCollection")
    private Collection<Dijelovi> dijeloviCollection;
    @OneToMany(mappedBy = "odrzavanjeId")
    private Collection<Kvarovi> kvaroviCollection;
    @JoinColumn(name = "vrsta_odrzavanja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VrstaOdrzavanja vrstaOdrzavanjaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "odrzavanjeId")
    private Collection<PlanOdrzavanja> planOdrzavanjaCollection;

    public Odrzavanje() {
    }

    public Odrzavanje(Integer id) {
        this.id = id;
    }

    public Odrzavanje(Integer id, Date datum, String opis) {
        this.id = id;
        this.datum = datum;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @XmlTransient
    public Collection<Dijelovi> getDijeloviCollection() {
        return dijeloviCollection;
    }

    public void setDijeloviCollection(Collection<Dijelovi> dijeloviCollection) {
        this.dijeloviCollection = dijeloviCollection;
    }

    @XmlTransient
    public Collection<Kvarovi> getKvaroviCollection() {
        return kvaroviCollection;
    }

    public void setKvaroviCollection(Collection<Kvarovi> kvaroviCollection) {
        this.kvaroviCollection = kvaroviCollection;
    }

    public VrstaOdrzavanja getVrstaOdrzavanjaId() {
        return vrstaOdrzavanjaId;
    }

    public void setVrstaOdrzavanjaId(VrstaOdrzavanja vrstaOdrzavanjaId) {
        this.vrstaOdrzavanjaId = vrstaOdrzavanjaId;
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
        if (!(object instanceof Odrzavanje)) {
            return false;
        }
        Odrzavanje other = (Odrzavanje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Odrzavanje[ id=" + id + " ]";
    }
    
}
