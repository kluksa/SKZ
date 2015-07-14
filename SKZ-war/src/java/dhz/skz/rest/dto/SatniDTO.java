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
public class SatniDTO implements Serializable {
    private int programMjerenjaId;
    private long vrijeme;
    private double vrijednost;
    private int status;
    private int obuhvat;
    private boolean valjan;

    public long getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(long vrijeme) {
        this.vrijeme = vrijeme;
    }

    public int getProgramMjerenjaId() {
        return programMjerenjaId;
    }

    public void setProgramMjerenjaId(int programMjerenjaId) {
        this.programMjerenjaId = programMjerenjaId;
    }

    public double getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(double vrijednost) {
        this.vrijednost = vrijednost;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getObuhvat() {
        return obuhvat;
    }

    public void setObuhvat(int obuhvat) {
        this.obuhvat = obuhvat;
    }

    public boolean isValjan() {
        return valjan;
    }

    public void setValjan(boolean valjan) {
        this.valjan = valjan;
    }
    
}
