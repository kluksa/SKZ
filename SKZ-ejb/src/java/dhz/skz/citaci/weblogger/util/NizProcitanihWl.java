/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import dhz.skz.aqdb.entity.ZeroSpan;
import java.util.Date;
import java.util.EnumMap;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author kraljevic
 */
public class NizProcitanihWl extends NizProcitanih {
    
    private final NavigableMap<Date, ZeroSpan> zs;

    public NizProcitanihWl() {
        super();
        this.zs = new TreeMap<>();
    }

    public NavigableMap<Date, ZeroSpan> getZs() {
        return zs;
    }

    public void dodajZeroSpan(Date trenutnoVrijeme, ZeroSpan pod) {
        zs.put(trenutnoVrijeme, pod);
    }
    
}
