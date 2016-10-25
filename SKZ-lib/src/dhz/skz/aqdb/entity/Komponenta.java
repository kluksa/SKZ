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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Komponenta.findAll", query = "SELECT k FROM Komponenta k"),
    @NamedQuery(name = "Komponenta.findById", query = "SELECT k FROM Komponenta k WHERE k.id = :id"),
    @NamedQuery(name = "Komponenta.findByEolOznaka", query = "SELECT k FROM Komponenta k WHERE k.eolOznaka = :eolOznaka"),
    @NamedQuery(name = "Komponenta.findByIsoOznaka", query = "SELECT k FROM Komponenta k WHERE k.isoOznaka = :isoOznaka"),
    @NamedQuery(name = "Komponenta.findByFormula", query = "SELECT k FROM Komponenta k WHERE k.formula = :formula"),
    @NamedQuery(name = "Komponenta.findByNaziv", query = "SELECT k FROM Komponenta k WHERE k.naziv = :naziv"),
    @NamedQuery(name = "Komponenta.findByIzrazenoKao", query = "SELECT k FROM Komponenta k WHERE k.izrazenoKao = :izrazenoKao"),
    @NamedQuery(name = "Komponenta.findByVrstaKomponente", query = "SELECT k FROM Komponenta k WHERE k.vrstaKomponente = :vrstaKomponente"),
    @NamedQuery(name = "Komponenta.findByKonvVUM", query = "SELECT k FROM Komponenta k WHERE k.konvVUM = :konvVUM"),
    @NamedQuery(name = "Komponenta.findByNazivEng", query = "SELECT k FROM Komponenta k WHERE k.nazivEng = :nazivEng"),
    @NamedQuery(name = "Komponenta.findByPostajaDistinct", 
            query = "SELECT DISTINCT k FROM Komponenta k JOIN k.programMjerenjaCollection pm JOIN pm.postajaId po "
                    + " WHERE po.id = :postaja_id"),
    @NamedQuery(name = "Komponenta.findByProsijekTijekom", query = "SELECT k FROM Komponenta k WHERE k.prosijekTijekom = :prosijekTijekom")})
