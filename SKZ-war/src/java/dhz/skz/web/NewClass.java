/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import dhz.skz.GlavniBeanInterace;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
public abstract class NewClass  {
    @Inject
    private  transient Logger log;
    protected final int[] minute;
    protected int minuta;
    protected boolean aktivan;

    public NewClass() {
        minute = new int[60];
        for (int i=0; i<60; i++){
            minute[i]=i;
        }
    }

    public int getMinuta() {
        return getBean().getMinutaPokretanja();
    }

    public void setMinuta(int minuta) {
        this.minuta = minuta;
    }

    public void zaustavi() {
        getBean().zaustavi();
    }

    public void pokreni() {
        getBean().pokreni();
    }

    public void schedule() {
        getBean().schedule(minuta);
    }

    public boolean isAktivan() {
        return getBean().isAktivan();
    }
    
    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public int[] getMinute() {
        return minute;
    }
    
    protected abstract GlavniBeanInterace getBean();
    
}
