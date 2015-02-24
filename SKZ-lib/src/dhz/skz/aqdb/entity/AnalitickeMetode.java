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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "analiticke_metode", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnalitickeMetode.findAll", query = "SELECT a FROM AnalitickeMetode a"),
    @NamedQuery(name = "AnalitickeMetode.findById", query = "SELECT a FROM AnalitickeMetode a WHERE a.id = :id"),
    @NamedQuery(name = "AnalitickeMetode.findByNaziv", query = "SELECT a FROM AnalitickeMetode a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "AnalitickeMetode.findByNorma", query = "SELECT a FROM AnalitickeMetode a WHERE a.norma = :norma"),
    @NamedQuery(name = "AnalitickeMetode.findByPonovljivostUNuli", query = "SELECT a FROM AnalitickeMetode a WHERE a.ponovljivostUNuli = :ponovljivostUNuli")})
public class AnalitickeMetode implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String naziv;
    @Size(max = 45)
    @Column(length = 45)
    private String norma;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ponovljivost_u_nuli", precision = 12)
    private Float ponovljivostUNuli;
    @Column(name = "zero_drift_absolut", precision = 12)
    private Float zeroDriftAbs;
    @Column(name = "span_drift_relativ", precision = 12)
    private Float spanDriftRelativ;


    @OneToMany(mappedBy = "analitickeMetodeId")
    private Collection<ModelUredjaja> modelUredjajaCollection;

    public AnalitickeMetode() {
    }

    public AnalitickeMetode(Integer id) {
        this.id = id;
    }

    public AnalitickeMetode(Integer id, String naziv) {
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

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public Float getPonovljivostUNuli() {
        return ponovljivostUNuli;
    }

    public void setPonovljivostUNuli(Float ponovljivostUNuli) {
        this.ponovljivostUNuli = ponovljivostUNuli;
    }
    
    public Float getSpanDriftRelativ() {
        return spanDriftRelativ;
    }

    public void setSpanDriftRelativ(Float spanDriftRelativ) {
        this.spanDriftRelativ = spanDriftRelativ;
    }

    public Float getZeroDriftAbs() {
        return zeroDriftAbs;
    }

    public void setZeroDriftAbs(Float zeroDriftAbs) {
        this.zeroDriftAbs = zeroDriftAbs;
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
        if (!(object instanceof AnalitickeMetode)) {
            return false;
        }
        AnalitickeMetode other = (AnalitickeMetode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.AnalitickeMetode[ id=" + id + " ]";
    }
    
}
