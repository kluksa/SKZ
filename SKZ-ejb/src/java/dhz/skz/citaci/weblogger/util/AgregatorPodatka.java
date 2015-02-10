/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.citaci.weblogger.validatori.Validator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author kraljevic
 */
public class AgregatorPodatka {
    
    EnumMap<Status.ModRada, Float> kumulativnaSuma =  new EnumMap<>(Status.ModRada.class);
    EnumMap<Status.ModRada, Status> kumulativniStatus = new EnumMap<>(Status.ModRada.class);
    EnumMap<Status.ModRada, Integer> broj = new EnumMap<>(Status.ModRada.class);
    Set<Status.ModRada> vrste = EnumSet.noneOf(Status.ModRada.class);

    private final int minimalni_obuhvat = 75;
    private int minimalniBroj;
    private int ocekivaniBroj;
    
    private Validator validator;

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    
    public AgregatorPodatka() {
        this(60);
    }

    public AgregatorPodatka(Integer ocekivaniBroj) {
        this.ocekivaniBroj = ocekivaniBroj;
        this.minimalniBroj =  ocekivaniBroj * minimalni_obuhvat / 100;
    }

    public void reset(){
        kumulativnaSuma.clear();
        kumulativniStatus.clear();
        broj.clear();
    }

    public void dodaj(PodatakSirovi ps) {
        float vrijednost = ps.getVrijednost();
        Status s = validator.getStatus(ps.getStatusString());
        vrste.add(s.getModRada());
        if (!kumulativniStatus.containsKey(s.getModRada())){
            kumulativnaSuma.put(s.getModRada(), 0.f);
            broj.put(s.getModRada(),0);
            Status st = new Status();
            st.dodajFlag(Flag.OBUHVAT);
            kumulativniStatus.put(s.getModRada(), st);
        }
        if ( s.isValid() ){
            kumulativnaSuma.put(s.getModRada(), kumulativnaSuma.get(s.getModRada()) + vrijednost);
            int br = broj.get(s.getModRada())+1;
            broj.put(s.getModRada(), br);
            if ( br >= minimalniBroj) {
               kumulativniStatus.get(s.getModRada()).iskljuciFlag(Flag.OBUHVAT);
            }
        }
        if ( s.getModRada() == Status.ModRada.MJERENJE) {
            kumulativniStatus.get(Status.ModRada.MJERENJE).dodajStatus(s);
        } else  {
            kumulativniStatus.get(Status.ModRada.MJERENJE).dodajStatus(s);
            kumulativniStatus.get(s.getModRada()).dodajStatus(s);
        }
    }
    
    
    
    
    public Float getIznos(Status.ModRada mod){
        if ( broj.get(mod) != 0 ){
            return kumulativnaSuma.get(mod)/broj.get(mod);
        } else return -999.f;
    }
    
    public short getObuhvat(Status.ModRada mod){
        return (short) ((short) 100 * broj.get(mod)/ocekivaniBroj);
    }
    
    public Status getStatus(Status.ModRada mod){
        return kumulativniStatus.get(mod);
    }

    public boolean imaMjerenje() {
        return vrste.contains(Status.ModRada.MJERENJE);
    }

    public boolean imaZero() {
        return vrste.contains(Status.ModRada.ZERO);
    }

    public boolean imaSpan() {
        return vrste.contains(Status.ModRada.SPAN);
    }
}
