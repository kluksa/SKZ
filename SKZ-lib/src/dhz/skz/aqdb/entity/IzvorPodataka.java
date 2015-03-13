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
@Table(name = "izvor_podataka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IzvorPodataka.findAll", query = "SELECT i FROM IzvorPodataka i"),
    @NamedQuery(name = "IzvorPodataka.findById", query = "SELECT i FROM IzvorPodataka i WHERE i.id = :id"),
    @NamedQuery(name = "IzvorPodataka.findByNaziv", query = "SELECT i FROM IzvorPodataka i WHERE i.naziv = :naziv"),
    @NamedQuery(name = "IzvorPodataka.findByUri", query = "SELECT i FROM IzvorPodataka i WHERE i.uri = :uri"),
    @NamedQuery(name = "IzvorPodataka.findByBean", query = "SELECT i FROM IzvorPodataka i WHERE i.bean = :bean"),
    @NamedQuery(name = "IzvorPodataka.findByAktivan", query = "SELECT i FROM IzvorPodataka i WHERE i.aktivan = :aktivan")})
public class IzvorPodataka implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    private String naziv;
    @Size(max = 150)
    private String uri;
    @Size(max = 250)
    private String bean;
    @Basic(optional = false)
    @NotNull
    private boolean aktivan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "izvorPodataka")
    private Collection<ValidatorModelIzvor> validatorModelIzvorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "izvorPodatakaId")
    private Collection<ProgramMjerenja> programMjerenjaCollection;

    public IzvorPodataka() {
    }

    public IzvorPodataka(Integer id) {
        this.id = id;
    }

    public IzvorPodataka(Integer id, String naziv, boolean aktivan) {
        this.id = id;
        this.naziv = naziv;
        this.aktivan = aktivan;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    @XmlTransient
    public Collection<ValidatorModelIzvor> getValidatorModelIzvorCollection() {
        return validatorModelIzvorCollection;
    }

    public void setValidatorModelIzvorCollection(Collection<ValidatorModelIzvor> validatorModelIzvorCollection) {
        this.validatorModelIzvorCollection = validatorModelIzvorCollection;
    }

    @XmlTransient
    public Collection<ProgramMjerenja> getProgramMjerenjaCollection() {
        return programMjerenjaCollection;
    }

    public void setProgramMjerenjaCollection(Collection<ProgramMjerenja> programMjerenjaCollection) {
        this.programMjerenjaCollection = programMjerenjaCollection;
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
        if (!(object instanceof IzvorPodataka)) {
            return false;
        }
        IzvorPodataka other = (IzvorPodataka) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.IzvorPodataka[ id=" + id + " ]";
    }
    
}
