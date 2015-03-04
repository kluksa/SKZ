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
    
    public int toInt() {
        return status;
    }

    public void dodajStatus(Status s) {
        this.status |= s.toInt();
    }

    public void iskljuciStatus(Status s) {
        this.status &= ~s.toInt();
    }

    public void dodajFlag(Flag flag) {
        this.status |= 1 << flag.ordinal();
    }

    public void iskljuciFlag(Flag flag) {
        this.status &= ~1 << flag.ordinal();
    }

    public boolean isValid() {
        return status < Flag.NEVALJAN;
    }

    public boolean isAktivan(Flag flag) {
        return ((this.status & 1 << flag.ordinal()) != 0);
    }

    public Status() {
        status = 0;
    }
}
