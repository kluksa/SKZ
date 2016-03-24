/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "izvor_program_kljucevi_map")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IzvorProgramKljuceviMap.findAll", query = "SELECT i FROM IzvorProgramKljuceviMap i"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByProgramMjerenjaId", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.programMjerenjaId = :programMjerenjaId"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByPKljuc", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.pKljuc = :pKljuc"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByKKljuc", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.kKljuc = :kKljuc"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByUKljuc", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.uKljuc = :uKljuc"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByIzvor", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.izvorPodataka = :izvor"),
    @NamedQuery(name = "IzvorProgramKljuceviMap.findByNKljuc", query = "SELECT i FROM IzvorProgramKljuceviMap i WHERE i.nKljuc = :nKljuc")})
public class IzvorProgramKljuceviMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "program_mjerenja_id")
    private Integer programMjerenjaId;
    @Size(max = 90)
    @Column(name = "p_kljuc")
    private String pKljuc;
    @Size(max = 90)
    @Column(name = "k_kljuc")
    private String kKljuc;
    @Size(max = 90)
    @Column(name = "u_kljuc")
    private String uKljuc;
    @Size(max = 90)
    @Column(name = "n_kljuc")
    private String nKljuc;
    @Column(name = "broj_u_satu")
    private Integer brojUSatu;
    @Column(name = "zasebni_zs")
    private Boolean zasebniZS;
    @JoinColumn(name = "program_mjerenja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramMjerenja programMjerenja;

    public IzvorProgramKljuceviMap() {
    }

    public IzvorProgramKljuceviMap(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Integer getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public String getPKljuc() {
        return pKljuc;
    }

    public void setPKljuc(String pKljuc) {
        this.pKljuc = pKljuc;
    }

    public String getKKljuc() {
        return kKljuc;
    }

    public void setKKljuc(String kKljuc) {
        this.kKljuc = kKljuc;
    }

    public String getUKljuc() {
        return uKljuc;
    }

    public void setUKljuc(String uKljuc) {
        this.uKljuc = uKljuc;
    }

    public String getNKljuc() {
        return nKljuc;
    }

    public void setNKljuc(String nKljuc) {
        this.nKljuc = nKljuc;
    }

    @XmlTransient
    public ProgramMjerenja getProgramMjerenja() {
        return programMjerenja;
    }

    public void setProgramMjerenja(ProgramMjerenja programMjerenja) {
        this.programMjerenja = programMjerenja;
    }

    public Integer getBrojUSatu() {
        return brojUSatu;
    }

    public void setBrojUSatu(Integer brojUSatu) {
        this.brojUSatu = brojUSatu;
    }

    public Boolean getZasebniZS() {
        return zasebniZS;
    }

    public void setZasebniZS(Boolean zasebniZS) {
        this.zasebniZS = zasebniZS;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programMjerenjaId != null ? programMjerenjaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IzvorProgramKljuceviMap)) {
            return false;
        }
        IzvorProgramKljuceviMap other = (IzvorProgramKljuceviMap) object;
        if ((this.programMjerenjaId == null && other.programMjerenjaId != null) || (this.programMjerenjaId != null && !this.programMjerenjaId.equals(other.programMjerenjaId))) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.IzvorProgramKljuceviMap[ programMjerenjaId=" + programMjerenjaId + " ]";
    }
    
}
