/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz.web;

import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author kraljevic
 */
@ManagedBean
@Named(value = "indexMB")
@ViewScoped
public class IndexMB implements Serializable {
    @Inject
    private  transient Logger log;
    @EJB
    private PostajaFacade postajaFacade;
    private List<Postaja> postaje;
    private List<Podatak> podaci;
    private Map<Postaja, Date> vrijemeZadnjeg;
    private Date sada;
    /**
     * Creates a new instance of IndexMB
     */
    public IndexMB() {
        sada = new Date();
    }

    @PostConstruct
    public void init() {
        podaci = new ArrayList<>();
        postaje = postajaFacade.findAll();
        vrijemeZadnjeg = new HashMap<>();
        sada = new Date();
        for ( Postaja p : postaje) {
            Date zadnji = postajaFacade.getZadnjeVrijeme(p);
            if ( zadnji != null ) {
                vrijemeZadnjeg.put(p, zadnji);
            }
        }
    }
    
    public void klik(){
        sada = new Date();
    }

    public Map<Postaja, Date> getVrijemeZadnjeg() {
        return vrijemeZadnjeg;
    }

    public void setVrijemeZadnjeg(Map<Postaja, Date> vrijemeZadnjeg) {
        this.vrijemeZadnjeg = vrijemeZadnjeg;
    }
    
    public List<Map.Entry<Postaja,Date>> getVremena(){
        Set<Map.Entry<Postaja, Date>> productSet = vrijemeZadnjeg.entrySet();
    return new ArrayList<>(productSet);
        
    }

    public List<Postaja> getPostaje() {
        return postaje;
    }

    public List<Podatak> getPodaci() {
        return podaci;
    }

    public Date getSada() {
        return sada;
    }

    public String stil(Date d) {
        if ( d == null ) return "";
        long razlika = ( sada.getTime()-d.getTime() ) / 1000;
        if ( razlika < 3600 ) return "background:#86e395"; //zelena
        if ( razlika < 7200 ) return "background:#fbfe5b"; //zuta

        return "background:#fe8888"; //crvena
        
    }
    
    public String razlika(Date vrijeme) {
        long sekunde = ( sada.getTime()-vrijeme.getTime() ) / 1000;
        long dani = sekunde / 86400;
        sekunde = sekunde % 86400;
        long sati = sekunde / 3600;
        sekunde = sekunde % 3600;
        long minute =  sekunde / 60;
        sekunde = sekunde % 60;
        String str = dani + "D " + sati + "H "+minute +"M "+ sekunde + "S";  
        return str;
    }
    
}
