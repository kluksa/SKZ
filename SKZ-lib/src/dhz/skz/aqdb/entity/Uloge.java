/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "uloge", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uloge.findAll", query = "SELECT u FROM Uloge u"),
    @NamedQuery(name = "Uloge.findById", query = "SELECT u FROM Uloge u WHERE u.id = :id"),
    @NamedQuery(name = "Uloge.findByAuthority", query = "SELECT u FROM Uloge u WHERE u.authority = :authority"),
    @NamedQuery(name = "Uloge.findByUlogaId", query = "SELECT u FROM Uloge u WHERE u.ulogaId = :ulogaId"),
    @NamedQuery(name = "Uloge.findByVersion", query = "SELECT u FROM Uloge u WHERE u.version = :version")})
public class Uloge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "authority")
    private String authority;
    @Basic(optional = false)
    @Column(name = "uloga_id")
    private String ulogaId;
    @Column(name = "version")
    private Boolean version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ulogeId")
    private Collection<UlogeHasKorisnik> ulogeHasKorisnikCollection;

    public Uloge() {
    }

    public Uloge(Integer id) {
        this.id = id;
    }

    public Uloge(Integer id, String authority, String ulogaId) {
        this.id = id;
        this.authority = authority;
        this.ulogaId = ulogaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUlogaId() {
        return ulogaId;
    }

    public void setUlogaId(String ulogaId) {
        this.ulogaId = ulogaId;
    }

    public Boolean getVersion() {
        return version;
    }

    public void setVersion(Boolean version) {
        this.version = version;
    }

    @XmlTransient
    public Collection<UlogeHasKorisnik> getUlogeHasKorisnikCollection() {
        return ulogeHasKorisnikCollection;
    }

    public void setUlogeHasKorisnikCollection(Collection<UlogeHasKorisnik> ulogeHasKorisnikCollection) {
        this.ulogeHasKorisnikCollection = ulogeHasKorisnikCollection;
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
        if (!(object instanceof Uloge)) {
            return false;
        }
        Uloge other = (Uloge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Uloge[ id=" + id + " ]";
    }

}
