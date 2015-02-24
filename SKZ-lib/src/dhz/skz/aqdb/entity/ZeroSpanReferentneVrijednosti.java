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

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "zero_span_referentne_vrijednosti", catalog = "aqdb_likz", schema = "")
@XmlRootElement
public class ZeroSpanReferentneVrijednosti implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "pocetak_primjene", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date pocetakPrimjene;
    
    @Basic(optional = false)
    @Size(min = 1, max = 2)
    @Column(nullable = false, length = 2)
    private String vrsta;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 12)
    private Float vrijednost;
    
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenjaId;

    public ZeroSpanReferentneVrijednosti() {
    }

    public ZeroSpanReferentneVrijednosti(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public Float getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Float vrijednost) {
        this.vrijednost = vrijednost;
    }

    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Date getPocetakPrimjene() {
        return pocetakPrimjene;
    }

    public void setPocetakPrimjene(Date pocetakPrimjene) {
        this.pocetakPrimjene = pocetakPrimjene;
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
        return "dhz.skz.aqdb.entity.ZeroSpan[ id=" + id + " ]";
    }
    
}
