/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import dhz.skz.aqdb.entity.Komponenta;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class KomponentaDTO implements Serializable{
    private Integer id;
    private Short eolOznaka;
    private String isoOznaka;
    private String formula;
    private String naziv;
    private String nazivEng;
    
    public KomponentaDTO(){}

    public KomponentaDTO(Komponenta k) {
        this.id = k.getId();
        this.eolOznaka = k.getEolOznaka();
        this.isoOznaka = k.getIsoOznaka();
        this.formula = k.getFormula();
        this.naziv = k.getNaziv();
        this.nazivEng = k.getNazivEng();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getEolOznaka() {
        return eolOznaka;
    }

    public void setEolOznaka(Short eolOznaka) {
        this.eolOznaka = eolOznaka;
    }

    public String getIsoOznaka() {
        return isoOznaka;
    }

    public void setIsoOznaka(String isoOznaka) {
        this.isoOznaka = isoOznaka;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNazivEng() {
        return nazivEng;
    }

    public void setNazivEng(String nazivEng) {
        this.nazivEng = nazivEng;
    }
    
    
}
