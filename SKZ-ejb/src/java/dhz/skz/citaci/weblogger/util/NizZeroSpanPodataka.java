/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.citaci.weblogger.util;


import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.ZeroSpan;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class NizZeroSpanPodataka {
    private static final Logger log = Logger.getLogger(NizZeroSpanPodataka.class.getName());
    private ProgramMjerenja kljuc;
    private final NavigableMap<Date, ZeroSpan> podaci;
    private NavigableMap<Date,Uredjaj> uredjaji;

    public NizZeroSpanPodataka() {
        this.podaci = new TreeMap<>();
    }

    public NizZeroSpanPodataka(ProgramMjerenja kljuc) {
        this.kljuc = kljuc;
        this.podaci = new TreeMap<>();
    }

    public ProgramMjerenja getKljuc() {
        return kljuc;
    }

    public void setKljuc(ProgramMjerenja kljuc) {
        this.kljuc = kljuc;
    }
    
    public void dodajPodatak(Date vrijeme, ZeroSpan ocitanje) {
        podaci.put(vrijeme, ocitanje);
    }

    public NavigableMap<Date, ZeroSpan> getPodaci() {
        return podaci;
    }

    public NavigableMap<Date, Uredjaj> getUredjaji() {
        return uredjaji;
    }

    public void setUredjaji(NavigableMap<Date, Uredjaj> uredjaji) {
        this.uredjaji = uredjaji;
    }
    
}
