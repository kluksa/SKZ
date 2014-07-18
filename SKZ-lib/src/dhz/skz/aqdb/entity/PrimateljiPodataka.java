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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "primatelji_podataka", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrimateljiPodataka.findAll", query = "SELECT p FROM PrimateljiPodataka p"),
    @NamedQuery(name = "PrimateljiPodataka.findById", query = "SELECT p FROM PrimateljiPodataka p WHERE p.id = :id"),
    @NamedQuery(name = "PrimateljiPodataka.findByNaziv", query = "SELECT p FROM PrimateljiPodataka p WHERE p.naziv = :naziv"),
    @NamedQuery(name = "PrimateljiPodataka.findByUrl", query = "SELECT p FROM PrimateljiPodataka p WHERE p.url = :url"),
    @NamedQuery(name = "PrimateljiPodataka.findByTip", query = "SELECT p FROM PrimateljiPodataka p WHERE p.tip = :tip"),
    @NamedQuery(name = "PrimateljiPodataka.findByXsd", query = "SELECT p FROM PrimateljiPodataka p WHERE p.xsd = :xsd"),
    @NamedQuery(name = "PrimateljiPodataka.findByAktivan", query = "SELECT p FROM PrimateljiPodataka p WHERE p.aktivan = :aktivan"),
    @NamedQuery(name = "PrimateljiPodataka.findByCestinaSati", query = "SELECT p FROM PrimateljiPodataka p WHERE p.cestinaSati = :cestinaSati")})
public class PrimateljiPodataka implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "naziv")
    private String naziv;
    @Column(name = "url")
    private String url;
    @Column(name = "tip")
    private String tip;
    @Column(name = "xsd")
    private String xsd;
    @Column(name = "aktivan")
    private Short aktivan;
    @Column(name = "cestina_sati")
    private Integer cestinaSati;
    @JoinTable(name = "primatelji_podataka_has_program_mjerenja", joinColumns = {
        @JoinColumn(name = "primatelji_podataka_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ProgramMjerenja> programMjerenjaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "primateljiPodataka")
    private Collection<PrimateljProgramKljuceviMap> primateljProgramKljuceviMapCollection;
    @JoinColumn(name = "mreza_id", referencedColumnName = "id")
    @ManyToOne
    private Mreza mrezaId;

    public PrimateljiPodataka() {
    }

    public PrimateljiPodataka(Integer id) {
        this.id = id;
    }

    public PrimateljiPodataka(Integer id, String naziv) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    public Short getAktivan() {
        return aktivan;
    }

    public void setAktivan(Short aktivan) {
        this.aktivan = aktivan;
    }

    public Integer getCestinaSati() {
        return cestinaSati;
    }

    public void setCestinaSati(Integer cestinaSati) {
        this.cestinaSati = cestinaSati;
    }

    @XmlTransient
    public Collection<ProgramMjerenja> getProgramMjerenjaCollection() {
        return programMjerenjaCollection;
    }

    public void setProgramMjerenjaCollection(Collection<ProgramMjerenja> programMjerenjaCollection) {
        this.programMjerenjaCollection = programMjerenjaCollection;
    }

    @XmlTransient
    public Collection<PrimateljProgramKljuceviMap> getPrimateljProgramKljuceviMapCollection() {
        return primateljProgramKljuceviMapCollection;
    }

    public void setPrimateljProgramKljuceviMapCollection(Collection<PrimateljProgramKljuceviMap> primateljProgramKljuceviMapCollection) {
        this.primateljProgramKljuceviMapCollection = primateljProgramKljuceviMapCollection;
    }

    public Mreza getMrezaId() {
        return mrezaId;
    }

    public void setMrezaId(Mreza mrezaId) {
        this.mrezaId = mrezaId;
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
        if (!(object instanceof PrimateljiPodataka)) {
            return false;
        }
        PrimateljiPodataka other = (PrimateljiPodataka) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PrimateljiPodataka[ id=" + id + " ]";
    }
    
}
