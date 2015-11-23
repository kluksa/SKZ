/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "metoda_umjerne_tocke")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetodaUmjerneTocke.findAll", query = "SELECT m FROM MetodaUmjerneTocke m"),
    @NamedQuery(name = "MetodaUmjerneTocke.findByMetodaId", query = "SELECT m FROM MetodaUmjerneTocke m WHERE m.metodaUmjerneTockePK.metodaId = :metodaId"),
    @NamedQuery(name = "MetodaUmjerneTocke.findByTockaNum", query = "SELECT m FROM MetodaUmjerneTocke m WHERE m.metodaUmjerneTockePK.tockaNum = :tockaNum"),
    @NamedQuery(name = "MetodaUmjerneTocke.findByPostotakOpsega", query = "SELECT m FROM MetodaUmjerneTocke m WHERE m.postotakOpsega = :postotakOpsega"),
    @NamedQuery(name = "MetodaUmjerneTocke.findByBrojMjerenja", query = "SELECT m FROM MetodaUmjerneTocke m WHERE m.brojMjerenja = :brojMjerenja"),
    @NamedQuery(name = "MetodaUmjerneTocke.findByBrojUSrednjaku", query = "SELECT m FROM MetodaUmjerneTocke m WHERE m.brojUSrednjaku = :brojUSrednjaku")})
public class MetodaUmjerneTocke implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetodaUmjerneTockePK metodaUmjerneTockePK;
    @Column(name = "postotak_opsega")
    private Integer postotakOpsega;
    @Column(name = "broj_mjerenja")
    private Integer brojMjerenja;
    @Column(name = "broj_u_srednjaku")
    private Integer brojUSrednjaku;
    @JoinColumn(name = "metoda_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AnalitickeMetode analitickeMetode;

    public MetodaUmjerneTocke() {
    }

    public MetodaUmjerneTocke(MetodaUmjerneTockePK metodaUmjerneTockePK) {
        this.metodaUmjerneTockePK = metodaUmjerneTockePK;
    }

    public MetodaUmjerneTocke(int metodaId, int tockaNum) {
        this.metodaUmjerneTockePK = new MetodaUmjerneTockePK(metodaId, tockaNum);
    }

    public MetodaUmjerneTockePK getMetodaUmjerneTockePK() {
        return metodaUmjerneTockePK;
    }

    public void setMetodaUmjerneTockePK(MetodaUmjerneTockePK metodaUmjerneTockePK) {
        this.metodaUmjerneTockePK = metodaUmjerneTockePK;
    }

    public Integer getPostotakOpsega() {
        return postotakOpsega;
    }

    public void setPostotakOpsega(Integer postotakOpsega) {
        this.postotakOpsega = postotakOpsega;
    }

    public Integer getBrojMjerenja() {
        return brojMjerenja;
    }

    public void setBrojMjerenja(Integer brojMjerenja) {
        this.brojMjerenja = brojMjerenja;
    }

    public Integer getBrojUSrednjaku() {
        return brojUSrednjaku;
    }

    public void setBrojUSrednjaku(Integer brojUSrednjaku) {
        this.brojUSrednjaku = brojUSrednjaku;
    }

    public AnalitickeMetode getAnalitickeMetode() {
        return analitickeMetode;
    }

    public void setAnalitickeMetode(AnalitickeMetode analitickeMetode) {
        this.analitickeMetode = analitickeMetode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metodaUmjerneTockePK != null ? metodaUmjerneTockePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetodaUmjerneTocke)) {
            return false;
        }
        MetodaUmjerneTocke other = (MetodaUmjerneTocke) object;
        if ((this.metodaUmjerneTockePK == null && other.metodaUmjerneTockePK != null) || (this.metodaUmjerneTockePK != null && !this.metodaUmjerneTockePK.equals(other.metodaUmjerneTockePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.MetodaUmjerneTocke[ metodaUmjerneTockePK=" + metodaUmjerneTockePK + " ]";
    }
    
}
