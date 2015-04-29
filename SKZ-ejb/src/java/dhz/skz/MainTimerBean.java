/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz;

import dhz.skz.citaci.CitacMainBean;
import dhz.skz.diseminacija.DiseminacijaMainBean;
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

    @EJB
    private DiseminacijaMainBean diseminacijaMain;
    @EJB
    private CitacMainBean citacMainBean;

    private static final Logger log = Logger.getLogger(MainTimerBean.class.getName());

    @Schedule(minute = "23", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void pokreniDiseminaciju() {
        try {
            log.log(Level.INFO, "Pokrecem diseminaciju" );
            
//            diseminacijaMain.pokreni();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }

    @Schedule(minute = "46", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "10", dayOfWeek = "*")
    public void pokreniCitace() {
        log.log(Level.INFO, "Pokrecem citace");
        try {
            citacMainBean.pokreniCitace();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
        log.log(Level.INFO, "Zavrsio sa citacima");
    }

}
