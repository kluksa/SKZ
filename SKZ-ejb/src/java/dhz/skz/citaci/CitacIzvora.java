/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.weblogger.util.NizOcitanja;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public interface CitacIzvora {

    public Map<ProgramMjerenja, NizOcitanja> procitaj(IzvorPodataka izvor);

    public Map<ProgramMjerenja, NizOcitanja> procitaj(IzvorPodataka izvor,
            Map<ProgramMjerenja, Podatak> zadnjiPodatak);
    
//    public Collection<PodatakSirovi> getSirovi(ProgramMjerenja program, 
//            Date pocetak, Date kraj);

    public Collection<PodatakSirovi> dohvatiSirove(ProgramMjerenja program, Date pocetak, Date kraj, boolean p, boolean k);

}
