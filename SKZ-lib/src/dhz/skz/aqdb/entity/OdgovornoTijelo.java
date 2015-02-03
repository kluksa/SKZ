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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "odgovorno_tijelo", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OdgovornoTijelo.findAll", query = "SELECT o FROM OdgovornoTijelo o"),
    @NamedQuery(name = "OdgovornoTijelo.findById", query = "SELECT o FROM OdgovornoTijelo o WHERE o.id = :id"),
    @NamedQuery(name = "OdgovornoTijelo.findByNaziv", query = "SELECT o FROM OdgovornoTijelo o WHERE o.naziv = :naziv"),
    @NamedQuery(name = "OdgovornoTijelo.findByOdgovornaOsoba", query = "SELECT o FROM OdgovornoTijelo o WHERE o.odgovornaOsoba = :odgovornaOsoba"),
    @NamedQuery(name = "OdgovornoTijelo.findByAdresa", query = "SELECT o FROM OdgovornoTijelo o WHERE o.adresa = :adresa"),
    @NamedQuery(name = "OdgovornoTijelo.findByTelefon", query = "SELECT o FROM OdgovornoTijelo o WHERE o.telefon = :telefon"),
    @NamedQuery(name = "OdgovornoTijelo.findByFax", query = "SELECT o FROM OdgovornoTijelo o WHERE o.fax = :fax"),
    @NamedQuery(name = "OdgovornoTijelo.findByEMail", query = "SELECT o FROM OdgovornoTijelo o WHERE o.eMail = :eMail"),
    @NamedQuery(name = "OdgovornoTijelo.findByInternetAdresa", query = "SELECT o FROM OdgovornoTijelo o WHERE o.internetAdresa = :internetAdresa")})
public class OdgovornoTijelo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "odgovorna_osoba", nullable = false, length = 255)
    private String odgovornaOsoba;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String adresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String telefon;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String fax;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "e_mail", length = 255)
    private String eMail;
    @Size(max = 255)
    @Column(name = "internet_adresa", length = 255)
    private String internetAdresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tijeloId")
    private Collection<Mreza> mrezaCollection;
    @OneToMany(mappedBy = "odgovornoTijeloId")
    private Collection<Postaja> postajaCollection;

    public OdgovornoTijelo() {
    }

    public OdgovornoTijelo(Integer id) {
        this.id = id;
    }

    public OdgovornoTijelo(Integer id, String naziv, String odgovornaOsoba, String adresa, String telefon, String fax) {
        this.id = id;
        this.naziv = naziv;
        this.odgovornaOsoba = odgovornaOsoba;
        this.adresa = adresa;
        this.telefon = telefon;
        this.fax = fax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOdgovornaOsoba() {
        return odgovornaOsoba;
    }

    public void setOdgovornaOsoba(String odgovornaOsoba) {
        this.odgovornaOsoba = odgovornaOsoba;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getInternetAdresa() {
        return internetAdresa;
    }

    public void setInternetAdresa(String internetAdresa) {
        this.internetAdresa = internetAdresa;
    }

    @XmlTransient
    public Collection<Mreza> getMrezaCollection() {
        return mrezaCollection;
    }

    public void setMrezaCollection(Collection<Mreza> mrezaCollection) {
        this.mrezaCollection = mrezaCollection;
    }

    @XmlTransient
    public Collection<Postaja> getPostajaCollection() {
        return postajaCollection;
    }

    public void setPostajaCollection(Collection<Postaja> postajaCollection) {
        this.postajaCollection = postajaCollection;
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
        if (!(object instanceof OdgovornoTijelo)) {
            return false;
        }
        OdgovornoTijelo other = (OdgovornoTijelo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.OdgovornoTijelo[ id=" + id + " ]";
    }
    
}
