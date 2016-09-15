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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Granice.findAll", query = "SELECT g FROM Granice g"),
    @NamedQuery(name = "Granice.findById", query = "SELECT g FROM Granice g WHERE g.id = :id"),
    @NamedQuery(name = "Granice.findByVrijednost", query = "SELECT g FROM Granice g WHERE g.vrijednost = :vrijednost"),
    @NamedQuery(name = "Granice.findByDozvoljeniBrojPrekoracenja", query = "SELECT g FROM Granice g WHERE g.dozvoljeniBrojPrekoracenja = :dozvoljeniBrojPrekoracenja"),
    @NamedQuery(name = "Granice.findByPocetakPrimjene", query = "SELECT g FROM Granice g WHERE g.pocetakPrimjene = :pocetakPrimjene"),
    @NamedQuery(name = "Granice.findByKrajPrimjene", query = "SELECT g FROM Granice g WHERE g.krajPrimjene = :krajPrimjene")})
public class Granice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    private double vrijednost;
    @Column(name = "dozvoljeni_broj_prekoracenja")
    private Integer dozvoljeniBrojPrekoracenja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pocetak_primjene")
    @Temporal(TemporalType.DATE)
    private Date pocetakPrimjene;
    @Column(name = "kraj_primjene")
    @Temporal(TemporalType.DATE)
    private Date krajPrimjene;
    @JoinColumn(name = "agregacije_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Agregacije agregacijeId;
    @JoinColumn(name = "kategorije_granica_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private KategorijeGranica kategorijeGranicaId;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Komponenta komponentaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "komponenta")
    private Collection<Obavijesti> obavijesti;


    public Granice() {
    }

    public Granice(Integer id) {
        this.id = id;
    }

    public Granice(Integer id, double vrijednost, Date pocetakPrimjene) {
        this.id = id;
        this.vrijednost = vrijednost;
        this.pocetakPrimjene = pocetakPrimjene;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public Integer getDozvoljeniBrojPrekoracenja() {
        return dozvoljeniBrojPrekoracenja;
    }

    public void setDozvoljeniBrojPrekoracenja(Integer dozvoljeniBrojPrekoracenja) {
        this.dozvoljeniBrojPrekoracenja = dozvoljeniBrojPrekoracenja;
    }

    public Date getPocetakPrimjene() {
        return pocetakPrimjene;
    }

    public void setPocetakPrimjene(Date pocetakPrimjene) {
        this.pocetakPrimjene = pocetakPrimjene;
    }

    public Date getKrajPrimjene() {
        return krajPrimjene;
    }

    public void setKrajPrimjene(Date krajPrimjene) {
        this.krajPrimjene = krajPrimjene;
    }

    public Agregacije getAgregacijeId() {
        return agregacijeId;
    }

    public void setAgregacijeId(Agregacije agregacijeId) {
        this.agregacijeId = agregacijeId;
    }

    public KategorijeGranica getKategorijeGranicaId() {
        return kategorijeGranicaId;
    }

    public void setKategorijeGranicaId(KategorijeGranica kategorijeGranicaId) {
        this.kategorijeGranicaId = kategorijeGranicaId;
    }

    public Komponenta getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(Komponenta komponentaId) {
        this.komponentaId = komponentaId;
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
        if (!(object instanceof Granice)) {
            return false;
        }
        Granice other = (Granice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Collection<Obavijesti> getObavijesti() {
        return obavijesti;
    }

    public void setObavijesti(Collection<Obavijesti> obavijesti) {
        this.obavijesti = obavijesti;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Granice[ id=" + id + " ]";
    }
    
}
