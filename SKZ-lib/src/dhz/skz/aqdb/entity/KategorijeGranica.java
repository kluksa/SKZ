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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "kategorije_granica", catalog = "aqdb_likz", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"oznaka"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KategorijeGranica.findAll", query = "SELECT k FROM KategorijeGranica k"),
    @NamedQuery(name = "KategorijeGranica.findById", query = "SELECT k FROM KategorijeGranica k WHERE k.id = :id"),
    @NamedQuery(name = "KategorijeGranica.findByOznaka", query = "SELECT k FROM KategorijeGranica k WHERE k.oznaka = :oznaka"),
    @NamedQuery(name = "KategorijeGranica.findByOpis", query = "SELECT k FROM KategorijeGranica k WHERE k.opis = :opis")})
public class KategorijeGranica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Size(max = 45)
    @Column(length = 45)
    private String oznaka;
    @Size(max = 300)
    @Column(length = 300)
    private String opis;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kategorijeGranicaId")
    private Collection<Granice> graniceCollection;

    public KategorijeGranica() {
    }

    public KategorijeGranica(Integer id) {
        this.id = id;
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
    public Collection<Granice> getGraniceCollection() {
        return graniceCollection;
    }

    public void setGraniceCollection(Collection<Granice> graniceCollection) {
        this.graniceCollection = graniceCollection;
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
        if (!(object instanceof KategorijeGranica)) {
            return false;
        }
        KategorijeGranica other = (KategorijeGranica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.KategorijeGranica[ id=" + id + " ]";
    }
    
}
