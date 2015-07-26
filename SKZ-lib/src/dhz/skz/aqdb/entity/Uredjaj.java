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
    @NamedQuery(name = "Uredjaj.findAll", query = "SELECT u FROM Uredjaj u"),
    @NamedQuery(name = "Uredjaj.findById", query = "SELECT u FROM Uredjaj u WHERE u.id = :id"),
    @NamedQuery(name = "Uredjaj.findBySerijskaOznaka", query = "SELECT u FROM Uredjaj u WHERE u.serijskaOznaka = :serijskaOznaka"),
    @NamedQuery(name = "Uredjaj.findByGodinaProizvodnje", query = "SELECT u FROM Uredjaj u WHERE u.godinaProizvodnje = :godinaProizvodnje"),
    @NamedQuery(name = "Uredjaj.findByDatumIsporuke", query = "SELECT u FROM Uredjaj u WHERE u.datumIsporuke = :datumIsporuke"),
    @NamedQuery(name = "Uredjaj.findByDatumOtpisa", query = "SELECT u FROM Uredjaj u WHERE u.datumOtpisa = :datumOtpisa")})
public class Uredjaj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "serijska_oznaka")
    private String serijskaOznaka;
    @Column(name = "godina_proizvodnje")
    private Integer godinaProizvodnje;
    @Column(name = "datum_isporuke")
    @Temporal(TemporalType.DATE)
    private Date datumIsporuke;
    @Column(name = "datum_otpisa")
    @Temporal(TemporalType.DATE)
    private Date datumOtpisa;
    @OneToMany(mappedBy = "uredjajId")
    private Collection<Umjeravanje> umjeravanjeCollection;
    @OneToMany(mappedBy = "uredjajId")
    private Collection<ProgramUredjajLink> programUredjajLinkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjaj")
    private Collection<EtalonBoca> etalonBocaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjajId")
    private Collection<Kvarovi> kvaroviCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjajId")
    private Collection<Umjeravanje> umjeravanjeCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjaj")
    private Collection<EtalonDilucijska> etalonDilucijskaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjajId")
    private Collection<PostajaUredjajLink> postajaUredjajLinkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjaj")
    private Collection<EtalonCistiZrakKvaliteta> etalonCistiZrakKvalitetaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjaj")
    private Collection<PlanUmjeravanja> planUmjeravanjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uredjaj")
    private Collection<PlanOdrzavanja> planOdrzavanjaCollection;
    @JoinColumn(name = "model_uredjaja_id", referencedColumnName = "id")
    @ManyToOne
    private ModelUredjaja modelUredjajaId;
    @JoinColumn(name = "vrsta_opreme_id", referencedColumnName = "id")
    @ManyToOne
    private VrstaOpreme vrstaOpremeId;

    public Uredjaj() {
    }

    public Uredjaj(Integer id) {
        this.id = id;
    }

    public Uredjaj(Integer id, String serijskaOznaka) {
        this.id = id;
        this.serijskaOznaka = serijskaOznaka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerijskaOznaka() {
        return serijskaOznaka;
    }

    public void setSerijskaOznaka(String serijskaOznaka) {
        this.serijskaOznaka = serijskaOznaka;
    }

    public Integer getGodinaProizvodnje() {
        return godinaProizvodnje;
    }

    public void setGodinaProizvodnje(Integer godinaProizvodnje) {
        this.godinaProizvodnje = godinaProizvodnje;
    }

    public Date getDatumIsporuke() {
        return datumIsporuke;
    }

    public void setDatumIsporuke(Date datumIsporuke) {
        this.datumIsporuke = datumIsporuke;
    }

    public Date getDatumOtpisa() {
        return datumOtpisa;
    }

    public void setDatumOtpisa(Date datumOtpisa) {
        this.datumOtpisa = datumOtpisa;
    }

    @XmlTransient
    public Collection<Umjeravanje> getUmjeravanjeCollection() {
        return umjeravanjeCollection;
    }

    public void setUmjeravanjeCollection(Collection<Umjeravanje> umjeravanjeCollection) {
        this.umjeravanjeCollection = umjeravanjeCollection;
    }

    @XmlTransient
    public Collection<ProgramUredjajLink> getProgramUredjajLinkCollection() {
        return programUredjajLinkCollection;
    }

    public void setProgramUredjajLinkCollection(Collection<ProgramUredjajLink> programUredjajLinkCollection) {
        this.programUredjajLinkCollection = programUredjajLinkCollection;
    }

    @XmlTransient
    public Collection<EtalonBoca> getEtalonBocaCollection() {
        return etalonBocaCollection;
    }

    public void setEtalonBocaCollection(Collection<EtalonBoca> etalonBocaCollection) {
        this.etalonBocaCollection = etalonBocaCollection;
    }

    @XmlTransient
    public Collection<Kvarovi> getKvaroviCollection() {
        return kvaroviCollection;
    }

    public void setKvaroviCollection(Collection<Kvarovi> kvaroviCollection) {
        this.kvaroviCollection = kvaroviCollection;
    }

    @XmlTransient
    public Collection<Umjeravanje> getUmjeravanjeCollection1() {
        return umjeravanjeCollection1;
    }

    public void setUmjeravanjeCollection1(Collection<Umjeravanje> umjeravanjeCollection1) {
        this.umjeravanjeCollection1 = umjeravanjeCollection1;
    }

    @XmlTransient
    public Collection<EtalonDilucijska> getEtalonDilucijskaCollection() {
        return etalonDilucijskaCollection;
    }

    public void setEtalonDilucijskaCollection(Collection<EtalonDilucijska> etalonDilucijskaCollection) {
        this.etalonDilucijskaCollection = etalonDilucijskaCollection;
    }

    @XmlTransient
    public Collection<PostajaUredjajLink> getPostajaUredjajLinkCollection() {
        return postajaUredjajLinkCollection;
    }

    public void setPostajaUredjajLinkCollection(Collection<PostajaUredjajLink> postajaUredjajLinkCollection) {
        this.postajaUredjajLinkCollection = postajaUredjajLinkCollection;
    }

    @XmlTransient
    public Collection<EtalonCistiZrakKvaliteta> getEtalonCistiZrakKvalitetaCollection() {
        return etalonCistiZrakKvalitetaCollection;
    }

    public void setEtalonCistiZrakKvalitetaCollection(Collection<EtalonCistiZrakKvaliteta> etalonCistiZrakKvalitetaCollection) {
        this.etalonCistiZrakKvalitetaCollection = etalonCistiZrakKvalitetaCollection;
    }

    @XmlTransient
    public Collection<PlanUmjeravanja> getPlanUmjeravanjaCollection() {
        return planUmjeravanjaCollection;
    }

    public void setPlanUmjeravanjaCollection(Collection<PlanUmjeravanja> planUmjeravanjaCollection) {
        this.planUmjeravanjaCollection = planUmjeravanjaCollection;
    }

    @XmlTransient
    public Collection<PlanOdrzavanja> getPlanOdrzavanjaCollection() {
        return planOdrzavanjaCollection;
    }

    public void setPlanOdrzavanjaCollection(Collection<PlanOdrzavanja> planOdrzavanjaCollection) {
        this.planOdrzavanjaCollection = planOdrzavanjaCollection;
    }

    public ModelUredjaja getModelUredjajaId() {
        return modelUredjajaId;
    }

    public void setModelUredjajaId(ModelUredjaja modelUredjajaId) {
        this.modelUredjajaId = modelUredjajaId;
    }

    public VrstaOpreme getVrstaOpremeId() {
        return vrstaOpremeId;
    }

    public void setVrstaOpremeId(VrstaOpreme vrstaOpremeId) {
        this.vrstaOpremeId = vrstaOpremeId;
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
        if (!(object instanceof Uredjaj)) {
            return false;
        }
        Uredjaj other = (Uredjaj) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Uredjaj[ id=" + id + " ]";
    }
    
}
