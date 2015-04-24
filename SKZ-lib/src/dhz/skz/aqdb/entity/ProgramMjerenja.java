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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "program_mjerenja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramMjerenja.findAll", query = "SELECT p FROM ProgramMjerenja p"),
    @NamedQuery(name = "ProgramMjerenja.findById", query = "SELECT p FROM ProgramMjerenja p WHERE p.id = :id"),
    @NamedQuery(name = "ProgramMjerenja.findByUsporednoMjerenje", query = "SELECT p FROM ProgramMjerenja p WHERE p.usporednoMjerenje = :usporednoMjerenje"),
    @NamedQuery(name = "ProgramMjerenja.findByPocetakMjerenja", query = "SELECT p FROM ProgramMjerenja p WHERE p.pocetakMjerenja = :pocetakMjerenja"),
    @NamedQuery(name = "ProgramMjerenja.findByZavrsetakMjerenja", query = "SELECT p FROM ProgramMjerenja p WHERE p.zavrsetakMjerenja = :zavrsetakMjerenja"),
    @NamedQuery(name = "ProgramMjerenja.findByPrikazWeb", query = "SELECT p FROM ProgramMjerenja p WHERE p.prikazWeb = :prikazWeb")})
public class ProgramMjerenja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usporedno_mjerenje")
    private int usporednoMjerenje;
    @Column(name = "pocetak_mjerenja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetakMjerenja;
    @Column(name = "zavrsetak_mjerenja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date zavrsetakMjerenja;
    @Column(name = "prikaz_web")
    private Boolean prikazWeb;
    @ManyToMany(mappedBy = "programMjerenjaCollection")
    private Collection<PrimateljiPodataka> primateljiPodatakaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenjaId")
    private Collection<ProgramUredjajLink> programUredjajLinkCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "programMjerenja")
    private IzvorProgramKljuceviMap izvorProgramKljuceviMap;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenjaId")
    private Collection<Podatak> podatakCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenja")
    private Collection<PrimateljProgramKljuceviMap> primateljProgramKljuceviMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenjaId")
    private Collection<ZeroSpanReferentneVrijednosti> zeroSpanReferentneVrijednostiCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenjaId")
    private Collection<ZeroSpan> zeroSpanCollection;
    @JoinColumn(name = "metoda_id", referencedColumnName = "id")
    @ManyToOne
    private AnalitickeMetode metodaId;
    @JoinColumn(name = "izvor_podataka_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IzvorPodataka izvorPodatakaId;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Komponenta komponentaId;
    @JoinColumn(name = "postaja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Postaja postajaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programMjerenjaId")
    private Collection<PodatakSirovi> podatakSiroviCollection;

    public ProgramMjerenja() {
    }

    public ProgramMjerenja(Integer id) {
        this.id = id;
    }

    public ProgramMjerenja(Integer id, int usporednoMjerenje) {
        this.id = id;
        this.usporednoMjerenje = usporednoMjerenje;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUsporednoMjerenje() {
        return usporednoMjerenje;
    }

    public void setUsporednoMjerenje(int usporednoMjerenje) {
        this.usporednoMjerenje = usporednoMjerenje;
    }

    public Date getPocetakMjerenja() {
        return pocetakMjerenja;
    }

    public void setPocetakMjerenja(Date pocetakMjerenja) {
        this.pocetakMjerenja = pocetakMjerenja;
    }

    public Date getZavrsetakMjerenja() {
        return zavrsetakMjerenja;
    }

    public void setZavrsetakMjerenja(Date zavrsetakMjerenja) {
        this.zavrsetakMjerenja = zavrsetakMjerenja;
    }

    public Boolean getPrikazWeb() {
        return prikazWeb;
    }

    public void setPrikazWeb(Boolean prikazWeb) {
        this.prikazWeb = prikazWeb;
    }

    @XmlTransient
    public Collection<PrimateljiPodataka> getPrimateljiPodatakaCollection() {
        return primateljiPodatakaCollection;
    }

    public void setPrimateljiPodatakaCollection(Collection<PrimateljiPodataka> primateljiPodatakaCollection) {
        this.primateljiPodatakaCollection = primateljiPodatakaCollection;
    }

    @XmlTransient
    public Collection<ProgramUredjajLink> getProgramUredjajLinkCollection() {
        return programUredjajLinkCollection;
    }

    public void setProgramUredjajLinkCollection(Collection<ProgramUredjajLink> programUredjajLinkCollection) {
        this.programUredjajLinkCollection = programUredjajLinkCollection;
    }

    @XmlTransient
    public IzvorProgramKljuceviMap getIzvorProgramKljuceviMap() {
        return izvorProgramKljuceviMap;
    }

    public void setIzvorProgramKljuceviMap(IzvorProgramKljuceviMap izvorProgramKljuceviMap) {
        this.izvorProgramKljuceviMap = izvorProgramKljuceviMap;
    }

    @XmlTransient
    public Collection<Podatak> getPodatakCollection() {
        return podatakCollection;
    }

    public void setPodatakCollection(Collection<Podatak> podatakCollection) {
        this.podatakCollection = podatakCollection;
    }

    @XmlTransient
    public Collection<PrimateljProgramKljuceviMap> getPrimateljProgramKljuceviMapCollection() {
        return primateljProgramKljuceviMapCollection;
    }

    public void setPrimateljProgramKljuceviMapCollection(Collection<PrimateljProgramKljuceviMap> primateljProgramKljuceviMapCollection) {
        this.primateljProgramKljuceviMapCollection = primateljProgramKljuceviMapCollection;
    }

    @XmlTransient
    public Collection<ZeroSpanReferentneVrijednosti> getZeroSpanReferentneVrijednostiCollection() {
        return zeroSpanReferentneVrijednostiCollection;
    }

    public void setZeroSpanReferentneVrijednostiCollection(Collection<ZeroSpanReferentneVrijednosti> zeroSpanReferentneVrijednostiCollection) {
        this.zeroSpanReferentneVrijednostiCollection = zeroSpanReferentneVrijednostiCollection;
    }

    @XmlTransient
    public Collection<ZeroSpan> getZeroSpanCollection() {
        return zeroSpanCollection;
    }

    public void setZeroSpanCollection(Collection<ZeroSpan> zeroSpanCollection) {
        this.zeroSpanCollection = zeroSpanCollection;
    }

    public AnalitickeMetode getMetodaId() {
        return metodaId;
    }

    public void setMetodaId(AnalitickeMetode metodaId) {
        this.metodaId = metodaId;
    }

    @XmlTransient
    public IzvorPodataka getIzvorPodatakaId() {
        return izvorPodatakaId;
    }

    public void setIzvorPodatakaId(IzvorPodataka izvorPodatakaId) {
        this.izvorPodatakaId = izvorPodatakaId;
    }

    public Komponenta getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(Komponenta komponentaId) {
        this.komponentaId = komponentaId;
    }

    public Postaja getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Postaja postajaId) {
        this.postajaId = postajaId;
    }

    @XmlTransient
    public Collection<PodatakSirovi> getPodatakSiroviCollection() {
        return podatakSiroviCollection;
    }

    public void setPodatakSiroviCollection(Collection<PodatakSirovi> podatakSiroviCollection) {
        this.podatakSiroviCollection = podatakSiroviCollection;
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
        if (!(object instanceof ProgramMjerenja)) {
            return false;
        }
        ProgramMjerenja other = (ProgramMjerenja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ProgramMjerenja[ id=" + id + " ]";
    }
    
}
