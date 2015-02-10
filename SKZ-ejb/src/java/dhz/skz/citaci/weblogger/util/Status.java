/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

/**
 *
 * @author kraljevic
 */
public class Status {

    public enum ModRada {MJERENJE, ZERO, SPAN, KALIBRACIJA};
    
    private int status;
    private ModRada modRada;

    public ModRada getModRada() {
        return modRada;
    }

    public void setModRada(ModRada modRada) {
        this.modRada = modRada;
    }
    
    public int getStatus() {
        return status;
    }

    public void dodajStatus(Status s) {
        this.status |= s.getStatus();
    }

    public void iskljuciStatus(Status s) {
        this.status &= ~s.getStatus();
    }

    public void dodajFlag(Flag s) {
        this.status |= 1 << s.ordinal();
    }

    public void iskljuciFlag(Flag s) {
        this.status &= ~1 << (s.ordinal() - 1);
    }

    public boolean isValid() {
        return status < Flag.NEVALJAN;
    }

    public boolean isAktivan(Flag s) {
        return ((this.status & 1 << s.ordinal()) != 0);
    }

    public Status() {
        status = 0;
    }
}
