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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "prometne_postaje_svojstva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrometnePostajeSvojstva.findAll", query = "SELECT p FROM PrometnePostajeSvojstva p"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByPostajaId", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.postajaId = :postajaId"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByKolicinaPrometa", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.kolicinaPrometa = :kolicinaPrometa"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByUdaljenostOdRubaPlocnika", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.udaljenostOdRubaPlocnika = :udaljenostOdRubaPlocnika"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByUdioTeskihVozila", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.udioTeskihVozila = :udioTeskihVozila"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByUdaljenostOdProcelja", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.udaljenostOdProcelja = :udaljenostOdProcelja"),
    @NamedQuery(name = "PrometnePostajeSvojstva.findByVisinaZgrade", query = "SELECT p FROM PrometnePostajeSvojstva p WHERE p.visinaZgrade = :visinaZgrade")})
public class PrometnePostajeSvojstva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "postaja_id")
    private Integer postajaId;
    @Column(name = "kolicina_prometa")
    private Integer kolicinaPrometa;
    @Column(name = "udaljenost_od_ruba_plocnika")
    private Integer udaljenostOdRubaPlocnika;
    @Column(name = "udio_teskih_vozila")
    private Integer udioTeskihVozila;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "udaljenost_od_procelja")
    private Float udaljenostOdProcelja;
    @Column(name = "visina_zgrade")
    private Float visinaZgrade;
    @JoinColumn(name = "postaja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Postaja postaja;

    public PrometnePostajeSvojstva() {
    }

    public PrometnePostajeSvojstva(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Integer getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Integer getKolicinaPrometa() {
        return kolicinaPrometa;
    }

    public void setKolicinaPrometa(Integer kolicinaPrometa) {
        this.kolicinaPrometa = kolicinaPrometa;
    }

    public Integer getUdaljenostOdRubaPlocnika() {
        return udaljenostOdRubaPlocnika;
    }

    public void setUdaljenostOdRubaPlocnika(Integer udaljenostOdRubaPlocnika) {
        this.udaljenostOdRubaPlocnika = udaljenostOdRubaPlocnika;
    }

    public Integer getUdioTeskihVozila() {
        return udioTeskihVozila;
    }

    public void setUdioTeskihVozila(Integer udioTeskihVozila) {
        this.udioTeskihVozila = udioTeskihVozila;
    }

    public Float getUdaljenostOdProcelja() {
        return udaljenostOdProcelja;
    }

    public void setUdaljenostOdProcelja(Float udaljenostOdProcelja) {
        this.udaljenostOdProcelja = udaljenostOdProcelja;
    }

    public Float getVisinaZgrade() {
        return visinaZgrade;
    }

    public void setVisinaZgrade(Float visinaZgrade) {
        this.visinaZgrade = visinaZgrade;
    }

    public Postaja getPostaja() {
        return postaja;
    }

    public void setPostaja(Postaja postaja) {
        this.postaja = postaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (postajaId != null ? postajaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrometnePostajeSvojstva)) {
            return false;
        }
        PrometnePostajeSvojstva other = (PrometnePostajeSvojstva) object;
        if ((this.postajaId == null && other.postajaId != null) || (this.postajaId != null && !this.postajaId.equals(other.postajaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.PrometnePostajeSvojstva[ postajaId=" + postajaId + " ]";
    }
    
}
