/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "uloge_has_korisnik")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UlogeHasKorisnik.findAll", query = "SELECT u FROM UlogeHasKorisnik u"),
    @NamedQuery(name = "UlogeHasKorisnik.findById", query = "SELECT u FROM UlogeHasKorisnik u WHERE u.id = :id"),
    @NamedQuery(name = "UlogeHasKorisnik.findByVersion", query = "SELECT u FROM UlogeHasKorisnik u WHERE u.version = :version")})
public class UlogeHasKorisnik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Size(max = 90)
    private String version;
    @JoinColumn(name = "korisnik_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Korisnik korisnikId;
    @JoinColumn(name = "uloge_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uloge ulogeId;

    public UlogeHasKorisnik() {
    }

    public UlogeHasKorisnik(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Korisnik getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Korisnik korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Uloge getUlogeId() {
        return ulogeId;
    }

    public void setUlogeId(Uloge ulogeId) {
        this.ulogeId = ulogeId;
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
        if (!(object instanceof UlogeHasKorisnik)) {
            return false;
        }
        UlogeHasKorisnik other = (UlogeHasKorisnik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UlogeHasKorisnik[ id=" + id + " ]";
    }
    
}
