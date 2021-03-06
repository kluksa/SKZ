/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "podatak_sirovi", uniqueConstraints = {@UniqueConstraint(columnNames = {"vrijeme", "program_mjerenja_id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PodatakSirovi.findAll", query = "SELECT p FROM PodatakSirovi p"),
    @NamedQuery(name = "PodatakSirovi.findById", query = "SELECT p FROM PodatakSirovi p WHERE p.id = :id"),
    @NamedQuery(name = "PodatakSirovi.findByVrijeme", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijeme = :vrijeme"),
    @NamedQuery(name = "PodatakSirovi.findByVrijednost", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijednost = :vrijednost"),
    @NamedQuery(name = "PodatakSirovi.findByStatus", query = "SELECT p FROM PodatakSirovi p WHERE p.status = :status"),
    @NamedQuery(name = "PodatakSirovi.findByGreska", query = "SELECT p FROM PodatakSirovi p WHERE p.greska = :greska"),
    @NamedQuery(name = "PodatakSirovi.findByVrijemeUpisa", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijemeUpisa = :vrijemeUpisa"),
    @NamedQuery(name = "PodatakSirovi.findByStatusString", query = "SELECT p FROM PodatakSirovi p WHERE p.statusString = :statusString"),   
    @NamedQuery(name = "PodatakSirovi.findByVrijemeProgram", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijeme = :vrijeme AND p.programMjerenjaId = :programMjerenja"),   
    @NamedQuery(name = "PodatakSirovi.findByPocetakKraj", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijeme BETWEEN :pocetak AND :kraj"),
    @NamedQuery(name = "PodatakSirovi.findByProgramAsc", query = "SELECT p FROM PodatakSirovi p WHERE p.programMjerenjaId = :program ORDER BY p.vrijeme ASC"),
    @NamedQuery(name = "PodatakSirovi.findByProgramDesc", query = "SELECT p FROM PodatakSirovi p WHERE p.programMjerenjaId = :program ORDER BY p.vrijeme DESC")

})
public class PodatakSirovi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="podatak_sirovi_id_seq",
                       sequenceName="podatak_sirovi_id_seq",
                       allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator="podatak_sirovi_id_seq")
    @Column(name = "id", updatable=false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "vrijeme", columnDefinition="TIMESTAMP WITH TIME ZONE")
    private Date vrijeme;
    @Basic(optional = false)
    @NotNull
    private double vrijednost;
    private Integer status;
    private Integer greska;
    @Basic(optional = false)
    @Column(name = "vrijeme_upisa", insertable = false, updatable=true, columnDefinition="TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeUpisa;
    @Size(max = 120)
    @Column(name = "status_string")
    private String statusString;
    @JoinColumn(name = "korisnik_id", referencedColumnName = "id")
    @ManyToOne
    private Korisnik korisnikId;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenjaId;
    @Column(name = "nivo_validacije_id")
    @NotNull
    private Integer nivoValidacijeId;

    public PodatakSirovi() {
    }

    public PodatakSirovi(Integer id) {
        this.id = id;
    }

    public PodatakSirovi(Integer id, Date vrijeme, double vrijednost, Date vrijemeUpisa) {
        this.id = id;
        this.vrijeme = vrijeme;
        this.vrijednost = vrijednost;
        this.vrijemeUpisa = vrijemeUpisa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }
    
    public double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGreska() {
        return greska;
    }

    public void setGreska(Integer greska) {
        this.greska = greska;
    }

    public Date getVrijemeUpisa() {
        return vrijemeUpisa;
    }

    public void setVrijemeUpisa(Date vrijemeUpisa) {
        this.vrijemeUpisa = vrijemeUpisa;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Korisnik getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Korisnik korisnikId) {
        this.korisnikId = korisnikId;
    }

    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Integer getNivoValidacijeId() {
        return nivoValidacijeId;
    }

    public void setNivoValidacijeId(Integer nivoValidacijeId) {
        this.nivoValidacijeId = nivoValidacijeId;
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
        if (!(object instanceof PodatakSirovi)) {
            return false;
        }
        PodatakSirovi other = (PodatakSirovi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PodatakSirovi[ id=" + id + " ]";
    }
    
}
