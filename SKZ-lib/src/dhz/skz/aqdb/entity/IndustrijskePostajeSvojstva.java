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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "industrijske_postaje_svojstva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndustrijskePostajeSvojstva.findAll", query = "SELECT i FROM IndustrijskePostajeSvojstva i"),
    @NamedQuery(name = "IndustrijskePostajeSvojstva.findByPostajaId", query = "SELECT i FROM IndustrijskePostajeSvojstva i WHERE i.postajaId = :postajaId"),
    @NamedQuery(name = "IndustrijskePostajeSvojstva.findByUdaljenostOdIzvora", query = "SELECT i FROM IndustrijskePostajeSvojstva i WHERE i.udaljenostOdIzvora = :udaljenostOdIzvora")})
public class IndustrijskePostajeSvojstva implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "postaja_id")
    private Integer postajaId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "udaljenost_od_izvora")
    private Double udaljenostOdIzvora;
    @JoinTable(name = "industrijske_postaje_svojstva_has_snap_sektori", joinColumns = {
        @JoinColumn(name = "industrijske_postaje_svojstva_postaja_id", referencedColumnName = "postaja_id")}, inverseJoinColumns = {
        @JoinColumn(name = "snap_sektori_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<SnapSektori> snapSektoriCollection;
    @JoinColumn(name = "postaja_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Postaja postaja;

    public IndustrijskePostajeSvojstva() {
    }

    public IndustrijskePostajeSvojstva(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Integer getPostajaId() {
        return postajaId;
    }

    public void setPostajaId(Integer postajaId) {
        this.postajaId = postajaId;
    }

    public Double getUdaljenostOdIzvora() {
        return udaljenostOdIzvora;
    }

    public void setUdaljenostOdIzvora(Double udaljenostOdIzvora) {
        this.udaljenostOdIzvora = udaljenostOdIzvora;
    }

    @XmlTransient
    public Collection<SnapSektori> getSnapSektoriCollection() {
        return snapSektoriCollection;
    }

    public void setSnapSektoriCollection(Collection<SnapSektori> snapSektoriCollection) {
        this.snapSektoriCollection = snapSektoriCollection;
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
        if (!(object instanceof IndustrijskePostajeSvojstva)) {
            return false;
        }
        IndustrijskePostajeSvojstva other = (IndustrijskePostajeSvojstva) object;
        if ((this.postajaId == null && other.postajaId != null) || (this.postajaId != null && !this.postajaId.equals(other.postajaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.IndustrijskePostajeSvojstva[ postajaId=" + postajaId + " ]";
    }
    
}
