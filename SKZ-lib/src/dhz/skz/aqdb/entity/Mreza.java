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
    @NamedQuery(name = "Mreza.findAll", query = "SELECT m FROM Mreza m"),
    @NamedQuery(name = "Mreza.findById", query = "SELECT m FROM Mreza m WHERE m.id = :id"),
    @NamedQuery(name = "Mreza.findByNaziv", query = "SELECT m FROM Mreza m WHERE m.naziv = :naziv"),
    @NamedQuery(name = "Mreza.findByKratica", query = "SELECT m FROM Mreza m WHERE m.kratica = :kratica")})
public class Mreza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    private String naziv;
    @Size(max = 90)
    private String kratica;
    @OneToMany(mappedBy = "mrezaId")
    private Collection<PrimateljiPodataka> primateljiPodatakaCollection;
    @JoinColumn(name = "iskaz_vremena_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IskazVremena iskazVremenaId;
    @JoinColumn(name = "tijelo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OdgovornoTijelo tijeloId;
    @JoinColumn(name = "vrsta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VrstaMreze vrstaId;

    public Mreza() {
    }

    public Mreza(Integer id) {
        this.id = id;
    }

    public Mreza(Integer id, String naziv) {
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

    public String getKratica() {
        return kratica;
    }

    public void setKratica(String kratica) {
        this.kratica = kratica;
    }

    @XmlTransient
    public Collection<PrimateljiPodataka> getPrimateljiPodatakaCollection() {
        return primateljiPodatakaCollection;
    }

    public void setPrimateljiPodatakaCollection(Collection<PrimateljiPodataka> primateljiPodatakaCollection) {
        this.primateljiPodatakaCollection = primateljiPodatakaCollection;
    }

    public IskazVremena getIskazVremenaId() {
        return iskazVremenaId;
    }

    public void setIskazVremenaId(IskazVremena iskazVremenaId) {
        this.iskazVremenaId = iskazVremenaId;
    }

    public OdgovornoTijelo getTijeloId() {
        return tijeloId;
    }

    public void setTijeloId(OdgovornoTijelo tijeloId) {
        this.tijeloId = tijeloId;
    }

    public VrstaMreze getVrstaId() {
        return vrstaId;
    }

    public void setVrstaId(VrstaMreze vrstaId) {
        this.vrstaId = vrstaId;
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
        if (!(object instanceof Mreza)) {
            return false;
        }
        Mreza other = (Mreza) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Mreza[ id=" + id + " ]";
    }
    
}
