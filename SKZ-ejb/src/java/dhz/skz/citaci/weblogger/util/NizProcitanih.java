/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author kraljevic
 */
public class NizProcitanih {
    protected NavigableMap<Date, ProcitaniPodatak> pod;

    public NizProcitanih() {
        pod = new TreeMap<>();
    }

    public NavigableMap<Date, ProcitaniPodatak> getPodaci() {
        return pod;
    }

    public void dodajPodatak(Date trenutnoVrijeme, ProcitaniPodatak podatak) {
        pod.put(trenutnoVrijeme, podatak);
    }
}
