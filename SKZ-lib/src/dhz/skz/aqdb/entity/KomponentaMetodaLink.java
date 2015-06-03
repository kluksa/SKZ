/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "komponenta_metoda_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KomponentaMetodaLink.findAll", query = "SELECT k FROM KomponentaMetodaLink k"),
    @NamedQuery(name = "KomponentaMetodaLink.findById", query = "SELECT k FROM KomponentaMetodaLink k WHERE k.id = :id")})
public class KomponentaMetodaLink implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @JoinColumn(name = "metoda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AnalitickeMetode metodaId;
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Komponenta komponentaId;

    public KomponentaMetodaLink() {
    }

    public KomponentaMetodaLink(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AnalitickeMetode getMetodaId() {
        return metodaId;
    }

    public void setMetodaId(AnalitickeMetode metodaId) {
        this.metodaId = metodaId;
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
        if (!(object instanceof KomponentaMetodaLink)) {
            return false;
        }
        KomponentaMetodaLink other = (KomponentaMetodaLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.KomponentaMetodaLink[ id=" + id + " ]";
    }
    
}
