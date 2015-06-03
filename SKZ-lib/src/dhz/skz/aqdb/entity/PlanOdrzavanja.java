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
@Table(name = "plan_odrzavanja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanOdrzavanja.findAll", query = "SELECT p FROM PlanOdrzavanja p"),
    @NamedQuery(name = "PlanOdrzavanja.findByOpremaId", query = "SELECT p FROM PlanOdrzavanja p WHERE p.planOdrzavanjaPK.opremaId = :opremaId"),
    @NamedQuery(name = "PlanOdrzavanja.findByVrstaOdrzavanjaId", query = "SELECT p FROM PlanOdrzavanja p WHERE p.planOdrzavanjaPK.vrstaOdrzavanjaId = :vrstaOdrzavanjaId"),
    @NamedQuery(name = "PlanOdrzavanja.findByDatum", query = "SELECT p FROM PlanOdrzavanja p WHERE p.planOdrzavanjaPK.datum = :datum")})
public class PlanOdrzavanja implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanOdrzavanjaPK planOdrzavanjaPK;
    @JoinColumn(name = "odrzavanje_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Odrzavanje odrzavanjeId;
    @JoinColumn(name = "oprema_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Uredjaj uredjaj;
    @JoinColumn(name = "vrsta_odrzavanja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private VrstaOdrzavanja vrstaOdrzavanja;

    public PlanOdrzavanja() {
    }

    public PlanOdrzavanja(PlanOdrzavanjaPK planOdrzavanjaPK) {
        this.planOdrzavanjaPK = planOdrzavanjaPK;
    }

    public PlanOdrzavanja(int opremaId, int vrstaOdrzavanjaId, Date datum) {
        this.planOdrzavanjaPK = new PlanOdrzavanjaPK(opremaId, vrstaOdrzavanjaId, datum);
    }

    public PlanOdrzavanjaPK getPlanOdrzavanjaPK() {
        return planOdrzavanjaPK;
    }

    public void setPlanOdrzavanjaPK(PlanOdrzavanjaPK planOdrzavanjaPK) {
        this.planOdrzavanjaPK = planOdrzavanjaPK;
    }

    public Odrzavanje getOdrzavanjeId() {
        return odrzavanjeId;
    }

    public void setOdrzavanjeId(Odrzavanje odrzavanjeId) {
        this.odrzavanjeId = odrzavanjeId;
    }

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }

    public VrstaOdrzavanja getVrstaOdrzavanja() {
        return vrstaOdrzavanja;
    }

    public void setVrstaOdrzavanja(VrstaOdrzavanja vrstaOdrzavanja) {
        this.vrstaOdrzavanja = vrstaOdrzavanja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planOdrzavanjaPK != null ? planOdrzavanjaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanOdrzavanja)) {
            return false;
        }
        PlanOdrzavanja other = (PlanOdrzavanja) object;
        if ((this.planOdrzavanjaPK == null && other.planOdrzavanjaPK != null) || (this.planOdrzavanjaPK != null && !this.planOdrzavanjaPK.equals(other.planOdrzavanjaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PlanOdrzavanja[ planOdrzavanjaPK=" + planOdrzavanjaPK + " ]";
    }
    
}
