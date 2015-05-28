/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

import java.io.Serializable;

/**
 *
 * @author kraljevic
 */
public class PodatakSiroviDTO implements Serializable {
    private Integer id;
    private long vrijeme;
    private Double vrijednost;
    private String statusString;
    private Integer statusInt;
    private Boolean valjan;

    public Integer getStatusInt() {
        return statusInt;
    }

    public void setStatusInt(Integer statusInt) {
        this.statusInt = statusInt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(long vrijeme) {
        this.vrijeme = vrijeme;
    }

    public Double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(Double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Boolean isValjan() {
        return valjan;
    }

    public void setValjan(Boolean valjan) {
        this.valjan = valjan;
    }
    
}
