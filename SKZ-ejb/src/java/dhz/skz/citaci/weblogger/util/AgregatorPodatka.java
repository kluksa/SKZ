/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import java.util.EnumMap;

/**
 *
 * @author kraljevic
 */
public class AgregatorPodatka {
    
    EnumMap<Status.ModRada, Float> kumulativnaSuma =  new EnumMap<>(Status.ModRada.class);
    EnumMap<Status.ModRada, Status> kumulativniStatus = new EnumMap<>(Status.ModRada.class);
    EnumMap<Status.ModRada, Integer> broj = new EnumMap<>(Status.ModRada.class);

    private final Integer ocekivaniBroj;

    public AgregatorPodatka() {
        this.ocekivaniBroj = 60;
    }

    public AgregatorPodatka(Integer ocekivaniBroj) {
        this.ocekivaniBroj = ocekivaniBroj;
    }

    public void reset(){
        kumulativnaSuma.clear();
        kumulativniStatus.clear();
        broj.clear();
    }

    public void dodaj(Float vrijednost, Status s) {
        if (!kumulativniStatus.containsKey(s.getModRada())){
            kumulativnaSuma.put(s.getModRada(), 0.f);
            broj.put(s.getModRada(),0);
            kumulativniStatus.put(s.getModRada(), new Status());
        }
        if ( s.isValid() ){
            kumulativnaSuma.put(s.getModRada(), kumulativnaSuma.get(s.getModRada()) + vrijednost);
            broj.put(s.getModRada(), broj.get(s.getModRada())+1);
        }
        if ( s.getModRada() != Status.ModRada.MJERENJE) {
            kumulativniStatus.get(Status.ModRada.MJERENJE).dodajStatus(s);
        }
        kumulativniStatus.get(s.getModRada()).dodajStatus(s);
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
}
