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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
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
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Podatak.findAll", query = "SELECT p FROM Podatak p"),
    @NamedQuery(name = "Podatak.findByVrijemeProgramNivo", query = "SELECT p FROM Podatak p WHERE p.vrijeme = :vrijeme AND p.programMjerenjaId=:program AND  p.nivoValidacijeId = :nivo"),
    @NamedQuery(name = "Podatak.findByPodatakId", query = "SELECT p FROM Podatak p WHERE p.podatakId = :podatakId"),
    @NamedQuery(name = "Podatak.findByVrijeme", query = "SELECT p FROM Podatak p WHERE p.vrijeme = :vrijeme"),
    @NamedQuery(name = "Podatak.findByVrijednost", query = "SELECT p FROM Podatak p WHERE p.vrijednost = :vrijednost"),
    @NamedQuery(name = "Podatak.findByObuhvat", query = "SELECT p FROM Podatak p WHERE p.obuhvat = :obuhvat"),
    @NamedQuery(name = "Podatak.findByVrijemeUpisa", query = "SELECT p FROM Podatak p WHERE p.vrijemeUpisa = :vrijemeUpisa"),
    @NamedQuery(name = "Podatak.findByOriginalniPodatakId", query = "SELECT p FROM Podatak p WHERE p.originalniPodatakId = :originalniPodatakId"),
    @NamedQuery(name = "Podatak.findByStatus", query = "SELECT p FROM Podatak p WHERE p.status = :status"),
    @NamedQuery(name = "Podatak.findByKomponentaVrijemeNivo", query = "SELECT p FROM Podatak p JOIN p.programMjerenjaId pm "
            + "WHERE p.vrijeme > :pocetak AND p.vrijeme <= :kraj AND p.nivoValidacijeId = :nivo AND pm.komponentaId = :komponenta "
            + "AND pm.usporednoMjerenje = :usporedno ORDER BY p.vrijeme"),
    @NamedQuery(name = "Podatak.findByPostajaKomponentaVrijemeNivoUsporedno", query = "SELECT p FROM Podatak p JOIN p.programMjerenjaId pm "
            + "WHERE p.vrijeme > :pocetak AND p.vrijeme <= :kraj "
            + "AND p.nivoValidacijeId = :nivo AND pm.komponentaId = :komponenta AND pm.postajaId = :postaja "
            + "AND pm.usporednoMjerenje = :usporedno ORDER BY p.vrijeme"),
    @NamedQuery(name = "Podatak.getVrijemeZadnjegProgramNivo", query = "SELECT p.vrijeme FROM Podatak p WHERE p.programMjerenjaId = :program AND p.nivoValidacijeId = :nivo ORDER BY p.vrijeme DESC"),
    @NamedQuery(name = "Podatak.getVrijemeZadnjegPostaja", query = "SELECT p.vrijeme FROM Podatak p JOIN p.programMjerenjaId pm  WHERE pm.postajaId = :postaja ORDER BY p.vrijeme DESC")

})
public class Podatak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "podatak_id_seq",
            sequenceName = "podatak_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "podatak_id_seq")
    @Column(name = "podatak_id")
    private Integer podatakId;
    @Basic(optional = false)
    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double vrijednost;
    private Integer obuhvat;
    @Basic(optional = false)
    @Column(name = "vrijeme_upisa", insertable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeUpisa;
    @Column(name = "originalni_podatak_id")
    private Integer originalniPodatakId;
    @Basic(optional = false)
    @NotNull
    private int status;
    @ManyToMany(mappedBy = "podatakCollection")
    private Collection<Flag> flagCollection;
    @JoinColumn(name = "mjeritelj_id", referencedColumnName = "id")
    @ManyToOne
    private Korisnik mjeriteljId;
    @Column(name = "nivo_validacije_id")
    @NotNull
    private Integer nivoValidacijeId;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private ProgramMjerenja programMjerenjaId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "podatak")
    private Komentar komentar;

    public Podatak() {
    }

    public Podatak(Integer podatakId) {
        this.podatakId = podatakId;
    }

    public Podatak(Integer podatakId, Date vrijeme, Date vrijemeUpisa, int status) {
        this.podatakId = podatakId;
        this.vrijeme = vrijeme;
        this.vrijemeUpisa = vrijemeUpisa;
        this.status = status;
    }

    public Integer getPodatakId() {
        return podatakId;
    }

    public void setPodatakId(Integer podatakId) {
        this.podatakId = podatakId;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public Double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Integer getObuhvat() {
        return obuhvat;
    }

    public void setObuhvat(Integer obuhvat) {
        this.obuhvat = obuhvat;
    }

    public Date getVrijemeUpisa() {
        return vrijemeUpisa;
    }

    public void setVrijemeUpisa(Date vrijemeUpisa) {
        this.vrijemeUpisa = vrijemeUpisa;
    }

    public Integer getOriginalniPodatakId() {
        return originalniPodatakId;
    }

    public void setOriginalniPodatakId(Integer originalniPodatakId) {
        this.originalniPodatakId = originalniPodatakId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Flag> getFlagCollection() {
        return flagCollection;
    }

    public void setFlagCollection(Collection<Flag> flagCollection) {
        this.flagCollection = flagCollection;
    }

    public Korisnik getMjeriteljId() {
        return mjeriteljId;
    }

    public void setMjeriteljId(Korisnik mjeriteljId) {
        this.mjeriteljId = mjeriteljId;
    }

    public Integer getNivoValidacijeId() {
        return nivoValidacijeId;
    }

    public void setNivoValidacijeId(Integer nivoValidacijeId) {
        this.nivoValidacijeId = nivoValidacijeId;
    }

    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Komentar getKomentar() {
        return komentar;
    }

    public void setKomentar(Komentar komentar) {
        this.komentar = komentar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (podatakId != null ? podatakId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Podatak)) {
            return false;
        }
        Podatak other = (Podatak) object;
        if ((this.podatakId == null && other.podatakId != null) || (this.podatakId != null && !this.podatakId.equals(other.podatakId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Podatak[ podatakId=" + podatakId + " ]";
    }

}
