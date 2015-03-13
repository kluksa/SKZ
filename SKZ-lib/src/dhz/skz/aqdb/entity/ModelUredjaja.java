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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "model_uredjaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModelUredjaja.findAll", query = "SELECT m FROM ModelUredjaja m"),
    @NamedQuery(name = "ModelUredjaja.findById", query = "SELECT m FROM ModelUredjaja m WHERE m.id = :id"),
    @NamedQuery(name = "ModelUredjaja.findByOznakaModela", query = "SELECT m FROM ModelUredjaja m WHERE m.oznakaModela = :oznakaModela"),
    @NamedQuery(name = "ModelUredjaja.findByVrsta", query = "SELECT m FROM ModelUredjaja m WHERE m.vrsta = :vrsta"),
    @NamedQuery(name = "ModelUredjaja.findByBrojMjerenjaUSatu", query = "SELECT m FROM ModelUredjaja m WHERE m.brojMjerenjaUSatu = :brojMjerenjaUSatu"),
    @NamedQuery(name = "ModelUredjaja.findByImaZeroSpanCal", query = "SELECT m FROM ModelUredjaja m WHERE m.imaZeroSpanCal = :imaZeroSpanCal")})
public class ModelUredjaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "oznaka_modela")
    private String oznakaModela;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    private String vrsta;
    @Column(name = "broj_mjerenja_u_satu")
    private Integer brojMjerenjaUSatu;
    @Column(name = "ima_zero_span_cal")
    private Boolean imaZeroSpanCal;
    @ManyToMany(mappedBy = "modelUredjajaCollection")
    private Collection<Komponenta> komponentaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelUredjaja")
    private Collection<ValidatorModelIzvor> validatorModelIzvorCollection;
    @JoinColumn(name = "analiticke_metode_id", referencedColumnName = "id")
    @ManyToOne
    private AnalitickeMetode analitickeMetodeId;
    @JoinColumn(name = "proizvodjac_id", referencedColumnName = "id")
    @ManyToOne
    private Proizvodjac proizvodjacId;
    @JoinColumn(name = "validator_id", referencedColumnName = "id")
    @ManyToOne
    private Validatori validatorId;
    @OneToMany(mappedBy = "modelUredjajaId")
    private Collection<Uredjaj> uredjajCollection;

    public ModelUredjaja() {
    }

    public ModelUredjaja(Integer id) {
        this.id = id;
    }

    public ModelUredjaja(Integer id, String oznakaModela, String vrsta) {
        this.id = id;
        this.oznakaModela = oznakaModela;
        this.vrsta = vrsta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOznakaModela() {
        return oznakaModela;
    }

    public void setOznakaModela(String oznakaModela) {
        this.oznakaModela = oznakaModela;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public Integer getBrojMjerenjaUSatu() {
        return brojMjerenjaUSatu;
    }

    public void setBrojMjerenjaUSatu(Integer brojMjerenjaUSatu) {
        this.brojMjerenjaUSatu = brojMjerenjaUSatu;
    }

    public Boolean getImaZeroSpanCal() {
        return imaZeroSpanCal;
    }

    public void setImaZeroSpanCal(Boolean imaZeroSpanCal) {
        this.imaZeroSpanCal = imaZeroSpanCal;
    }

    @XmlTransient
    public Collection<Komponenta> getKomponentaCollection() {
        return komponentaCollection;
    }

    public void setKomponentaCollection(Collection<Komponenta> komponentaCollection) {
        this.komponentaCollection = komponentaCollection;
    }

    @XmlTransient
    public Collection<ValidatorModelIzvor> getValidatorModelIzvorCollection() {
        return validatorModelIzvorCollection;
    }

    public void setValidatorModelIzvorCollection(Collection<ValidatorModelIzvor> validatorModelIzvorCollection) {
        this.validatorModelIzvorCollection = validatorModelIzvorCollection;
    }

    public AnalitickeMetode getAnalitickeMetodeId() {
        return analitickeMetodeId;
    }

    public void setAnalitickeMetodeId(AnalitickeMetode analitickeMetodeId) {
        this.analitickeMetodeId = analitickeMetodeId;
    }

    public Proizvodjac getProizvodjacId() {
        return proizvodjacId;
    }

    public void setProizvodjacId(Proizvodjac proizvodjacId) {
        this.proizvodjacId = proizvodjacId;
    }

    public Validatori getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Validatori validatorId) {
        this.validatorId = validatorId;
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
        if (!(object instanceof ModelUredjaja)) {
            return false;
        }
        ModelUredjaja other = (ModelUredjaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ModelUredjaja[ id=" + id + " ]";
    }
    
}
