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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kvarovi.findAll", query = "SELECT k FROM Kvarovi k"),
    @NamedQuery(name = "Kvarovi.findById", query = "SELECT k FROM Kvarovi k WHERE k.id = :id"),
    @NamedQuery(name = "Kvarovi.findByDatum", query = "SELECT k FROM Kvarovi k WHERE k.datum = :datum"),
    @NamedQuery(name = "Kvarovi.findByOpisKvara", query = "SELECT k FROM Kvarovi k WHERE k.opisKvara = :opisKvara")})
public class Kvarovi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis_kvara")
    private String opisKvara;
    @JoinColumn(name = "odrzavanje_id", referencedColumnName = "id")
    @ManyToOne
    private Odrzavanje odrzavanjeId;
    @JoinColumn(name = "uredjaj_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uredjaj uredjajId;

    public Kvarovi() {
    }

    public Kvarovi(Integer id) {
        this.id = id;
    }

    public Kvarovi(Integer id, Date datum, String opisKvara) {
        this.id = id;
        this.datum = datum;
        this.opisKvara = opisKvara;
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

    public String getOpisKvara() {
        return opisKvara;
    }

    public void setOpisKvara(String opisKvara) {
        this.opisKvara = opisKvara;
    }

    public Odrzavanje getOdrzavanjeId() {
        return odrzavanjeId;
    }

    public void setOdrzavanjeId(Odrzavanje odrzavanjeId) {
        this.odrzavanjeId = odrzavanjeId;
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
        if (!(object instanceof Kvarovi)) {
            return false;
        }
        Kvarovi other = (Kvarovi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Kvarovi[ id=" + id + " ]";
    }
    
}
