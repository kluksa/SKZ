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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "ciljevi_pracenja", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CiljeviPracenja.findAll", query = "SELECT c FROM CiljeviPracenja c"),
    @NamedQuery(name = "CiljeviPracenja.findById", query = "SELECT c FROM CiljeviPracenja c WHERE c.id = :id"),
    @NamedQuery(name = "CiljeviPracenja.findByTekstualnaDefinicija", query = "SELECT c FROM CiljeviPracenja c WHERE c.tekstualnaDefinicija = :tekstualnaDefinicija")})
public class CiljeviPracenja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tekstualna_definicija", nullable = false, length = 45)
    private String tekstualnaDefinicija;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(nullable = false, length = 65535)
    private String opis;
    @JoinTable(name = "postaja_ciljevi_link", joinColumns = {
        @JoinColumn(name = "ciljevi_pracenja_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "postaja_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany
    private Collection<Postaja> postajaCollection;

    public CiljeviPracenja() {
    }

    public CiljeviPracenja(Integer id) {
        this.id = id;
    }

    public CiljeviPracenja(Integer id, String tekstualnaDefinicija, String opis) {
        this.id = id;
        this.tekstualnaDefinicija = tekstualnaDefinicija;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTekstualnaDefinicija() {
        return tekstualnaDefinicija;
    }

    public void setTekstualnaDefinicija(String tekstualnaDefinicija) {
        this.tekstualnaDefinicija = tekstualnaDefinicija;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @XmlTransient
    public Collection<Postaja> getPostajaCollection() {
        return postajaCollection;
    }

    public void setPostajaCollection(Collection<Postaja> postajaCollection) {
        this.postajaCollection = postajaCollection;
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
        if (!(object instanceof CiljeviPracenja)) {
            return false;
        }
        CiljeviPracenja other = (CiljeviPracenja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.CiljeviPracenja[ id=" + id + " ]";
    }
    
}
