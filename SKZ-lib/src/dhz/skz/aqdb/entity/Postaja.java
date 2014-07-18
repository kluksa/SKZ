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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "postaja", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Postaja.findAll", query = "SELECT p FROM Postaja p"),
    @NamedQuery(name = "Postaja.findById", query = "SELECT p FROM Postaja p WHERE p.id = :id"),
    @NamedQuery(name = "Postaja.findByNazivPostaje", query = "SELECT p FROM Postaja p WHERE p.nazivPostaje = :nazivPostaje"),
    @NamedQuery(name = "Postaja.findByNazivLokacije", query = "SELECT p FROM Postaja p WHERE p.nazivLokacije = :nazivLokacije"),
    @NamedQuery(name = "Postaja.findByNacionalnaOznaka", query = "SELECT p FROM Postaja p WHERE p.nacionalnaOznaka = :nacionalnaOznaka"),
    @NamedQuery(name = "Postaja.findByOznakaPostaje", query = "SELECT p FROM Postaja p WHERE p.oznakaPostaje = :oznakaPostaje"),
    @NamedQuery(name = "Postaja.findByGeogrDuzina", query = "SELECT p FROM Postaja p WHERE p.geogrDuzina = :geogrDuzina"),
    @NamedQuery(name = "Postaja.findByGeogrSirina", query = "SELECT p FROM Postaja p WHERE p.geogrSirina = :geogrSirina"),
    @NamedQuery(name = "Postaja.findByNadmorskaVisina", query = "SELECT p FROM Postaja p WHERE p.nadmorskaVisina = :nadmorskaVisina"),
    @NamedQuery(name = "Postaja.findByNutsOznaka", query = "SELECT p FROM Postaja p WHERE p.nutsOznaka = :nutsOznaka"),
    @NamedQuery(name = "Postaja.findByStanovnistvo", query = "SELECT p FROM Postaja p WHERE p.stanovnistvo = :stanovnistvo"),
    @NamedQuery(name = "Postaja.findByKratkaOznaka", query = "SELECT p FROM Postaja p WHERE p.kratkaOznaka = :kratkaOznaka")})
