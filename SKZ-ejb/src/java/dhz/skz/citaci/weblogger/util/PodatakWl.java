/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import dhz.skz.aqdb.entity.Podatak;

/**
 *
 * @author kraljevic
 */
public class PodatakWl extends Podatak {

    public PodatakWl() {
        setStatus(0);
    }

    public void dodajStatus(Flag s) {
        setStatus(getStatus() | (1 << s.ordinal()));
    }

    public void dodajStatus(Status s) {
        setStatus(getStatus() | s.getStatus());
    }

    public void dodajStatus(int i) {
        setStatus(getStatus() | i);
    }

    public boolean isValid() {
        return getStatus() < Flag.NEVALJAN;
    }
}
