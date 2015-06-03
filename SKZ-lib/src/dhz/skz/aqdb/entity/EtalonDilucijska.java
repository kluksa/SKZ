/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "etalon_dilucijska")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtalonDilucijska.findAll", query = "SELECT e FROM EtalonDilucijska e"),
    @NamedQuery(name = "EtalonDilucijska.findByOpremaId", query = "SELECT e FROM EtalonDilucijska e WHERE e.etalonDilucijskaPK.opremaId = :opremaId"),
    @NamedQuery(name = "EtalonDilucijska.findByPrimjenaOd", query = "SELECT e FROM EtalonDilucijska e WHERE e.etalonDilucijskaPK.primjenaOd = :primjenaOd"),
    @NamedQuery(name = "EtalonDilucijska.findByMfcsNesigurnost", query = "SELECT e FROM EtalonDilucijska e WHERE e.mfcsNesigurnost = :mfcsNesigurnost"),
    @NamedQuery(name = "EtalonDilucijska.findByMfcsSljedivost", query = "SELECT e FROM EtalonDilucijska e WHERE e.mfcsSljedivost = :mfcsSljedivost"),
    @NamedQuery(name = "EtalonDilucijska.findByMfczNesigurnost", query = "SELECT e FROM EtalonDilucijska e WHERE e.mfczNesigurnost = :mfczNesigurnost"),
    @NamedQuery(name = "EtalonDilucijska.findByMfczSljedivost", query = "SELECT e FROM EtalonDilucijska e WHERE e.mfczSljedivost = :mfczSljedivost")})
public class EtalonDilucijska implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EtalonDilucijskaPK etalonDilucijskaPK;
    @Size(max = 90)
    @Column(name = "mfcs_nesigurnost")
    private String mfcsNesigurnost;
    @Size(max = 90)
    @Column(name = "mfcs_sljedivost")
    private String mfcsSljedivost;
    @Size(max = 90)
    @Column(name = "mfcz_nesigurnost")
    private String mfczNesigurnost;
    @Size(max = 90)
    @Column(name = "mfcz_sljedivost")
    private String mfczSljedivost;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjaj;

    public EtalonDilucijska() {
    }

    public EtalonDilucijska(EtalonDilucijskaPK etalonDilucijskaPK) {
        this.etalonDilucijskaPK = etalonDilucijskaPK;
    }

    public EtalonDilucijska(int opremaId, Date primjenaOd) {
        this.etalonDilucijskaPK = new EtalonDilucijskaPK(opremaId, primjenaOd);
    }

    public EtalonDilucijskaPK getEtalonDilucijskaPK() {
        return etalonDilucijskaPK;
    }

    public void setEtalonDilucijskaPK(EtalonDilucijskaPK etalonDilucijskaPK) {
        this.etalonDilucijskaPK = etalonDilucijskaPK;
    }

    public String getMfcsNesigurnost() {
        return mfcsNesigurnost;
    }

    public void setMfcsNesigurnost(String mfcsNesigurnost) {
        this.mfcsNesigurnost = mfcsNesigurnost;
    }

    public String getMfcsSljedivost() {
        return mfcsSljedivost;
    }

    public void setMfcsSljedivost(String mfcsSljedivost) {
        this.mfcsSljedivost = mfcsSljedivost;
    }

    public String getMfczNesigurnost() {
        return mfczNesigurnost;
    }

    public void setMfczNesigurnost(String mfczNesigurnost) {
        this.mfczNesigurnost = mfczNesigurnost;
    }

    public String getMfczSljedivost() {
        return mfczSljedivost;
    }

    public void setMfczSljedivost(String mfczSljedivost) {
        this.mfczSljedivost = mfczSljedivost;
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
        hash += (etalonDilucijskaPK != null ? etalonDilucijskaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtalonDilucijska)) {
            return false;
        }
        EtalonDilucijska other = (EtalonDilucijska) object;
        if ((this.etalonDilucijskaPK == null && other.etalonDilucijskaPK != null) || (this.etalonDilucijskaPK != null && !this.etalonDilucijskaPK.equals(other.etalonDilucijskaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.EtalonDilucijska[ etalonDilucijskaPK=" + etalonDilucijskaPK + " ]";
    }
    
}
