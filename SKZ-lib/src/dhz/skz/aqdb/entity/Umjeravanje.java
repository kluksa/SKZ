/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Umjeravanje.findAll", query = "SELECT u FROM Umjeravanje u"),
    @NamedQuery(name = "Umjeravanje.findById", query = "SELECT u FROM Umjeravanje u WHERE u.id = :id"),
    @NamedQuery(name = "Umjeravanje.findByDatum", query = "SELECT u FROM Umjeravanje u WHERE u.datum = :datum"),
    @NamedQuery(name = "Umjeravanje.findByOznakaUmjernice", query = "SELECT u FROM Umjeravanje u WHERE u.oznakaUmjernice = :oznakaUmjernice"),
    @NamedQuery(name = "Umjeravanje.findBySlope", query = "SELECT u FROM Umjeravanje u WHERE u.slope = :slope"),
    @NamedQuery(name = "Umjeravanje.findByOffset", query = "SELECT u FROM Umjeravanje u WHERE u.offset = :offset")})
public class Umjeravanje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "oznaka_umjernice", nullable = false, length = 45)
    private String oznakaUmjernice;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private float slope;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private float offset;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "umjeravanje")
    private UmjerniKoeficijenti umjerniKoeficijenti;
    @JoinColumn(name = "umjerni_laboratorij_id", referencedColumnName = "id")
    @ManyToOne
    private UmjerniLaboratorij umjerniLaboratorijId;
    @JoinColumn(name = "mjeritelj_id", referencedColumnName = "id")
    @ManyToOne
    private Korisnik mjeriteljId;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Komponenta komponentaId;
    @JoinColumn(name = "uredjaj_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjajId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "umjeravanje")
    private UmjeravanjeKomentar umjeravanjeKomentar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "umjeravanjeId")
    private Collection<UmjerneTocke> umjerneTockeCollection;

    public Umjeravanje() {
    }

    public Umjeravanje(Integer id) {
        this.id = id;
    }

    public Umjeravanje(Integer id, Date datum, String oznakaUmjernice, float slope, float offset) {
        this.id = id;
        this.datum = datum;
        this.oznakaUmjernice = oznakaUmjernice;
        this.slope = slope;
        this.offset = offset;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getOznakaUmjernice() {
        return oznakaUmjernice;
    }

    public void setOznakaUmjernice(String oznakaUmjernice) {
        this.oznakaUmjernice = oznakaUmjernice;
    }

    public float getSlope() {
        return slope;
    }

    public void setSlope(float slope) {
        this.slope = slope;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public UmjerniKoeficijenti getUmjerniKoeficijenti() {
        return umjerniKoeficijenti;
    }

    public void setUmjerniKoeficijenti(UmjerniKoeficijenti umjerniKoeficijenti) {
        this.umjerniKoeficijenti = umjerniKoeficijenti;
    }

    public UmjerniLaboratorij getUmjerniLaboratorijId() {
        return umjerniLaboratorijId;
    }

    public void setUmjerniLaboratorijId(UmjerniLaboratorij umjerniLaboratorijId) {
        this.umjerniLaboratorijId = umjerniLaboratorijId;
    }

    public Korisnik getMjeriteljId() {
        return mjeriteljId;
    }

    public void setMjeriteljId(Korisnik mjeriteljId) {
        this.mjeriteljId = mjeriteljId;
    }

    public Komponenta getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(Komponenta komponentaId) {
        this.komponentaId = komponentaId;
    }

    public Uredjaj getUredjajId() {
        return uredjajId;
    }

    public void setUredjajId(Uredjaj uredjajId) {
        this.uredjajId = uredjajId;
    }

    public UmjeravanjeKomentar getUmjeravanjeKomentar() {
        return umjeravanjeKomentar;
    }

    public void setUmjeravanjeKomentar(UmjeravanjeKomentar umjeravanjeKomentar) {
        this.umjeravanjeKomentar = umjeravanjeKomentar;
    }

    @XmlTransient
    public Collection<UmjerneTocke> getUmjerneTockeCollection() {
        return umjerneTockeCollection;
    }

    public void setUmjerneTockeCollection(Collection<UmjerneTocke> umjerneTockeCollection) {
        this.umjerneTockeCollection = umjerneTockeCollection;
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
        if (!(object instanceof Umjeravanje)) {
            return false;
        }
        Umjeravanje other = (Umjeravanje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Umjeravanje[ id=" + id + " ]";
    }
    
}
