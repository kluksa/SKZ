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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    @NamedQuery(name = "Umjeravanje.findAll", query = "SELECT u FROM Umjeravanje u"),
    @NamedQuery(name = "Umjeravanje.findById", query = "SELECT u FROM Umjeravanje u WHERE u.id = :id"),
    @NamedQuery(name = "Umjeravanje.findByDatum", query = "SELECT u FROM Umjeravanje u WHERE u.datum = :datum"),
    @NamedQuery(name = "Umjeravanje.findByOznakaUmjernice", query = "SELECT u FROM Umjeravanje u WHERE u.oznakaUmjernice = :oznakaUmjernice")})
public class Umjeravanje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIMESTAMP WITH TIME ZONE")
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "oznaka_umjernice")
    private String oznakaUmjernice;
    @JoinTable(name = "umjeravanje_etalon_link", joinColumns = {
        @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "oprema_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Uredjaj> etalonCollection;
    @JoinColumn(name = "analiticke_metode_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AnalitickeMetode analitickeMetodeId;
    @JoinColumn(name = "mjeritelj_id", referencedColumnName = "id")
    @ManyToOne
    private Korisnik mjeriteljId;
    @JoinColumn(name = "umjerni_laboratorij_id", referencedColumnName = "id")
    @ManyToOne
    private UmjerniLaboratorij umjerniLaboratorijId;
    @JoinColumn(name = "uredjaj_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uredjaj uredjajId;
    @JoinColumn(name = "vrsta_umjeravanja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VrstaUmjeravanja vrstaUmjeravanjaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "umjeravanje")
    private Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "umjeravanje")
    private UmjeravanjeKomentar umjeravanjeKomentar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "umjeravanjeId")
    private Collection<PlanUmjeravanja> planUmjeravanjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "umjeravanjeId")
    private Collection<UmjerneTocke> umjerneTockeCollection;

    public Umjeravanje() {
    }

    public Umjeravanje(Integer id) {
        this.id = id;
    }

    public Umjeravanje(Integer id, Date datum, String oznakaUmjernice) {
        this.id = id;
        this.datum = datum;
        this.oznakaUmjernice = oznakaUmjernice;
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

    public String getOznakaUmjernice() {
        return oznakaUmjernice;
    }

    public void setOznakaUmjernice(String oznakaUmjernice) {
        this.oznakaUmjernice = oznakaUmjernice;
    }

    @XmlTransient
    public Collection<Uredjaj> getUredjajCollection() {
        return etalonCollection;
    }

    public void setUredjajCollection(Collection<Uredjaj> uredjajCollection) {
        this.etalonCollection = uredjajCollection;
    }

    public AnalitickeMetode getAnalitickeMetodeId() {
        return analitickeMetodeId;
    }

    public void setAnalitickeMetodeId(AnalitickeMetode analitickeMetodeId) {
        this.analitickeMetodeId = analitickeMetodeId;
    }

    public Korisnik getMjeriteljId() {
        return mjeriteljId;
    }

    public void setMjeriteljId(Korisnik mjeriteljId) {
        this.mjeriteljId = mjeriteljId;
    }

    public UmjerniLaboratorij getUmjerniLaboratorijId() {
        return umjerniLaboratorijId;
    }

    public void setUmjerniLaboratorijId(UmjerniLaboratorij umjerniLaboratorijId) {
        this.umjerniLaboratorijId = umjerniLaboratorijId;
    }

    public Uredjaj getUredjajId() {
        return uredjajId;
    }

    public void setUredjajId(Uredjaj uredjajId) {
        this.uredjajId = uredjajId;
    }

    public VrstaUmjeravanja getVrstaUmjeravanjaId() {
        return vrstaUmjeravanjaId;
    }

    public void setVrstaUmjeravanjaId(VrstaUmjeravanja vrstaUmjeravanjaId) {
        this.vrstaUmjeravanjaId = vrstaUmjeravanjaId;
    }

    @XmlTransient
    public Collection<UmjeravanjeHasIspitneVelicine> getUmjeravanjeHasIspitneVelicineCollection() {
        return umjeravanjeHasIspitneVelicineCollection;
    }

    public void setUmjeravanjeHasIspitneVelicineCollection(Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection) {
        this.umjeravanjeHasIspitneVelicineCollection = umjeravanjeHasIspitneVelicineCollection;
    }

    public UmjeravanjeKomentar getUmjeravanjeKomentar() {
        return umjeravanjeKomentar;
    }

    public void setUmjeravanjeKomentar(UmjeravanjeKomentar umjeravanjeKomentar) {
        this.umjeravanjeKomentar = umjeravanjeKomentar;
    }

    @XmlTransient
    public Collection<PlanUmjeravanja> getPlanUmjeravanjaCollection() {
        return planUmjeravanjaCollection;
    }

    public void setPlanUmjeravanjaCollection(Collection<PlanUmjeravanja> planUmjeravanjaCollection) {
        this.planUmjeravanjaCollection = planUmjeravanjaCollection;
    }

    @XmlTransient
    public Collection<UmjerneTocke> getUmjerneTockeCollection() {
        return umjerneTockeCollection;
    }

    public void setUmjerneTockeCollection(Collection<UmjerneTocke> umjerneTockeCollection) {
        this.umjerneTockeCollection = umjerneTockeCollection;
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
        if (!(object instanceof Umjeravanje)) {
            return false;
        }
        Umjeravanje other = (Umjeravanje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Umjeravanje[ id=" + id + " ]";
    }
    
}
