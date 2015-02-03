/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.rs.dto;

/**
 *
 * @author kraljevic
 */
public class ZeroSpanDTO {    
    
    private long vrijeme;
    private float vrijednost;
    private Character vrsta;


    public long getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(long vrijeme) {
        this.vrijeme = vrijeme;
    }

    public float getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(float vrijednost) {
        this.vrijednost = vrijednost;
    }

    public  Character getVrsta() {
        return vrsta;
    }

    public void setVrsta(Character vrsta) {
        this.vrsta = vrsta;
    }
}
