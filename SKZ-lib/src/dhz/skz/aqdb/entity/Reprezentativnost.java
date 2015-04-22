/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
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
    @NamedQuery(name = "Reprezentativnost.findAll", query = "SELECT r FROM Reprezentativnost r"),
    @NamedQuery(name = "Reprezentativnost.findById", query = "SELECT r FROM Reprezentativnost r WHERE r.id = :id"),
    @NamedQuery(name = "Reprezentativnost.findByAsociranaOznaka", query = "SELECT r FROM Reprezentativnost r WHERE r.asociranaOznaka = :asociranaOznaka"),
    @NamedQuery(name = "Reprezentativnost.findByDefinicija", query = "SELECT r FROM Reprezentativnost r WHERE r.definicija = :definicija")})
public class Reprezentativnost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "asocirana_oznaka")
    private String asociranaOznaka;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    private String definicija;
    @OneToMany(mappedBy = "reprezentativnostId")
    private Collection<Postaja> postajaCollection;
    @JoinColumn(name = "podrucje_id", referencedColumnName = "id")
    @ManyToOne
    private Podrucje podrucjeId;

    public Reprezentativnost() {
    }

    public Reprezentativnost(Integer id) {
        this.id = id;
    }

    public Reprezentativnost(Integer id, String asociranaOznaka, String definicija) {
        this.id = id;
        this.asociranaOznaka = asociranaOznaka;
        this.definicija = definicija;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAsociranaOznaka() {
        return asociranaOznaka;
    }

    public void setAsociranaOznaka(String asociranaOznaka) {
        this.asociranaOznaka = asociranaOznaka;
    }

    public String getDefinicija() {
        return definicija;
    }

    public void setDefinicija(String definicija) {
        this.definicija = definicija;
    }

    @XmlTransient
    public Collection<Postaja> getPostajaCollection() {
        return postajaCollection;
    }

    public void setPostajaCollection(Collection<Postaja> postajaCollection) {
        this.postajaCollection = postajaCollection;
    }

    public Podrucje getPodrucjeId() {
        return podrucjeId;
    }

    public void setPodrucjeId(Podrucje podrucjeId) {
        this.podrucjeId = podrucjeId;
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
        if (!(object instanceof Reprezentativnost)) {
            return false;
        }
        Reprezentativnost other = (Reprezentativnost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Reprezentativnost[ id=" + id + " ]";
    }
    
}
