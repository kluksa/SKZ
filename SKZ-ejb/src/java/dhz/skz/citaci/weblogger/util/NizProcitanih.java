/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import dhz.skz.aqdb.entity.ZeroSpan;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author kraljevic
 */
public class NizProcitanih {
    
    private NavigableMap<Date, ProcitaniPodatak> podaci;
    private NavigableMap<Date, ZeroSpan> zs;

    public NizProcitanih() {
        this.podaci = new TreeMap<>();
        this.zs = new TreeMap<>();
    }
    

    public NavigableMap<Date, ProcitaniPodatak> getPodaci() {
        return podaci;
    }

    public NavigableMap<Date, ZeroSpan> getZs() {
        return zs;
    }

    public void dodajZeroSpan(Date trenutnoVrijeme, ZeroSpan pod) {
        zs.put(trenutnoVrijeme, pod);
    }

    public void dodajPodatak(Date trenutnoVrijeme, ProcitaniPodatak pod) {
        podaci.put(trenutnoVrijeme, pod);
    }
}
