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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "analiticke_metode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnalitickeMetode.findAll", query = "SELECT a FROM AnalitickeMetode a"),
    @NamedQuery(name = "AnalitickeMetode.findById", query = "SELECT a FROM AnalitickeMetode a WHERE a.id = :id"),
    @NamedQuery(name = "AnalitickeMetode.findByNaziv", query = "SELECT a FROM AnalitickeMetode a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "AnalitickeMetode.findByNorma", query = "SELECT a FROM AnalitickeMetode a WHERE a.norma = :norma"),
    @NamedQuery(name = "AnalitickeMetode.findByZeroDriftAbsolut", query = "SELECT a FROM AnalitickeMetode a WHERE a.zeroDriftAbsolut = :zeroDriftAbsolut"),
    @NamedQuery(name = "AnalitickeMetode.findBySpanDriftRelativ", query = "SELECT a FROM AnalitickeMetode a WHERE a.spanDriftRelativ = :spanDriftRelativ")})
public class AnalitickeMetode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 510)
    private String naziv;
    @Size(max = 90)
    private String norma;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "zero_drift_absolut")
    private Double zeroDriftAbsolut;
    @Column(name = "span_drift_relativ")
    private Double spanDriftRelativ;
    @JoinTable(name = "komponenta_metoda_link", joinColumns = {
        @JoinColumn(name = "metoda_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "komponenta_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Komponenta> komponentaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analitickeMetode")
    private Collection<DozvoljeneGranice> dozvoljeneGraniceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analitickeMetode")
    private Collection<MetodaUmjerneTocke> metodaUmjerneTockeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analitickeMetodeId")
    private Collection<Umjeravanje> umjeravanjeCollection;
    @OneToMany(mappedBy = "analitickeMetodeId")
    private Collection<ModelUredjaja> modelUredjajaCollection;
    @OneToMany(mappedBy = "metodaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "metodaId")
    private Collection<KomponentaMetodaLink> komponentaMetodaLinkCollection;

    public AnalitickeMetode() {
    }

    public AnalitickeMetode(Integer id) {
        this.id = id;
    }

    public AnalitickeMetode(Integer id, String naziv) {
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

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public Double getZeroDriftAbsolut() {
        return zeroDriftAbsolut;
    }

    public void setZeroDriftAbsolut(Double zeroDriftAbsolut) {
        this.zeroDriftAbsolut = zeroDriftAbsolut;
    }

    public Double getSpanDriftRelativ() {
        return spanDriftRelativ;
    }

    public void setSpanDriftRelativ(Double spanDriftRelativ) {
        this.spanDriftRelativ = spanDriftRelativ;
    }

    @XmlTransient
    public Collection<Komponenta> getKomponentaCollection() {
        return komponentaCollection;
    }

    public void setKomponentaCollection(Collection<Komponenta> komponentaCollection) {
        this.komponentaCollection = komponentaCollection;
    }

    @XmlTransient
    public Collection<DozvoljeneGranice> getDozvoljeneGraniceCollection() {
        return dozvoljeneGraniceCollection;
    }

    public void setDozvoljeneGraniceCollection(Collection<DozvoljeneGranice> dozvoljeneGraniceCollection) {
        this.dozvoljeneGraniceCollection = dozvoljeneGraniceCollection;
    }

    @XmlTransient
    public Collection<MetodaUmjerneTocke> getMetodaUmjerneTockeCollection() {
        return metodaUmjerneTockeCollection;
    }

    public void setMetodaUmjerneTockeCollection(Collection<MetodaUmjerneTocke> metodaUmjerneTockeCollection) {
        this.metodaUmjerneTockeCollection = metodaUmjerneTockeCollection;
    }

    @XmlTransient
    public Collection<Umjeravanje> getUmjeravanjeCollection() {
        return umjeravanjeCollection;
    }

    public void setUmjeravanjeCollection(Collection<Umjeravanje> umjeravanjeCollection) {
        this.umjeravanjeCollection = umjeravanjeCollection;
    }

    @XmlTransient
    public Collection<ModelUredjaja> getModelUredjajaCollection() {
        return modelUredjajaCollection;
    }

    public void setModelUredjajaCollection(Collection<ModelUredjaja> modelUredjajaCollection) {
        this.modelUredjajaCollection = modelUredjajaCollection;
    }

    @XmlTransient
    public Collection<ProgramMjerenja> getProgramMjerenjaCollection() {
        return programMjerenjaCollection;
    }

    public void setProgramMjerenjaCollection(Collection<ProgramMjerenja> programMjerenjaCollection) {
        this.programMjerenjaCollection = programMjerenjaCollection;
    }

    @XmlTransient
    public Collection<KomponentaMetodaLink> getKomponentaMetodaLinkCollection() {
        return komponentaMetodaLinkCollection;
    }

    public void setKomponentaMetodaLinkCollection(Collection<KomponentaMetodaLink> komponentaMetodaLinkCollection) {
        this.komponentaMetodaLinkCollection = komponentaMetodaLinkCollection;
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
        if (!(object instanceof AnalitickeMetode)) {
            return false;
        }
        AnalitickeMetode other = (AnalitickeMetode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.AnalitickeMetode[ id=" + id + " ]";
    }
    
}
