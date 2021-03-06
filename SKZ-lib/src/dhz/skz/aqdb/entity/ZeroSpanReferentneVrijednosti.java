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
import javax.persistence.Table;
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
@Table(name = "zero_span_referentne_vrijednosti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findAll", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z"),
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findById", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z WHERE z.id = :id"),
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findByPocetakPrimjene", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z WHERE z.pocetakPrimjene = :pocetakPrimjene"),
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findByVrsta", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z WHERE z.vrsta = :vrsta"),
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findByProgramVrsta", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z WHERE z.vrsta LIKE :vrsta and z.programMjerenjaId = :program"),
    @NamedQuery(name = "ZeroSpanReferentneVrijednosti.findByVrijednost", query = "SELECT z FROM ZeroSpanReferentneVrijednosti z WHERE z.vrijednost = :vrijednost")})
public class ZeroSpanReferentneVrijednosti implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pocetak_primjene")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetakPrimjene;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    private String vrsta;
    @Basic(optional = false)
    @NotNull
    private double vrijednost;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenjaId;

    public ZeroSpanReferentneVrijednosti() {
    }

    public ZeroSpanReferentneVrijednosti(Integer id) {
        this.id = id;
    }

    public ZeroSpanReferentneVrijednosti(Integer id, Date pocetakPrimjene, String vrsta, double vrijednost) {
        this.id = id;
        this.pocetakPrimjene = pocetakPrimjene;
        this.vrsta = vrsta;
        this.vrijednost = vrijednost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPocetakPrimjene() {
        return pocetakPrimjene;
    }

    public void setPocetakPrimjene(Date pocetakPrimjene) {
        this.pocetakPrimjene = pocetakPrimjene;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(double vrijednost) {
        this.vrijednost = vrijednost;
    }

    @XmlTransient
    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
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
        if (!(object instanceof ZeroSpanReferentneVrijednosti)) {
            return false;
        }
        ZeroSpanReferentneVrijednosti other = (ZeroSpanReferentneVrijednosti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ZeroSpanReferentneVrijednosti[ id=" + id + " ]";
    }
    
}
