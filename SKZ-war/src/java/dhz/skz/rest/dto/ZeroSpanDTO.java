/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rest.dto;

/**
 *
 * @author kraljevic
 */
public class ZeroSpanDTO {    
    
    private long vrijeme;
    private Double vrijednost;
    private Character vrsta;
    private Double minDozvoljeno, maxDozvoljeno;


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

    public  Character getVrsta() {
        return vrsta;
    }

    public void setVrsta(Character vrsta) {
        this.vrsta = vrsta;
    }

    public Double getMinDozvoljeno() {
        return minDozvoljeno;
    }

    public void setMinDozvoljeno(Double minDozvoljeno) {
        this.minDozvoljeno = minDozvoljeno;
    }

    public Double getMaxDozvoljeno() {
        return maxDozvoljeno;
    }

    public void setMaxDozvoljeno(Double maxDozvoljeno) {
        this.maxDozvoljeno = maxDozvoljeno;
    }
    
}
