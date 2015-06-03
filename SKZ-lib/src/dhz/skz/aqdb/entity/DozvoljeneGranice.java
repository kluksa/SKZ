/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
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
@Table(name = "dozvoljene_granice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DozvoljeneGranice.findAll", query = "SELECT d FROM DozvoljeneGranice d"),
    @NamedQuery(name = "DozvoljeneGranice.findByIspitneVelicineId", query = "SELECT d FROM DozvoljeneGranice d WHERE d.dozvoljeneGranicePK.ispitneVelicineId = :ispitneVelicineId"),
    @NamedQuery(name = "DozvoljeneGranice.findByAnalitickeMetodeId", query = "SELECT d FROM DozvoljeneGranice d WHERE d.dozvoljeneGranicePK.analitickeMetodeId = :analitickeMetodeId"),
    @NamedQuery(name = "DozvoljeneGranice.findByMin", query = "SELECT d FROM DozvoljeneGranice d WHERE d.min = :min"),
    @NamedQuery(name = "DozvoljeneGranice.findByMax", query = "SELECT d FROM DozvoljeneGranice d WHERE d.max = :max")})
public class DozvoljeneGranice implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DozvoljeneGranicePK dozvoljeneGranicePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double min;
    private Double max;
    @JoinColumn(name = "analiticke_metode_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AnalitickeMetode analitickeMetode;
    @JoinColumn(name = "ispitne_velicine_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IspitneVelicine ispitneVelicine;
    @JoinColumn(name = "mjerne_jedinice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MjerneJedinice mjerneJediniceId;

    public DozvoljeneGranice() {
    }

    public DozvoljeneGranice(DozvoljeneGranicePK dozvoljeneGranicePK) {
        this.dozvoljeneGranicePK = dozvoljeneGranicePK;
    }

    public DozvoljeneGranice(int ispitneVelicineId, int analitickeMetodeId) {
        this.dozvoljeneGranicePK = new DozvoljeneGranicePK(ispitneVelicineId, analitickeMetodeId);
    }

    public DozvoljeneGranicePK getDozvoljeneGranicePK() {
        return dozvoljeneGranicePK;
    }

    public void setDozvoljeneGranicePK(DozvoljeneGranicePK dozvoljeneGranicePK) {
        this.dozvoljeneGranicePK = dozvoljeneGranicePK;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public AnalitickeMetode getAnalitickeMetode() {
        return analitickeMetode;
    }

    public void setAnalitickeMetode(AnalitickeMetode analitickeMetode) {
        this.analitickeMetode = analitickeMetode;
    }

    public IspitneVelicine getIspitneVelicine() {
        return ispitneVelicine;
    }

    public void setIspitneVelicine(IspitneVelicine ispitneVelicine) {
        this.ispitneVelicine = ispitneVelicine;
    }

    public MjerneJedinice getMjerneJediniceId() {
        return mjerneJediniceId;
    }

    public void setMjerneJediniceId(MjerneJedinice mjerneJediniceId) {
        this.mjerneJediniceId = mjerneJediniceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dozvoljeneGranicePK != null ? dozvoljeneGranicePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DozvoljeneGranice)) {
            return false;
        }
        DozvoljeneGranice other = (DozvoljeneGranice) object;
        if ((this.dozvoljeneGranicePK == null && other.dozvoljeneGranicePK != null) || (this.dozvoljeneGranicePK != null && !this.dozvoljeneGranicePK.equals(other.dozvoljeneGranicePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.DozvoljeneGranice[ dozvoljeneGranicePK=" + dozvoljeneGranicePK + " ]";
    }
    
}
