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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "komponenta", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Komponenta.findAll", query = "SELECT k FROM Komponenta k"),
    @NamedQuery(name = "Komponenta.findById", query = "SELECT k FROM Komponenta k WHERE k.id = :id"),
    @NamedQuery(name = "Komponenta.findByEolOznaka", query = "SELECT k FROM Komponenta k WHERE k.eolOznaka = :eolOznaka"),
    @NamedQuery(name = "Komponenta.findByIsoOznaka", query = "SELECT k FROM Komponenta k WHERE k.isoOznaka = :isoOznaka"),
    @NamedQuery(name = "Komponenta.findByFormula", query = "SELECT k FROM Komponenta k WHERE k.formula = :formula"),
    @NamedQuery(name = "Komponenta.findByNaziv", query = "SELECT k FROM Komponenta k WHERE k.naziv = :naziv"),
    @NamedQuery(name = "Komponenta.findByProsijekTijekom", query = "SELECT k FROM Komponenta k WHERE k.prosijekTijekom = :prosijekTijekom"),
    @NamedQuery(name = "Komponenta.findByIzrazenoKao", query = "SELECT k FROM Komponenta k WHERE k.izrazenoKao = :izrazenoKao"),
    @NamedQuery(name = "Komponenta.findByVrstaKomponente", query = "SELECT k FROM Komponenta k WHERE k.vrstaKomponente = :vrstaKomponente"),
    @NamedQuery(name = "Komponenta.findByKonvVUM", query = "SELECT k FROM Komponenta k WHERE k.konvVUM = :konvVUM"),
    @NamedQuery(name = "Komponenta.findByNazivEng", query = "SELECT k FROM Komponenta k WHERE k.nazivEng = :nazivEng")})
public class Komponenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "eol_oznaka")
    private Short eolOznaka;
    @Column(name = "iso_oznaka")
    private String isoOznaka;
    @Column(name = "formula")
    private String formula;
    @Basic(optional = false)
    @Column(name = "naziv")
    private String naziv;
    @Column(name = "prosijek_tijekom")
    private String prosijekTijekom;
    @Column(name = "izrazeno_kao")
    private String izrazenoKao;
    @Column(name = "vrsta_komponente")
    private Character vrstaKomponente;
    @Basic(optional = false)
    @Column(name = "konv_v_u_m")
    private float konvVUM;
    @Column(name = "naziv_eng")
    private String nazivEng;
    @JoinTable(name = "komponenta_has_relevantne_smjernice", joinColumns = {
        @JoinColumn(name = "komponenta_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "relevantne_smjernice_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<RelevantneSmjernice> relevantneSmjerniceCollection;
    @JoinTable(name = "model_uredjaja_komponenta_link", joinColumns = {
        @JoinColumn(name = "komponenta_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "model_uredjaja_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ModelUredjaja> modelUredjajaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<SluzbeneVrijednosti> sluzbeneVrijednostiCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponentaId")
    private Collection<Umjeravanje> umjeravanjeCollection;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;

    public Komponenta() {
    }

    public Komponenta(Integer id) {
        this.id = id;
    }

    public Komponenta(Integer id, String naziv, float konvVUM) {
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

    public String getProsijekTijekom() {
        return prosijekTijekom;
    }

    public void setProsijekTijekom(String prosijekTijekom) {
        this.prosijekTijekom = prosijekTijekom;
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

    public float getKonvVUM() {
        return konvVUM;
    }

    public void setKonvVUM(float konvVUM) {
        this.konvVUM = konvVUM;
    }

    public String getNazivEng() {
        return nazivEng;
    }

    public void setNazivEng(String nazivEng) {
        this.nazivEng = nazivEng;
    }

    @XmlTransient
    public Collection<RelevantneSmjernice> getRelevantneSmjerniceCollection() {
        return relevantneSmjerniceCollection;
    }

    public void setRelevantneSmjerniceCollection(Collection<RelevantneSmjernice> relevantneSmjerniceCollection) {
        this.relevantneSmjerniceCollection = relevantneSmjerniceCollection;
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
    public Collection<SluzbeneVrijednosti> getSluzbeneVrijednostiCollection() {
        return sluzbeneVrijednostiCollection;
    }

    public void setSluzbeneVrijednostiCollection(Collection<SluzbeneVrijednosti> sluzbeneVrijednostiCollection) {
        this.sluzbeneVrijednostiCollection = sluzbeneVrijednostiCollection;
    }

    @XmlTransient
    public Collection<Umjeravanje> getUmjeravanjeCollection() {
        return umjeravanjeCollection;
    }

    public void setUmjeravanjeCollection(Collection<Umjeravanje> umjeravanjeCollection) {
        this.umjeravanjeCollection = umjeravanjeCollection;
    }

    public MjerneJedinice getMjerneJediniceId() {
        return mjerneJediniceId;
    }

    public void setMjerneJediniceId(MjerneJedinice mjerneJediniceId) {
        this.mjerneJediniceId = mjerneJediniceId;
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
