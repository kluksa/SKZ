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
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "naziv_postaje")
    private String nazivPostaje;
    @Size(max = 510)
    @Column(name = "naziv_lokacije")
    private String nazivLokacije;
    @Size(max = 16)
    @Column(name = "nacionalna_oznaka")
    private String nacionalnaOznaka;
    @Size(max = 16)
    @Column(name = "oznaka_postaje")
    private String oznakaPostaje;
    @Size(max = 255)
    @Column(name = "net_adresa")
    private String netAdresa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "geogr_duzina")
    private Double geogrDuzina;
    @Column(name = "geogr_sirina")
    private Double geogrSirina;
    @Column(name = "nadmorska_visina")
    private Integer nadmorskaVisina;
    @Size(max = 90)
    @Column(name = "nuts_oznaka")
    private String nutsOznaka;
    @Size(min = 1, max = 90)
    @Column(name = "radna_oznaka")
    private String radnaOznaka;

    
    
    private Integer stanovnistvo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "kratka_oznaka")
    private String kratkaOznaka;
    @ManyToMany(mappedBy = "postajaCollection")
    private Collection<ZemljopisneKarakteristike> zemljopisneKarakteristikeCollection;
    @ManyToMany(mappedBy = "postajaCollection")
    private Collection<CiljeviPracenja> ciljeviPracenjaCollection;
    @JoinColumn(name = "odgovorno_tijelo_id", referencedColumnName = "id")
    @ManyToOne
    private OdgovornoTijelo odgovornoTijeloId;
    @JoinColumn(name = "podrucje_id", referencedColumnName = "id")
    @ManyToOne
    private Podrucje podrucjeId;
    @JoinColumn(name = "reprezentativnost_id", referencedColumnName = "id")
    @ManyToOne
    private Reprezentativnost reprezentativnostId;
    @JoinColumn(name = "vrsta_postaje_izvor_id", referencedColumnName = "id")
    @ManyToOne
    private VrstaPostajeIzvor vrstaPostajeIzvorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postajaId")
    private Collection<PostajaUredjajLink> postajaUredjajLinkCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "postaja")
    private PrometnePostajeSvojstva prometnePostajeSvojstva;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postajaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "postaja")
    private IndustrijskePostajeSvojstva industrijskePostajeSvojstva;

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

    public void setNetAdresa(String netAdresa) {
        this.netAdresa = netAdresa;
    }

    public String getNetAdresa() {
        return netAdresa;
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

    public Double getGeogrDuzina() {
        return geogrDuzina;
    }

    public void setGeogrDuzina(Double geogrDuzina) {
        this.geogrDuzina = geogrDuzina;
    }

    public Double getGeogrSirina() {
        return geogrSirina;
    }

    public void setGeogrSirina(Double geogrSirina) {
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

    public String getRadnaOznaka() {
        return radnaOznaka;
    }

    public void setRadnaOznaka(String radnaOznaka) {
        this.radnaOznaka = radnaOznaka;
    }
    
    @XmlTransient
    public Collection<ZemljopisneKarakteristike> getZemljopisneKarakteristikeCollection() {
        return zemljopisneKarakteristikeCollection;
    }

    public void setZemljopisneKarakteristikeCollection(Collection<ZemljopisneKarakteristike> zemljopisneKarakteristikeCollection) {
        this.zemljopisneKarakteristikeCollection = zemljopisneKarakteristikeCollection;
    }

    @XmlTransient
    public Collection<CiljeviPracenja> getCiljeviPracenjaCollection() {
        return ciljeviPracenjaCollection;
    }

    public void setCiljeviPracenjaCollection(Collection<CiljeviPracenja> ciljeviPracenjaCollection) {
        this.ciljeviPracenjaCollection = ciljeviPracenjaCollection;
    }

    public OdgovornoTijelo getOdgovornoTijeloId() {
        return odgovornoTijeloId;
    }

    public void setOdgovornoTijeloId(OdgovornoTijelo odgovornoTijeloId) {
        this.odgovornoTijeloId = odgovornoTijeloId;
    }

    public Podrucje getPodrucjeId() {
        return podrucjeId;
    }

    public void setPodrucjeId(Podrucje podrucjeId) {
        this.podrucjeId = podrucjeId;
    }

    public Reprezentativnost getReprezentativnostId() {
        return reprezentativnostId;
    }

    public void setReprezentativnostId(Reprezentativnost reprezentativnostId) {
        this.reprezentativnostId = reprezentativnostId;
    }

    public VrstaPostajeIzvor getVrstaPostajeIzvorId() {
        return vrstaPostajeIzvorId;
    }

    public void setVrstaPostajeIzvorId(VrstaPostajeIzvor vrstaPostajeIzvorId) {
        this.vrstaPostajeIzvorId = vrstaPostajeIzvorId;
    }

    @XmlTransient
    public Collection<PostajaUredjajLink> getPostajaUredjajLinkCollection() {
        return postajaUredjajLinkCollection;
    }

    public void setPostajaUredjajLinkCollection(Collection<PostajaUredjajLink> postajaUredjajLinkCollection) {
        this.postajaUredjajLinkCollection = postajaUredjajLinkCollection;
    }

    public PrometnePostajeSvojstva getPrometnePostajeSvojstva() {
        return prometnePostajeSvojstva;
    }

    public void setPrometnePostajeSvojstva(PrometnePostajeSvojstva prometnePostajeSvojstva) {
        this.prometnePostajeSvojstva = prometnePostajeSvojstva;
    }

    @XmlTransient
    public Collection<ProgramMjerenja> getProgramMjerenjaCollection() {
        return programMjerenjaCollection;
    }

    public void setProgramMjerenjaCollection(Collection<ProgramMjerenja> programMjerenjaCollection) {
        this.programMjerenjaCollection = programMjerenjaCollection;
    }

    public IndustrijskePostajeSvojstva getIndustrijskePostajeSvojstva() {
        return industrijskePostajeSvojstva;
    }

    public void setIndustrijskePostajeSvojstva(IndustrijskePostajeSvojstva industrijskePostajeSvojstva) {
        this.industrijskePostajeSvojstva = industrijskePostajeSvojstva;
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
