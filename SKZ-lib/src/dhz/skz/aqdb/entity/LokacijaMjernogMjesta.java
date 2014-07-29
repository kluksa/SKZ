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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "lokacija_mjernog_mjesta", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LokacijaMjernogMjesta.findAll", query = "SELECT l FROM LokacijaMjernogMjesta l"),
    @NamedQuery(name = "LokacijaMjernogMjesta.findById", query = "SELECT l FROM LokacijaMjernogMjesta l WHERE l.id = :id"),
    @NamedQuery(name = "LokacijaMjernogMjesta.findByTekst", query = "SELECT l FROM LokacijaMjernogMjesta l WHERE l.tekst = :tekst")})
public class LokacijaMjernogMjesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Column(name = "tekst")
    private String tekst;

    public LokacijaMjernogMjesta() {
    }

    public LokacijaMjernogMjesta(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
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
        if (!(object instanceof LokacijaMjernogMjesta)) {
            return false;
        }
        LokacijaMjernogMjesta other = (LokacijaMjernogMjesta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.LokacijaMjernogMjesta[ id=" + id + " ]";
    }

}
