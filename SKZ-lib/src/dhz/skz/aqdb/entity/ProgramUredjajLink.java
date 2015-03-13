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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "program_uredjaj_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramUredjajLink.findAll", query = "SELECT p FROM ProgramUredjajLink p"),
    @NamedQuery(name = "ProgramUredjajLink.findById", query = "SELECT p FROM ProgramUredjajLink p WHERE p.id = :id"),
    @NamedQuery(name = "ProgramUredjajLink.findByVrijemePostavljanja", query = "SELECT p FROM ProgramUredjajLink p WHERE p.vrijemePostavljanja = :vrijemePostavljanja"),
    @NamedQuery(name = "ProgramUredjajLink.findByUcestalostIntegriranjaPodataka", query = "SELECT p FROM ProgramUredjajLink p WHERE p.ucestalostIntegriranjaPodataka = :ucestalostIntegriranjaPodataka"),
    @NamedQuery(name = "ProgramUredjajLink.findByVrijemeUklanjanja", query = "SELECT p FROM ProgramUredjajLink p WHERE p.vrijemeUklanjanja = :vrijemeUklanjanja")})
public class ProgramUredjajLink implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "vrijeme_postavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemePostavljanja;
    @Column(name = "ucestalost_integriranja_podataka")
    private Integer ucestalostIntegriranjaPodataka;
    @Column(name = "vrijeme_uklanjanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijemeUklanjanja;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenjaId;
    @JoinColumn(name = "uredjaj_id", referencedColumnName = "id")
    @ManyToOne
    private Uredjaj uredjajId;

    public ProgramUredjajLink() {
    }

    public ProgramUredjajLink(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVrijemePostavljanja() {
        return vrijemePostavljanja;
    }

    public void setVrijemePostavljanja(Date vrijemePostavljanja) {
        this.vrijemePostavljanja = vrijemePostavljanja;
    }

    public Integer getUcestalostIntegriranjaPodataka() {
        return ucestalostIntegriranjaPodataka;
    }

    public void setUcestalostIntegriranjaPodataka(Integer ucestalostIntegriranjaPodataka) {
        this.ucestalostIntegriranjaPodataka = ucestalostIntegriranjaPodataka;
    }

    public Date getVrijemeUklanjanja() {
        return vrijemeUklanjanja;
    }

    public void setVrijemeUklanjanja(Date vrijemeUklanjanja) {
        this.vrijemeUklanjanja = vrijemeUklanjanja;
    }

    public ProgramMjerenja getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(ProgramMjerenja programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Uredjaj getUredjajId() {
        return uredjajId;
    }

    public void setUredjajId(Uredjaj uredjajId) {
        this.uredjajId = uredjajId;
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
        if (!(object instanceof ProgramUredjajLink)) {
            return false;
        }
        ProgramUredjajLink other = (ProgramUredjajLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ProgramUredjajLink[ id=" + id + " ]";
    }
    
}
