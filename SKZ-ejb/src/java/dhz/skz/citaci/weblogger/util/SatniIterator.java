/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author kraljevic
 */
public class SatniIterator {
    
    private final Calendar trenutni_termin;
    private final Calendar zadnji_termin;

    public SatniIterator(Date pocetak, Date kraj) {
        trenutni_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        trenutni_termin.setTime(pocetak);
        trenutni_termin.set(Calendar.MINUTE, 0);
        trenutni_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);

        zadnji_termin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        zadnji_termin.setTime(kraj);
        zadnji_termin.set(Calendar.MINUTE, 0);
        zadnji_termin.set(Calendar.SECOND, 0);
        trenutni_termin.set(Calendar.MILLISECOND, 0);
    }
    
    public boolean next(){
        trenutni_termin.add(Calendar.HOUR, 1);
        return !trenutni_termin.after(zadnji_termin);
    }
    
    public Date getVrijeme(){
        return trenutni_termin.getTime();
    }
}