public class Komponenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "eol_oznaka")
    private Short eolOznaka;
    @Size(max = 4)
    @Column(name = "iso_oznaka")
    private String isoOznaka;
    @Size(max = 90)
    private String formula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    private String naziv;
    @Size(max = 90)
    @Column(name = "izrazeno_kao")
    private String izrazenoKao;
    @Column(name = "vrsta_komponente")
    private Character vrstaKomponente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "konv_v_u_m")
    private double konvVUM;
    @Size(max = 120)
    @Column(name = "naziv_eng")
    private String nazivEng;
    @Size(max = 90)
    @Column(name = "prosijek_tijekom")
    private String prosijekTijekom;
    @JoinTable(name = "model_uredjaja_komponenta_link", joinColumns = {
        @JoinColumn(name = "komponenta_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "model_uredjaja_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ModelUredjaja> modelUredjajaCollection;
    @ManyToMany(mappedBy = "komponentaCollection")
    private Collection<AnalitickeMetode> analitickeMetodeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<KomponentaHasRelevantneDirektive> komponentaHasRelevantneDirektiveCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<Granice> graniceCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<EtalonBoca> etalonBocaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<EtalonCistiZrakKvaliteta> etalonCistiZrakKvalitetaCollection;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<KomponentaMetodaLink> komponentaMetodaLinkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<UmjerneTocke> umjerneTockeCollection;

    public Komponenta() {
    }

    public Komponenta(Integer id) {
        this.id = id;
    }

    public Komponenta(Integer id, String naziv, double konvVUM) {
        this.id = id;
        this.naziv = naziv;
        this.konvVUM = konvVUM;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getEolOznaka() {
        return eolOznaka;
    }

    public void setEolOznaka(Short eolOznaka) {
        this.eolOznaka = eolOznaka;
    }

    public String getIsoOznaka() {
        return isoOznaka;
    }

    public void setIsoOznaka(String isoOznaka) {
        this.isoOznaka = isoOznaka;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIzrazenoKao() {
        return izrazenoKao;
    }

    public void setIzrazenoKao(String izrazenoKao) {
        this.izrazenoKao = izrazenoKao;
    }

    public Character getVrstaKomponente() {
        return vrstaKomponente;
    }

    public void setVrstaKomponente(Character vrstaKomponente) {
        this.vrstaKomponente = vrstaKomponente;
    }

    public double getKonvVUM() {
        return konvVUM;
    }

    public void setKonvVUM(double konvVUM) {
        this.konvVUM = konvVUM;
    }

    public String getNazivEng() {
        return nazivEng;
    }

    public void setNazivEng(String nazivEng) {
        this.nazivEng = nazivEng;
    }

    public String getProsijekTijekom() {
        return prosijekTijekom;
    }

    public void setProsijekTijekom(String prosijekTijekom) {
        this.prosijekTijekom = prosijekTijekom;
    }

    @XmlTransient
    public Collection<ModelUredjaja> getModelUredjajaCollection() {
        return modelUredjajaCollection;
    }

    public void setModelUredjajaCollection(Collection<ModelUredjaja> modelUredjajaCollection) {
        this.modelUredjajaCollection = modelUredjajaCollection;
    }

    @XmlTransient
    public Collection<AnalitickeMetode> getAnalitickeMetodeCollection() {
        return analitickeMetodeCollection;
    }

    public void setAnalitickeMetodeCollection(Collection<AnalitickeMetode> analitickeMetodeCollection) {
        this.analitickeMetodeCollection = analitickeMetodeCollection;
    }

    @XmlTransient
    public Collection<KomponentaHasRelevantneDirektive> getKomponentaHasRelevantneDirektiveCollection() {
        return komponentaHasRelevantneDirektiveCollection;
    }

    public void setKomponentaHasRelevantneDirektiveCollection(Collection<KomponentaHasRelevantneDirektive> komponentaHasRelevantneDirektiveCollection) {
        this.komponentaHasRelevantneDirektiveCollection = komponentaHasRelevantneDirektiveCollection;
    }

    @XmlTransient
    public Collection<Granice> getGraniceCollection() {
        return graniceCollection;
    }

    public void setGraniceCollection(Collection<Granice> graniceCollection) {
        this.graniceCollection = graniceCollection;
    }

    @XmlTransient
    public Collection<EtalonBoca> getEtalonBocaCollection() {
        return etalonBocaCollection;
    }

    public void setEtalonBocaCollection(Collection<EtalonBoca> etalonBocaCollection) {
        this.etalonBocaCollection = etalonBocaCollection;
    }

    @XmlTransient
    public Collection<UmjeravanjeHasIspitneVelicine> getUmjeravanjeHasIspitneVelicineCollection() {
        return umjeravanjeHasIspitneVelicineCollection;
    }

    public void setUmjeravanjeHasIspitneVelicineCollection(Collection<UmjeravanjeHasIspitneVelicine> umjeravanjeHasIspitneVelicineCollection) {
        this.umjeravanjeHasIspitneVelicineCollection = umjeravanjeHasIspitneVelicineCollection;
    }

    @XmlTransient
    public Collection<EtalonCistiZrakKvaliteta> getEtalonCistiZrakKvalitetaCollection() {
        return etalonCistiZrakKvalitetaCollection;
    }

    public void setEtalonCistiZrakKvalitetaCollection(Collection<EtalonCistiZrakKvaliteta> etalonCistiZrakKvalitetaCollection) {
        this.etalonCistiZrakKvalitetaCollection = etalonCistiZrakKvalitetaCollection;
    }

    public MjerneJedinice getMjerneJediniceId() {
        return mjerneJediniceId;
    }

    public void setMjerneJediniceId(MjerneJedinice mjerneJediniceId) {
        this.mjerneJediniceId = mjerneJediniceId;
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
        if (!(object instanceof Komponenta)) {
            return false;
        }
        Komponenta other = (Komponenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Komponenta[ id=" + id + " ]";
    }
    
}
