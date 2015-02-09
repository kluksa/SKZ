/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import java.util.Date;

/**
 *
 * @author kraljevic
 */
public class ProcitaniPodatak {

    private Date vrijeme;
        
    private String status;

    private Float vrijednost;

    public Float getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Float vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }
}
