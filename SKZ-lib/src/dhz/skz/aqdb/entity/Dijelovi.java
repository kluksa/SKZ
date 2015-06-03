/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dijelovi.findAll", query = "SELECT d FROM Dijelovi d"),
    @NamedQuery(name = "Dijelovi.findById", query = "SELECT d FROM Dijelovi d WHERE d.dijeloviPK.id = :id"),
    @NamedQuery(name = "Dijelovi.findByNaziv", query = "SELECT d FROM Dijelovi d WHERE d.naziv = :naziv"),
    @NamedQuery(name = "Dijelovi.findByModelUredjajaId", query = "SELECT d FROM Dijelovi d WHERE d.dijeloviPK.modelUredjajaId = :modelUredjajaId"),
    @NamedQuery(name = "Dijelovi.findByRaspoloziviBroj", query = "SELECT d FROM Dijelovi d WHERE d.raspoloziviBroj = :raspoloziviBroj"),
    @NamedQuery(name = "Dijelovi.findByOznakaProizvodjaca", query = "SELECT d FROM Dijelovi d WHERE d.oznakaProizvodjaca = :oznakaProizvodjaca")})
public class Dijelovi implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DijeloviPK dijeloviPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "raspolozivi_broj")
    private String raspoloziviBroj;
    @Size(max = 90)
    @Column(name = "oznaka_proizvodjaca")
    private String oznakaProizvodjaca;
    @JoinTable(name = "odrzavanje_has_dijelovi", joinColumns = {
        @JoinColumn(name = "dijelovi_id", referencedColumnName = "id"),
        @JoinColumn(name = "dijelovi_model_uredjaja_id", referencedColumnName = "model_uredjaja_id")}, inverseJoinColumns = {
        @JoinColumn(name = "odrzavanje_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Odrzavanje> odrzavanjeCollection;
    @JoinColumn(name = "model_uredjaja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ModelUredjaja modelUredjaja;

    public Dijelovi() {
    }

    public Dijelovi(DijeloviPK dijeloviPK) {
        this.dijeloviPK = dijeloviPK;
    }

    public Dijelovi(DijeloviPK dijeloviPK, String naziv, String raspoloziviBroj) {
        this.dijeloviPK = dijeloviPK;
        this.naziv = naziv;
        this.raspoloziviBroj = raspoloziviBroj;
    }

    public Dijelovi(int id, int modelUredjajaId) {
        this.dijeloviPK = new DijeloviPK(id, modelUredjajaId);
    }

    public DijeloviPK getDijeloviPK() {
        return dijeloviPK;
    }

    public void setDijeloviPK(DijeloviPK dijeloviPK) {
        this.dijeloviPK = dijeloviPK;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getRaspoloziviBroj() {
        return raspoloziviBroj;
    }

    public void setRaspoloziviBroj(String raspoloziviBroj) {
        this.raspoloziviBroj = raspoloziviBroj;
    }

    public String getOznakaProizvodjaca() {
        return oznakaProizvodjaca;
    }

    public void setOznakaProizvodjaca(String oznakaProizvodjaca) {
        this.oznakaProizvodjaca = oznakaProizvodjaca;
    }

    @XmlTransient
    public Collection<Odrzavanje> getOdrzavanjeCollection() {
        return odrzavanjeCollection;
    }

    public void setOdrzavanjeCollection(Collection<Odrzavanje> odrzavanjeCollection) {
        this.odrzavanjeCollection = odrzavanjeCollection;
    }

    public ModelUredjaja getModelUredjaja() {
        return modelUredjaja;
    }

    public void setModelUredjaja(ModelUredjaja modelUredjaja) {
        this.modelUredjaja = modelUredjaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dijeloviPK != null ? dijeloviPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dijelovi)) {
            return false;
        }
        Dijelovi other = (Dijelovi) object;
        if ((this.dijeloviPK == null && other.dijeloviPK != null) || (this.dijeloviPK != null && !this.dijeloviPK.equals(other.dijeloviPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.Dijelovi[ dijeloviPK=" + dijeloviPK + " ]";
    }
    
}
