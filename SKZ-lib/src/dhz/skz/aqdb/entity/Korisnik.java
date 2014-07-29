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
@Table(name = "korisnik", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Korisnik.findAll", query = "SELECT k FROM Korisnik k"),
    @NamedQuery(name = "Korisnik.findById", query = "SELECT k FROM Korisnik k WHERE k.id = :id"),
    @NamedQuery(name = "Korisnik.findByKorisnickoIme", query = "SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme"),
    @NamedQuery(name = "Korisnik.findByLozinka", query = "SELECT k FROM Korisnik k WHERE k.lozinka = :lozinka"),
    @NamedQuery(name = "Korisnik.findByIme", query = "SELECT k FROM Korisnik k WHERE k.ime = :ime"),
    @NamedQuery(name = "Korisnik.findByPrezime", query = "SELECT k FROM Korisnik k WHERE k.prezime = :prezime"),
    @NamedQuery(name = "Korisnik.findByEnabled", query = "SELECT k FROM Korisnik k WHERE k.enabled = :enabled"),
    @NamedQuery(name = "Korisnik.findByVersion", query = "SELECT k FROM Korisnik k WHERE k.version = :version")})
public class Korisnik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "korisnicko_ime")
    private String korisnickoIme;
    @Basic(optional = false)
    @Column(name = "lozinka")
    private String lozinka;
    @Column(name = "ime")
    private String ime;
    @Column(name = "prezime")
    private String prezime;
    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name = "version")
    private Integer version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "korisnikId")
    private Collection<UlogeHasKorisnik> ulogeHasKorisnikCollection;
    @OneToMany(mappedBy = "mjeriteljId")
    private Collection<Umjeravanje> umjeravanjeCollection;
    @OneToMany(mappedBy = "mjeriteljId")
    private Collection<Podatak> podatakCollection;

    public Korisnik() {
    }

    public Korisnik(Integer id) {
        this.id = id;
    }

    public Korisnik(Integer id, String korisnickoIme, String lozinka) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @XmlTransient
    public Collection<UlogeHasKorisnik> getUlogeHasKorisnikCollection() {
        return ulogeHasKorisnikCollection;
    }

    public void setUlogeHasKorisnikCollection(Collection<UlogeHasKorisnik> ulogeHasKorisnikCollection) {
        this.ulogeHasKorisnikCollection = ulogeHasKorisnikCollection;
    }

    @XmlTransient
    public Collection<Umjeravanje> getUmjeravanjeCollection() {
        return umjeravanjeCollection;
    }

    public void setUmjeravanjeCollection(Collection<Umjeravanje> umjeravanjeCollection) {
        this.umjeravanjeCollection = umjeravanjeCollection;
    }

    @XmlTransient
    public Collection<Podatak> getPodatakCollection() {
        return podatakCollection;
    }

    public void setPodatakCollection(Collection<Podatak> podatakCollection) {
        this.podatakCollection = podatakCollection;
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
        if (!(object instanceof Korisnik)) {
            return false;
        }
        Korisnik other = (Korisnik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Korisnik[ id=" + id + " ]";
    }

}
