/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.aqdb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "zadnji_sirovi")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZadnjiSirovi.findAll", query = "SELECT z FROM ZadnjiSirovi z"),
    @NamedQuery(name = "ZadnjiSirovi.findByProgramMjerenja", query = "SELECT z FROM ZadnjiSirovi z WHERE z.programMjerenja = :programMjerenja"),
    @NamedQuery(name = "ZadnjiSirovi.findByVrijeme", query = "SELECT z FROM ZadnjiSirovi z WHERE z.vrijeme = :vrijeme"),
    @NamedQuery(name = "ZadnjiSirovi.findByPodatakSiroviId", query = "SELECT z FROM ZadnjiSirovi z WHERE z.podatakSiroviId = :podatakSiroviId")})
public class ZadnjiSirovi implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "program_mjerenja_id", nullable = false)
    private Integer programMjerenjaId;

    @OneToOne
    @PrimaryKeyJoinColumn(name="program_mjerenja_id", referencedColumnName="id")
    private ProgramMjerenja programMjerenja;
    

    
    @JoinColumn(name = "podatak_sirovi_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PodatakSirovi podatakSiroviId;
    
    public ZadnjiSirovi() {
    }

    public ZadnjiSirovi(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public ZadnjiSirovi(Integer programMjerenjaId, Date vrijeme, PodatakSirovi podatakSiroviId) {
        this.programMjerenjaId = programMjerenjaId;
        this.vrijeme = vrijeme;
        this.podatakSiroviId = podatakSiroviId;
    }

    public Integer getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(Integer programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    public PodatakSirovi getPodatakSiroviId() {
        return podatakSiroviId;
    }

    public void setPodatakSiroviId(PodatakSirovi podatakSiroviId) {
        this.podatakSiroviId = podatakSiroviId;
    }

    public ProgramMjerenja getProgramMjerenja() {
        return programMjerenja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programMjerenjaId != null ? programMjerenjaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZadnjiSirovi)) {
            return false;
        }
        ZadnjiSirovi other = (ZadnjiSirovi) object;
        if ((this.programMjerenjaId == null && other.programMjerenjaId != null) || (this.programMjerenjaId != null && !this.programMjerenjaId.equals(other.programMjerenjaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dhz.skz.aqdb.entity.ZadnjiSirovi[ programMjerenjaId=" + programMjerenjaId + " ]";
    }
    
}
