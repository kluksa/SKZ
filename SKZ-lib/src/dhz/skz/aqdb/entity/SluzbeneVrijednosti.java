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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "sluzbene_vrijednosti", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SluzbeneVrijednosti.findAll", query = "SELECT s FROM SluzbeneVrijednosti s"),
    @NamedQuery(name = "SluzbeneVrijednosti.findById", query = "SELECT s FROM SluzbeneVrijednosti s WHERE s.id = :id"),
    @NamedQuery(name = "SluzbeneVrijednosti.findByUsrednjavanjaId", query = "SELECT s FROM SluzbeneVrijednosti s WHERE s.usrednjavanjaId = :usrednjavanjaId"),
    @NamedQuery(name = "SluzbeneVrijednosti.findByNaziv", query = "SELECT s FROM SluzbeneVrijednosti s WHERE s.naziv = :naziv"),
    @NamedQuery(name = "SluzbeneVrijednosti.findByGranicnaVrijednost", query = "SELECT s FROM SluzbeneVrijednosti s WHERE s.granicnaVrijednost = :granicnaVrijednost")})
public class SluzbeneVrijednosti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "usrednjavanja_id")
    private short usrednjavanjaId;
    @Column(name = "naziv")
    private String naziv;
    @Column(name = "granicna_vrijednost")
    private String granicnaVrijednost;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Komponenta komponentaId;

    public SluzbeneVrijednosti() {
    }

    public SluzbeneVrijednosti(Integer id) {
        this.id = id;
    }

    public SluzbeneVrijednosti(Integer id, short usrednjavanjaId) {
        this.id = id;
        this.usrednjavanjaId = usrednjavanjaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getUsrednjavanjaId() {
        return usrednjavanjaId;
    }

    public void setUsrednjavanjaId(short usrednjavanjaId) {
        this.usrednjavanjaId = usrednjavanjaId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getGranicnaVrijednost() {
        return granicnaVrijednost;
    }

    public void setGranicnaVrijednost(String granicnaVrijednost) {
        this.granicnaVrijednost = granicnaVrijednost;
    }

    public Komponenta getKomponentaId() {
        return komponentaId;
    }

    public void setKomponentaId(Komponenta komponentaId) {
        this.komponentaId = komponentaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SluzbeneVrijednosti)) {
            return false;
        }
        SluzbeneVrijednosti other = (SluzbeneVrijednosti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.SluzbeneVrijednosti[ id=" + id + " ]";
    }

}
