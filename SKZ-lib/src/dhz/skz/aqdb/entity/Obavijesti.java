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
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@Entity
@Table(name = "obavijesti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Obavijesti.findAll", query = "SELECT o FROM Obavijesti o"),
    @NamedQuery(name = "Obavijesti.findByPrimatelj", query = "SELECT o FROM Obavijesti o WHERE o.primatelj = :primatelj"),

})

public class Obavijesti implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @JoinColumn(name = "granice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Granice granica;
    
    @JoinColumn(name = "primatelji_podataka_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PrimateljiPodataka primatelj;
    
    @Size(max = 300)
    @Column(name = "predlozak_teksta")
    private String predlozakTeksta;

    public Obavijesti() {
       
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPredlozakTeksta() {
        return predlozakTeksta;
    }

    public void setPredlozakTeksta(String predlozakTeksta) {
        this.predlozakTeksta = predlozakTeksta;
    }

    public Granice getGranica() {
        return granica;
    }

    public void setGranica(Granice granica) {
        this.granica = granica;
    }

    public PrimateljiPodataka getPrimatelj() {
        return primatelj;
    }

    public void setPrimatelj(PrimateljiPodataka primatelj) {
        this.primatelj = primatelj;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Obavijesti other = (Obavijesti) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Obavijesti{" + "id=" + id + '}';
    }
}