public class Postaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "naziv_postaje")
    private String nazivPostaje;
    @Column(name = "naziv_lokacije")
    private String nazivLokacije;
    @Column(name = "nacionalna_oznaka")
    private String nacionalnaOznaka;
    @Column(name = "oznaka_postaje")
    private String oznakaPostaje;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "geogr_duzina")
    private Float geogrDuzina;
    @Column(name = "geogr_sirina")
    private Float geogrSirina;
    @Column(name = "nadmorska_visina")
    private Integer nadmorskaVisina;
    @Column(name = "nuts_oznaka")
    private String nutsOznaka;
    @Column(name = "stanovnistvo")
    private Integer stanovnistvo;
    @Basic(optional = false)
    @Column(name = "kratka_oznaka")
    private String kratkaOznaka;
    @ManyToMany(mappedBy = "postajaCollection")
    private Collection<CiljeviPracenja> ciljeviPracenjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postajaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "postaja")
    private PrometnePostajeSvojstva prometnePostajeSvojstva;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "postaja")
    private IndustrijskePostajeSvojstva industrijskePostajeSvojstva;
    @JoinColumn(name = "vrsta_postaje_izvor_id", referencedColumnName = "id")
    @ManyToOne
    private VrstaPostajeIzvor vrstaPostajeIzvorId;
    @JoinColumn(name = "reprezentativnost_id", referencedColumnName = "id")
    @ManyToOne
    private Reprezentativnost reprezentativnostId;
    @JoinColumn(name = "podrucje_id", referencedColumnName = "id")
    @ManyToOne
    private Podrucje podrucjeId;
    @JoinColumn(name = "odgovorno_tijelo_id", referencedColumnName = "id")
    @ManyToOne
    private OdgovornoTijelo odgovornoTijeloId;

    public Postaja() {
    }

    public Postaja(Integer id) {
        this.id = id;
    }

    public Postaja(Integer id, String nazivPostaje, String kratkaOznaka) {
        this.id = id;
        this.nazivPostaje = nazivPostaje;
        this.kratkaOznaka = kratkaOznaka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazivPostaje() {
        return nazivPostaje;
    }

    public void setNazivPostaje(String nazivPostaje) {
        this.nazivPostaje = nazivPostaje;
    }

    public String getNazivLokacije() {
        return nazivLokacije;
    }

    public void setNazivLokacije(String nazivLokacije) {
        this.nazivLokacije = nazivLokacije;
    }

    public String getNacionalnaOznaka() {
        return nacionalnaOznaka;
    }

    public void setNacionalnaOznaka(String nacionalnaOznaka) {
        this.nacionalnaOznaka = nacionalnaOznaka;
    }

    public String getOznakaPostaje() {
        return oznakaPostaje;
    }

    public void setOznakaPostaje(String oznakaPostaje) {
        this.oznakaPostaje = oznakaPostaje;
    }

    public Float getGeogrDuzina() {
        return geogrDuzina;
    }

    public void setGeogrDuzina(Float geogrDuzina) {
        this.geogrDuzina = geogrDuzina;
    }

    public Float getGeogrSirina() {
        return geogrSirina;
    }

    public void setGeogrSirina(Float geogrSirina) {
        this.geogrSirina = geogrSirina;
    }

    public Integer getNadmorskaVisina() {
        return nadmorskaVisina;
    }

    public void setNadmorskaVisina(Integer nadmorskaVisina) {
        this.nadmorskaVisina = nadmorskaVisina;
    }

    public String getNutsOznaka() {
        return nutsOznaka;
    }

    public void setNutsOznaka(String nutsOznaka) {
        this.nutsOznaka = nutsOznaka;
    }

    public Integer getStanovnistvo() {
        return stanovnistvo;
    }

    public void setStanovnistvo(Integer stanovnistvo) {
        this.stanovnistvo = stanovnistvo;
    }

    public String getKratkaOznaka() {
        return kratkaOznaka;
    }

    public void setKratkaOznaka(String kratkaOznaka) {
        this.kratkaOznaka = kratkaOznaka;
    }

    @XmlTransient
    public Collection<CiljeviPracenja> getCiljeviPracenjaCollection() {
        return ciljeviPracenjaCollection;
    }

    public void setCiljeviPracenjaCollection(Collection<CiljeviPracenja> ciljeviPracenjaCollection) {
        this.ciljeviPracenjaCollection = ciljeviPracenjaCollection;
    }

    @XmlTransient
    public Collection<ProgramMjerenja> getProgramMjerenjaCollection() {
        return programMjerenjaCollection;
    }

    public void setProgramMjerenjaCollection(Collection<ProgramMjerenja> programMjerenjaCollection) {
        this.programMjerenjaCollection = programMjerenjaCollection;
    }

    public PrometnePostajeSvojstva getPrometnePostajeSvojstva() {
        return prometnePostajeSvojstva;
    }

    public void setPrometnePostajeSvojstva(PrometnePostajeSvojstva prometnePostajeSvojstva) {
        this.prometnePostajeSvojstva = prometnePostajeSvojstva;
    }

    public IndustrijskePostajeSvojstva getIndustrijskePostajeSvojstva() {
        return industrijskePostajeSvojstva;
    }

    public void setIndustrijskePostajeSvojstva(IndustrijskePostajeSvojstva industrijskePostajeSvojstva) {
        this.industrijskePostajeSvojstva = industrijskePostajeSvojstva;
    }

    public VrstaPostajeIzvor getVrstaPostajeIzvorId() {
        return vrstaPostajeIzvorId;
    }

    public void setVrstaPostajeIzvorId(VrstaPostajeIzvor vrstaPostajeIzvorId) {
        this.vrstaPostajeIzvorId = vrstaPostajeIzvorId;
    }

    public Reprezentativnost getReprezentativnostId() {
        return reprezentativnostId;
    }

    public void setReprezentativnostId(Reprezentativnost reprezentativnostId) {
        this.reprezentativnostId = reprezentativnostId;
    }

    public Podrucje getPodrucjeId() {
        return podrucjeId;
    }

    public void setPodrucjeId(Podrucje podrucjeId) {
        this.podrucjeId = podrucjeId;
    }

    public OdgovornoTijelo getOdgovornoTijeloId() {
        return odgovornoTijeloId;
    }

    public void setOdgovornoTijeloId(OdgovornoTijelo odgovornoTijeloId) {
        this.odgovornoTijeloId = odgovornoTijeloId;
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
        if (!(object instanceof Postaja)) {
            return false;
        }
        Postaja other = (Postaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Postaja[ id=" + id + " ]";
    }
    
}
