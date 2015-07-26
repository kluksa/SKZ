/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import dhz.skz.aqdb.entity.Postaja;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class PostajaDTO implements Serializable {
    
    private int id;
    private String naziv;
    private double g_sirina;
    private double g_duzina;
    private String oznaka;
    private int nadmorska_visina;

    public PostajaDTO(){}
    
    public PostajaDTO(Postaja p) {
        this.id = p.getId();
        this.naziv = p.getNazivPostaje();
        this.oznaka = p.getOznakaPostaje();
        this.g_duzina = p.getGeogrDuzina();
        this.g_sirina = p.getGeogrSirina();
        this.nadmorska_visina = p.getNadmorskaVisina();
        
    }
    
    public PostajaDTO(Integer id, String naziv, String oznakaPostaje, Double geogrDuzina, Double geogrSirina, Integer nadmorskaVisina) {
        this.id = id;
        this.naziv = naziv;
        this.oznaka = oznakaPostaje;
        this.g_duzina = geogrDuzina;
        this.g_sirina = geogrSirina;
        this.nadmorska_visina = nadmorskaVisina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getG_duzina() {
        return g_duzina;
    }

    public void setG_duzina(double g_duzina) {
        this.g_duzina = g_duzina;
    }
    

    public double getG_sirina() {
        return g_sirina;
    }

    public void setG_sirina(double g_sirina) {
        this.g_sirina = g_sirina;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public int getNadmorska_visina() {
        return nadmorska_visina;
    }

    public void setNadmorska_visina(int nadmorska_visina) {
        this.nadmorska_visina = nadmorska_visina;
    }
}
