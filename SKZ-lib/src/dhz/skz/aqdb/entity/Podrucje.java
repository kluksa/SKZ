/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Podrucje.findAll", query = "SELECT p FROM Podrucje p"),
    @NamedQuery(name = "Podrucje.findById", query = "SELECT p FROM Podrucje p WHERE p.id = :id"),
    @NamedQuery(name = "Podrucje.findByOznaka", query = "SELECT p FROM Podrucje p WHERE p.oznaka = :oznaka"),
    @NamedQuery(name = "Podrucje.findByOpis", query = "SELECT p FROM Podrucje p WHERE p.opis = :opis")})
public class Podrucje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    private String oznaka;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    private String opis;
    @OneToMany(mappedBy = "podrucjeId")
    private Collection<Postaja> postajaCollection;
    @OneToMany(mappedBy = "podrucjeId")
    private Collection<Reprezentativnost> reprezentativnostCollection;
    @OneToMany(mappedBy = "nadredjenoPodrucjeId")
    private Collection<Podrucje> podrucjeCollection;
    @JoinColumn(name = "nadredjeno_podrucje_id", referencedColumnName = "id")
    @ManyToOne
    private Podrucje nadredjenoPodrucjeId;

    public Podrucje() {
    }

    public Podrucje(Integer id) {
        this.id = id;
    }

    public Podrucje(Integer id, String oznaka, String opis) {
        this.id = id;
        this.oznaka = oznaka;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @XmlTransient
    public Collection<Postaja> getPostajaCollection() {
        return postajaCollection;
    }

    public void setPostajaCollection(Collection<Postaja> postajaCollection) {
        this.postajaCollection = postajaCollection;
    }

    @XmlTransient
    public Collection<Reprezentativnost> getReprezentativnostCollection() {
        return reprezentativnostCollection;
    }

    public void setReprezentativnostCollection(Collection<Reprezentativnost> reprezentativnostCollection) {
        this.reprezentativnostCollection = reprezentativnostCollection;
    }

    @XmlTransient
    public Collection<Podrucje> getPodrucjeCollection() {
        return podrucjeCollection;
    }

    public void setPodrucjeCollection(Collection<Podrucje> podrucjeCollection) {
        this.podrucjeCollection = podrucjeCollection;
    }

    public Podrucje getNadredjenoPodrucjeId() {
        return nadredjenoPodrucjeId;
    }

    public void setNadredjenoPodrucjeId(Podrucje nadredjenoPodrucjeId) {
        this.nadredjenoPodrucjeId = nadredjenoPodrucjeId;
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
        if (!(object instanceof Podrucje)) {
            return false;
        }
        Podrucje other = (Podrucje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Podrucje[ id=" + id + " ]";
    }
    
}
