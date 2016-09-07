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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kraljevic
 */
public class KomponentaUpozorenja implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @JoinColumn(name = "komponenta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Komponenta komponenta;
    
    @Size(max = 300)
    @Column(name = "predlozak_naslov")
    private String predlozakNaslova;

    @Size(max = 300)
    @Column(name = "predlozak_teksta")
    private String predlozakTeksta;

    @Basic(optional = false)
    @NotNull
    @Column(name = "granicna_koncentracija")
    private double granicnaKoncentracija;
    
    @JoinColumn(name = "agregacije_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Agregacije agregacijeId;

    @Column(name = "broj_pojavljivanja")
    private int brojPojavljivanja;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Komponenta getKomponenta() {
        return komponenta;
    }

    public void setKomponenta(Komponenta komponenta) {
        this.komponenta = komponenta;
    }

    public String getPredlozakTeksta() {
        return predlozakTeksta;
    }

    public void setPredlozakTeksta(String predlozakTeksta) {
        this.predlozakTeksta = predlozakTeksta;
    }

    public double getGranicnaKoncentracija() {
        return granicnaKoncentracija;
    }

    public void setGranicnaKoncentracija(double granicnaKoncentracija) {
        this.granicnaKoncentracija = granicnaKoncentracija;
    }

    public Agregacije getAgregacijeId() {
        return agregacijeId;
    }

    public void setAgregacijeId(Agregacije agregacijeId) {
        this.agregacijeId = agregacijeId;
    }

    public int getBrojPojavljivanja() {
        return brojPojavljivanja;
    }

    public void setBrojPojavljivanja(int brojPojavljivanja) {
        this.brojPojavljivanja = brojPojavljivanja;
    }

    public String getPredlozakNaslova() {
        return predlozakNaslova;
    }

    public void setPredlozakNaslova(String predlozakNaslova) {
        this.predlozakNaslova = predlozakNaslova;
    }
    
}
