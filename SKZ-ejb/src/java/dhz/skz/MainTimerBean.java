/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

/**
 *
 * @author kraljevic
 */
@Singleton
@LocalBean
@TransactionAttribute(NOT_SUPPORTED)
public class MainTimerBean {

    private static final Logger log = Logger.getLogger(MainTimerBean.class.getName());
    @EJB
    private GlavnaFasadaRemote glavnaFasada;
    
    private boolean diseminacijaAktivna = false, citanjeAktivno = false;

    @Schedule(minute = "37", second = "0", dayOfMonth = "1", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void pokreniDiseminaciju() {
        if (!diseminacijaAktivna) {
            try {
                log.log(Level.INFO, "Pokrecem diseminaciju");
                diseminacijaAktivna = true;
            glavnaFasada.pokreniDiseminaciju();
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            } finally {
                diseminacijaAktivna = false;
                log.log(Level.INFO, "Zavrsio sa diseminacijom");
            }
        } else {
            log.log(Level.INFO, "Diseminacija se vec provodi");
        }
    }

    @Schedule(minute = "27", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void pokreniCitace() {
        if (!citanjeAktivno) {
            try {
                log.log(Level.INFO, "Pokrecem citace");
                citanjeAktivno = true;
                glavnaFasada.pokreniCitanje();
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            } finally {
                citanjeAktivno = false;
                log.log(Level.INFO, "Zavrsio sa citacima");
            }
        } else {
            log.log(Level.INFO, "Citanje se vec provodi");
        } 

    }
}
