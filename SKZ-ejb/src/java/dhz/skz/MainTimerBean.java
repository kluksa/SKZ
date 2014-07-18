/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dhz.skz;

import dhz.skz.citaci.CitacMainBean;
import dhz.skz.diseminacija.DiseminacijaMain;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author kraljevic
 */
@Singleton
@LocalBean
public class MainTimerBean {
    @EJB
    private DiseminacijaMain diseminacijaMain;
    @EJB
    private CitacMainBean citacMainBean;

    
    private static final Logger log = Logger.getLogger(MainTimerBean.class.getName());
    

    
    @Schedule(minute = "17", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void pokreniDiseminaciju() {
        log.log(Level.INFO, "Pokrecem diseminaciju");
        diseminacijaMain.pokreniDiseminaciju();
    }
    
    @Schedule(minute = "14", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void pokreniCitace() {
        log.log(Level.INFO,"Pokrecem citace" );
        citacMainBean.pokreniCitace();
    }

}
