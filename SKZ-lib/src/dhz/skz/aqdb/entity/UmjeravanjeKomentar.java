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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "umjeravanje_komentar", catalog = "aqdb_likz", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UmjeravanjeKomentar.findAll", query = "SELECT u FROM UmjeravanjeKomentar u"),
    @NamedQuery(name = "UmjeravanjeKomentar.findByUmjeravanjeId", query = "SELECT u FROM UmjeravanjeKomentar u WHERE u.umjeravanjeId = :umjeravanjeId")})
public class UmjeravanjeKomentar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "umjeravanje_id", nullable = false)
    private Integer umjeravanjeId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(nullable = false, length = 65535)
    private String komentar;
    @JoinColumn(name = "umjeravanje_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Umjeravanje umjeravanje;

    public UmjeravanjeKomentar() {
    }

    public UmjeravanjeKomentar(Integer umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public UmjeravanjeKomentar(Integer umjeravanjeId, String komentar) {
        this.umjeravanjeId = umjeravanjeId;
        this.komentar = komentar;
    }

    public Integer getUmjeravanjeId() {
        return umjeravanjeId;
    }

    public void setUmjeravanjeId(Integer umjeravanjeId) {
        this.umjeravanjeId = umjeravanjeId;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
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
        hash += (umjeravanjeId != null ? umjeravanjeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UmjeravanjeKomentar)) {
            return false;
        }
        UmjeravanjeKomentar other = (UmjeravanjeKomentar) object;
        if ((this.umjeravanjeId == null && other.umjeravanjeId != null) || (this.umjeravanjeId != null && !this.umjeravanjeId.equals(other.umjeravanjeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.UmjeravanjeKomentar[ umjeravanjeId=" + umjeravanjeId + " ]";
    }
    
}
