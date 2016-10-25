/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.umjeravanje.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kraljevic
 */
@XmlRootElement
public class Crm {
    @NotNull
    private Uredjaj uredjaj;
    private String sljedivost;
    private List<Koncentracija> koncentracija;

    public Uredjaj getUredjaj() {
        return uredjaj;
    }

    public void setUredjaj(Uredjaj uredjaj) {
        this.uredjaj = uredjaj;
    }

    public String getSljedivost() {
        return sljedivost;
    }

    public void setSljedivost(String sljedivost) {
        this.sljedivost = sljedivost;
    }

    public List<Koncentracija> getKoncentracija() {
        return koncentracija;
    }

    public void setKoncentracija(List<Koncentracija> koncentracija) {
        this.koncentracija = koncentracija;
    }
    
    
}
