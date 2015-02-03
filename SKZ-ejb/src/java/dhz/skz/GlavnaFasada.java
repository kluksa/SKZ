/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import dhz.skz.aqdb.entity.PrimateljiPodataka;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.CitacMainLocal;
import dhz.skz.diseminacija.DiseminacijaMainBean;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author kraljevic
 */
@Stateless
public class GlavnaFasada implements GlavnaFasadaRemote {
    @EJB
    private CitacMainLocal citacMainBean;

    @EJB
    private DiseminacijaMainBean diseminacijaMain;
//    @EJB
//    private CitacMainBean citacMainBean;

    private static final Logger log = Logger.getLogger(GlavnaFasada.class.getName());

    @Override
    public void pokreniDiseminaciju() {
        log.log(Level.INFO, "Pokrecem diseminaciju");
        diseminacijaMain.pokreni();
    }

    @Override
    public void pokreniCitanje() {
        log.log(Level.INFO, "Pokrecem citace");
        citacMainBean.pokreniCitace();
    }

    @Override
    public void nadoknadiPodatke(PrimateljiPodataka primatelji, Collection<ProgramMjerenja> programi, Date pocetak, Date kraj) {
        log.log(Level.INFO, "Nadoknadjujem podatke");
        diseminacijaMain.nadoknadiPodatke(primatelji, programi, pocetak, kraj);
    }
    
    

}
