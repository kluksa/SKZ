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
@Table(name = "podatak_sirovi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PodatakSirovi.findAll", query = "SELECT p FROM PodatakSirovi p"),
    @NamedQuery(name = "PodatakSirovi.findById", query = "SELECT p FROM PodatakSirovi p WHERE p.id = :id"),
    @NamedQuery(name = "PodatakSirovi.findByVrijeme", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijeme = :vrijeme"),
    @NamedQuery(name = "PodatakSirovi.findByVrijednost", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijednost = :vrijednost"),
    @NamedQuery(name = "PodatakSirovi.findByStatus", query = "SELECT p FROM PodatakSirovi p WHERE p.status = :status"),
    @NamedQuery(name = "PodatakSirovi.findByGreska", query = "SELECT p FROM PodatakSirovi p WHERE p.greska = :greska"),
    @NamedQuery(name = "PodatakSirovi.findByVrijemeUpisa", query = "SELECT p FROM PodatakSirovi p WHERE p.vrijemeUpisa = :vrijemeUpisa"),
    @NamedQuery(name = "PodatakSirovi.findByStatusString", query = "SELECT p FROM PodatakSirovi p WHERE p.statusString = :statusString")})
public class PodatakSirovi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    @Basic(optional = false)
    @NotNull
    private float vrijednost;
    private Integer status;
    private Integer greska;
    @Basic(optional = false)
    @Column(name = "vrijeme_upisa")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeUpisa;
    @Size(max = 60)
    @Column(name = "status_string")
    private String statusString;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenjaId;

    public PodatakSirovi() {
    }

    public PodatakSirovi(Integer id) {
        this.id = id;
    }

    public PodatakSirovi(Integer id, Date vrijeme, float vrijednost, Date vrijemeUpisa) {
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

    public float getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(float vrijednost) {
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
