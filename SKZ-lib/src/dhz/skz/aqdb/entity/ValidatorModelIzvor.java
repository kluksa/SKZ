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
@Table(name = "validator_model_izvor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValidatorModelIzvor.findAll", query = "SELECT v FROM ValidatorModelIzvor v"),
    @NamedQuery(name = "ValidatorModelIzvor.findByModelUredjajaId", query = "SELECT v FROM ValidatorModelIzvor v WHERE v.validatorModelIzvorPK.modelUredjajaId = :modelUredjajaId"),
    @NamedQuery(name = "ValidatorModelIzvor.findByIzvorPodatakaId", query = "SELECT v FROM ValidatorModelIzvor v WHERE v.validatorModelIzvorPK.izvorPodatakaId = :izvorPodatakaId")})
public class ValidatorModelIzvor implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ValidatorModelIzvorPK validatorModelIzvorPK;
    @JoinColumn(name = "izvor_podataka_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IzvorPodataka izvorPodataka;
    @JoinColumn(name = "model_uredjaja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ModelUredjaja modelUredjaja;
    @JoinColumn(name = "validatori_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Validatori validatoriId;

    public ValidatorModelIzvor() {
    }

    public ValidatorModelIzvor(ValidatorModelIzvorPK validatorModelIzvorPK) {
        this.validatorModelIzvorPK = validatorModelIzvorPK;
    }

    public ValidatorModelIzvor(int modelUredjajaId, int izvorPodatakaId) {
        this.validatorModelIzvorPK = new ValidatorModelIzvorPK(modelUredjajaId, izvorPodatakaId);
    }

    public ValidatorModelIzvorPK getValidatorModelIzvorPK() {
        return validatorModelIzvorPK;
    }

    public void setValidatorModelIzvorPK(ValidatorModelIzvorPK validatorModelIzvorPK) {
        this.validatorModelIzvorPK = validatorModelIzvorPK;
    }

    public IzvorPodataka getIzvorPodataka() {
        return izvorPodataka;
    }

    public void setIzvorPodataka(IzvorPodataka izvorPodataka) {
        this.izvorPodataka = izvorPodataka;
    }

    public ModelUredjaja getModelUredjaja() {
        return modelUredjaja;
    }

    public void setModelUredjaja(ModelUredjaja modelUredjaja) {
        this.modelUredjaja = modelUredjaja;
    }

    public Validatori getValidatoriId() {
        return validatoriId;
    }

    public void setValidatoriId(Validatori validatoriId) {
        this.validatoriId = validatoriId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (validatorModelIzvorPK != null ? validatorModelIzvorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValidatorModelIzvor)) {
            return false;
        }
        ValidatorModelIzvor other = (ValidatorModelIzvor) object;
        if ((this.validatorModelIzvorPK == null && other.validatorModelIzvorPK != null) || (this.validatorModelIzvorPK != null && !this.validatorModelIzvorPK.equals(other.validatorModelIzvorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ValidatorModelIzvor[ validatorModelIzvorPK=" + validatorModelIzvorPK + " ]";
    }
    
}
