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
@Table(name = "validatori", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Validatori.findAll", query = "SELECT v FROM Validatori v"),
    @NamedQuery(name = "Validatori.findById", query = "SELECT v FROM Validatori v WHERE v.id = :id"),
    @NamedQuery(name = "Validatori.findByNaziv", query = "SELECT v FROM Validatori v WHERE v.naziv = :naziv")})
public class Validatori implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "naziv")
    private String naziv;
    @OneToMany(mappedBy = "validatorId")
    private Collection<ModelUredjaja> modelUredjajaCollection;

    public Validatori() {
    }

    public Validatori(Integer id) {
        this.id = id;
    }

    public Validatori(Integer id, String naziv) {
        this.id = id;
        this.naziv = naziv;
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

    @XmlTransient
    public Collection<ModelUredjaja> getModelUredjajaCollection() {
        return modelUredjajaCollection;
    }

    public void setModelUredjajaCollection(Collection<ModelUredjaja> modelUredjajaCollection) {
        this.modelUredjajaCollection = modelUredjajaCollection;
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
        if (!(object instanceof Validatori)) {
            return false;
        }
        Validatori other = (Validatori) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Validatori[ id=" + id + " ]";
    }

}
