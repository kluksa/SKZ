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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "snap_sektori", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SnapSektori.findAll", query = "SELECT s FROM SnapSektori s"),
    @NamedQuery(name = "SnapSektori.findById", query = "SELECT s FROM SnapSektori s WHERE s.id = :id"),
    @NamedQuery(name = "SnapSektori.findByAsociranaOznaka", query = "SELECT s FROM SnapSektori s WHERE s.asociranaOznaka = :asociranaOznaka"),
    @NamedQuery(name = "SnapSektori.findBySektor", query = "SELECT s FROM SnapSektori s WHERE s.sektor = :sektor"),
    @NamedQuery(name = "SnapSektori.findByNaziv", query = "SELECT s FROM SnapSektori s WHERE s.naziv = :naziv")})
public class SnapSektori implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asocirana_oznaka", nullable = false)
    private int asociranaOznaka;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private short sektor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String naziv;
    @ManyToMany(mappedBy = "snapSektoriCollection")
    private Collection<IndustrijskePostajeSvojstva> industrijskePostajeSvojstvaCollection;

    public SnapSektori() {
    }

    public SnapSektori(Integer id) {
        this.id = id;
    }

    public SnapSektori(Integer id, int asociranaOznaka, short sektor, String naziv) {
        this.id = id;
        this.asociranaOznaka = asociranaOznaka;
        this.sektor = sektor;
        this.naziv = naziv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAsociranaOznaka() {
        return asociranaOznaka;
    }

    public void setAsociranaOznaka(int asociranaOznaka) {
        this.asociranaOznaka = asociranaOznaka;
    }

    public short getSektor() {
        return sektor;
    }

    public void setSektor(short sektor) {
        this.sektor = sektor;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public Collection<IndustrijskePostajeSvojstva> getIndustrijskePostajeSvojstvaCollection() {
        return industrijskePostajeSvojstvaCollection;
    }

    public void setIndustrijskePostajeSvojstvaCollection(Collection<IndustrijskePostajeSvojstva> industrijskePostajeSvojstvaCollection) {
        this.industrijskePostajeSvojstvaCollection = industrijskePostajeSvojstvaCollection;
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
        if (!(object instanceof SnapSektori)) {
            return false;
        }
        SnapSektori other = (SnapSektori) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.SnapSektori[ id=" + id + " ]";
    }
    
}
