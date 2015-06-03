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
@Table(name = "iskaz_vremena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IskazVremena.findAll", query = "SELECT i FROM IskazVremena i"),
    @NamedQuery(name = "IskazVremena.findById", query = "SELECT i FROM IskazVremena i WHERE i.id = :id"),
    @NamedQuery(name = "IskazVremena.findByTekstDefinicija", query = "SELECT i FROM IskazVremena i WHERE i.tekstDefinicija = :tekstDefinicija")})
public class IskazVremena implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "tekst_definicija")
    private String tekstDefinicija;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iskazVremenaId")
    private Collection<Mreza> mrezaCollection;

    public IskazVremena() {
    }

    public IskazVremena(Integer id) {
        this.id = id;
    }

    public IskazVremena(Integer id, String tekstDefinicija) {
        this.id = id;
        this.tekstDefinicija = tekstDefinicija;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTekstDefinicija() {
        return tekstDefinicija;
    }

    public void setTekstDefinicija(String tekstDefinicija) {
        this.tekstDefinicija = tekstDefinicija;
    }

    @XmlTransient
    public Collection<Mreza> getMrezaCollection() {
        return mrezaCollection;
    }

    public void setMrezaCollection(Collection<Mreza> mrezaCollection) {
        this.mrezaCollection = mrezaCollection;
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
        if (!(object instanceof IskazVremena)) {
            return false;
        }
        IskazVremena other = (IskazVremena) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.IskazVremena[ id=" + id + " ]";
    }
    
}
