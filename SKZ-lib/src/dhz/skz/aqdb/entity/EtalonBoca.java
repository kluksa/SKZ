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

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "etalon_boca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtalonBoca.findAll", query = "SELECT e FROM EtalonBoca e"),
    @NamedQuery(name = "EtalonBoca.findByOpremaId", query = "SELECT e FROM EtalonBoca e WHERE e.opremaId = :opremaId"),
    @NamedQuery(name = "EtalonBoca.findByKoncentracija", query = "SELECT e FROM EtalonBoca e WHERE e.koncentracija = :koncentracija"),
    @NamedQuery(name = "EtalonBoca.findByNesigurnost", query = "SELECT e FROM EtalonBoca e WHERE e.nesigurnost = :nesigurnost"),
    @NamedQuery(name = "EtalonBoca.findBySljedivost", query = "SELECT e FROM EtalonBoca e WHERE e.sljedivost = :sljedivost")})
public class EtalonBoca implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "oprema_id")
    private Integer opremaId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Float koncentracija;
    private Float nesigurnost;
    @Size(max = 45)
    private String sljedivost;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Uredjaj uredjaj;

    public EtalonBoca() {
    }

    public EtalonBoca(Integer opremaId) {
        this.opremaId = opremaId;
    }

    public Integer getOpremaId() {
        return opremaId;
    }

    public void setOpremaId(Integer opremaId) {
        this.opremaId = opremaId;
    }

    public Float getKoncentracija() {
        return koncentracija;
    }

    public void setKoncentracija(Float koncentracija) {
        this.koncentracija = koncentracija;
    }

    public Float getNesigurnost() {
        return nesigurnost;
    }

    public void setNesigurnost(Float nesigurnost) {
        this.nesigurnost = nesigurnost;
    }

    public String getSljedivost() {
        return sljedivost;
    }

    public void setSljedivost(String sljedivost) {
        this.sljedivost = sljedivost;
    }

    public MjerneJedinice getMjerneJediniceId() {
        return mjerneJediniceId;
    }

    public void setMjerneJediniceId(MjerneJedinice mjerneJediniceId) {
        this.mjerneJediniceId = mjerneJediniceId;
    }

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opremaId != null ? opremaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonBoca)) {
            return false;
        }
        EtalonBoca other = (EtalonBoca) object;
        if ((this.opremaId == null && other.opremaId != null) || (this.opremaId != null && !this.opremaId.equals(other.opremaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonBoca[ opremaId=" + opremaId + " ]";
    }
    
}
