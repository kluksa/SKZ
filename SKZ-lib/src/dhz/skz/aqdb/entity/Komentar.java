/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "komentar", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Komentar.findAll", query = "SELECT k FROM Komentar k"),
    @NamedQuery(name = "Komentar.findByPodatakId", query = "SELECT k FROM Komentar k WHERE k.podatakId = :podatakId"),
    @NamedQuery(name = "Komentar.findByTekst", query = "SELECT k FROM Komentar k WHERE k.tekst = :tekst")})
public class Komentar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "podatak_id")
    private Integer podatakId;
    @Column(name = "tekst")
    private String tekst;
    @JoinColumn(name = "podatak_id", referencedColumnName = "podatak_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Podatak podatak;

    public Komentar() {
    }

    public Komentar(Integer podatakId) {
        this.podatakId = podatakId;
    }

    public Integer getPodatakId() {
        return podatakId;
    }

    public void setPodatakId(Integer podatakId) {
        this.podatakId = podatakId;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Podatak getPodatak() {
        return podatak;
    }

    public void setPodatak(Podatak podatak) {
        this.podatak = podatak;
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
        if (!(object instanceof Komentar)) {
            return false;
        }
        Komentar other = (Komentar) object;
        if ((this.podatakId == null && other.podatakId != null) || (this.podatakId != null && !this.podatakId.equals(other.podatakId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Komentar[ podatakId=" + podatakId + " ]";
    }

}
