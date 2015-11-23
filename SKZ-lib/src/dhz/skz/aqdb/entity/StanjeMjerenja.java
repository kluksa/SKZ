/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "stanje_mjerenja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StanjeMjerenja.findAll", query = "SELECT s FROM StanjeMjerenja s"),
    @NamedQuery(name = "StanjeMjerenja.findByProgramMjerenjaId", query = "SELECT s FROM StanjeMjerenja s WHERE s.stanjeMjerenjaPK.programMjerenjaId = :programMjerenjaId"),
    @NamedQuery(name = "StanjeMjerenja.findByPocetakPrimjene", query = "SELECT s FROM StanjeMjerenja s WHERE s.stanjeMjerenjaPK.pocetakPrimjene = :pocetakPrimjene"),
    @NamedQuery(name = "StanjeMjerenja.findByKrajPrimjene", query = "SELECT s FROM StanjeMjerenja s WHERE s.krajPrimjene = :krajPrimjene"),
    @NamedQuery(name = "StanjeMjerenja.findByStatus", query = "SELECT s FROM StanjeMjerenja s WHERE s.stanjeMjerenjaPK.status = :status"),
    @NamedQuery(name = "StanjeMjerenja.findByKomentar", query = "SELECT s FROM StanjeMjerenja s WHERE s.komentar = :komentar")})
public class StanjeMjerenja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StanjeMjerenjaPK stanjeMjerenjaPK;
    @Column(name = "kraj_primjene")
    @Temporal(TemporalType.TIMESTAMP)
    private Date krajPrimjene;
    private String komentar;
    @JoinColumn(name = "status", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FlagZaProgramMjerenja flagZaProgramMjerenja;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenja;

    public StanjeMjerenja() {
    }

    public StanjeMjerenja(StanjeMjerenjaPK stanjeMjerenjaPK) {
        this.stanjeMjerenjaPK = stanjeMjerenjaPK;
    }

    public StanjeMjerenja(int programMjerenjaId, Date pocetakPrimjene, int status) {
        this.stanjeMjerenjaPK = new StanjeMjerenjaPK(programMjerenjaId, pocetakPrimjene, status);
    }

    public StanjeMjerenjaPK getStanjeMjerenjaPK() {
        return stanjeMjerenjaPK;
    }

    public void setStanjeMjerenjaPK(StanjeMjerenjaPK stanjeMjerenjaPK) {
        this.stanjeMjerenjaPK = stanjeMjerenjaPK;
    }

    public Date getKrajPrimjene() {
        return krajPrimjene;
    }

    public void setKrajPrimjene(Date krajPrimjene) {
        this.krajPrimjene = krajPrimjene;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public FlagZaProgramMjerenja getFlagZaProgramMjerenja() {
        return flagZaProgramMjerenja;
    }

    public void setFlagZaProgramMjerenja(FlagZaProgramMjerenja flagZaProgramMjerenja) {
        this.flagZaProgramMjerenja = flagZaProgramMjerenja;
    }

    public ProgramMjerenja getProgramMjerenja() {
        return programMjerenja;
    }

    public void setProgramMjerenja(ProgramMjerenja programMjerenja) {
        this.programMjerenja = programMjerenja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stanjeMjerenjaPK != null ? stanjeMjerenjaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StanjeMjerenja)) {
            return false;
        }
        StanjeMjerenja other = (StanjeMjerenja) object;
        if ((this.stanjeMjerenjaPK == null && other.stanjeMjerenjaPK != null) || (this.stanjeMjerenjaPK != null && !this.stanjeMjerenjaPK.equals(other.stanjeMjerenjaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.StanjeMjerenja[ stanjeMjerenjaPK=" + stanjeMjerenjaPK + " ]";
    }
    
}
