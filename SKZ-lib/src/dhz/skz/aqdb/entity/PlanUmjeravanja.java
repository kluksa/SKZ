/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "plan_umjeravanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanUmjeravanja.findAll", query = "SELECT p FROM PlanUmjeravanja p"),
    @NamedQuery(name = "PlanUmjeravanja.findByOpremaId", query = "SELECT p FROM PlanUmjeravanja p WHERE p.planUmjeravanjaPK.opremaId = :opremaId"),
    @NamedQuery(name = "PlanUmjeravanja.findByDatum", query = "SELECT p FROM PlanUmjeravanja p WHERE p.planUmjeravanjaPK.datum = :datum"),
    @NamedQuery(name = "PlanUmjeravanja.findByIspitnaVelicinaId", query = "SELECT p FROM PlanUmjeravanja p WHERE p.planUmjeravanjaPK.ispitnaVelicinaId = :ispitnaVelicinaId")})
public class PlanUmjeravanja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanUmjeravanjaPK planUmjeravanjaPK;
    @JoinColumn(name = "ispitna_velicina_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IspitneVelicine ispitneVelicine;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Umjeravanje umjeravanjeId;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjaj;

    public PlanUmjeravanja() {
    }

    public PlanUmjeravanja(PlanUmjeravanjaPK planUmjeravanjaPK) {
        this.planUmjeravanjaPK = planUmjeravanjaPK;
    }

    public PlanUmjeravanja(int opremaId, Date datum, int ispitnaVelicinaId) {
        this.planUmjeravanjaPK = new PlanUmjeravanjaPK(opremaId, datum, ispitnaVelicinaId);
    }

    public PlanUmjeravanjaPK getPlanUmjeravanjaPK() {
        return planUmjeravanjaPK;
    }

    public void setPlanUmjeravanjaPK(PlanUmjeravanjaPK planUmjeravanjaPK) {
        this.planUmjeravanjaPK = planUmjeravanjaPK;
    }

    public IspitneVelicine getIspitneVelicine() {
        return ispitneVelicine;
    }

    public void setIspitneVelicine(IspitneVelicine ispitneVelicine) {
        this.ispitneVelicine = ispitneVelicine;
    }

    public Umjeravanje getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(Umjeravanje umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
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
        hash += (planUmjeravanjaPK != null ? planUmjeravanjaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanUmjeravanja)) {
            return false;
        }
        PlanUmjeravanja other = (PlanUmjeravanja) object;
        if ((this.planUmjeravanjaPK == null && other.planUmjeravanjaPK != null) || (this.planUmjeravanjaPK != null && !this.planUmjeravanjaPK.equals(other.planUmjeravanjaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PlanUmjeravanja[ planUmjeravanjaPK=" + planUmjeravanjaPK + " ]";
    }
    
}
