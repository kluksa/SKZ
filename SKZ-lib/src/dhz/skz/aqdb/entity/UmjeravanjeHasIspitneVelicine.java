/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjeravanje_has_ispitne_velicine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjeravanjeHasIspitneVelicine.findAll", query = "SELECT u FROM UmjeravanjeHasIspitneVelicine u"),
    @NamedQuery(name = "UmjeravanjeHasIspitneVelicine.findByUmjeravanjeId", query = "SELECT u FROM UmjeravanjeHasIspitneVelicine u WHERE u.umjeravanjeHasIspitneVelicinePK.umjeravanjeId = :umjeravanjeId"),
    @NamedQuery(name = "UmjeravanjeHasIspitneVelicine.findByIspitneVelicineId", query = "SELECT u FROM UmjeravanjeHasIspitneVelicine u WHERE u.umjeravanjeHasIspitneVelicinePK.ispitneVelicineId = :ispitneVelicineId"),
    @NamedQuery(name = "UmjeravanjeHasIspitneVelicine.findByKomponentaId", query = "SELECT u FROM UmjeravanjeHasIspitneVelicine u WHERE u.umjeravanjeHasIspitneVelicinePK.komponentaId = :komponentaId"),
    @NamedQuery(name = "UmjeravanjeHasIspitneVelicine.findByIznos", query = "SELECT u FROM UmjeravanjeHasIspitneVelicine u WHERE u.iznos = :iznos")})
public class UmjeravanjeHasIspitneVelicine implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UmjeravanjeHasIspitneVelicinePK umjeravanjeHasIspitneVelicinePK;
    @Basic(optional = false)
    @NotNull
    private double iznos;
    @JoinColumn(name = "ispitne_velicine_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IspitneVelicine ispitneVelicine;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Umjeravanje umjeravanje;

    public UmjeravanjeHasIspitneVelicine() {
    }

    public UmjeravanjeHasIspitneVelicine(UmjeravanjeHasIspitneVelicinePK umjeravanjeHasIspitneVelicinePK) {
        this.umjeravanjeHasIspitneVelicinePK = umjeravanjeHasIspitneVelicinePK;
    }

    public UmjeravanjeHasIspitneVelicine(UmjeravanjeHasIspitneVelicinePK umjeravanjeHasIspitneVelicinePK, double iznos) {
        this.umjeravanjeHasIspitneVelicinePK = umjeravanjeHasIspitneVelicinePK;
        this.iznos = iznos;
    }

    public UmjeravanjeHasIspitneVelicine(int umjeravanjeId, int ispitneVelicineId, int komponentaId) {
        this.umjeravanjeHasIspitneVelicinePK = new UmjeravanjeHasIspitneVelicinePK(umjeravanjeId, ispitneVelicineId, komponentaId);
    }

    public UmjeravanjeHasIspitneVelicinePK getUmjeravanjeHasIspitneVelicinePK() {
        return umjeravanjeHasIspitneVelicinePK;
    }

    public void setUmjeravanjeHasIspitneVelicinePK(UmjeravanjeHasIspitneVelicinePK umjeravanjeHasIspitneVelicinePK) {
        this.umjeravanjeHasIspitneVelicinePK = umjeravanjeHasIspitneVelicinePK;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public IspitneVelicine getIspitneVelicine() {
        return ispitneVelicine;
    }

    public void setIspitneVelicine(IspitneVelicine ispitneVelicine) {
        this.ispitneVelicine = ispitneVelicine;
    }

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    public Umjeravanje getUmjeravanje() {
        return umjeravanje;
    }

    public void setUmjeravanje(Umjeravanje umjeravanje) {
        this.umjeravanje = umjeravanje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (umjeravanjeHasIspitneVelicinePK != null ? umjeravanjeHasIspitneVelicinePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjeHasIspitneVelicine)) {
            return false;
        }
        UmjeravanjeHasIspitneVelicine other = (UmjeravanjeHasIspitneVelicine) object;
        if ((this.umjeravanjeHasIspitneVelicinePK == null && other.umjeravanjeHasIspitneVelicinePK != null) || (this.umjeravanjeHasIspitneVelicinePK != null && !this.umjeravanjeHasIspitneVelicinePK.equals(other.umjeravanjeHasIspitneVelicinePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine[ umjeravanjeHasIspitneVelicinePK=" + umjeravanjeHasIspitneVelicinePK + " ]";
    }
    
}
