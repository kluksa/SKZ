/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import javax.ejb.Stateless;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Status;

/**
 *
 * @author kraljevic
 */
@Stateless
public class API100EValidator implements Validator {


    @Override
    public Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException {
        Status s = new Status();
        if (iznos == -999.f) {
            s.dodajFlag(Flag.NEDOSTAJE);
        } 
        if (!statusStr.isEmpty()) {
            try {
                int stInt = Integer.parseInt(statusStr, 16);
                if (stInt > 255) {
                    s.dodajFlag(Flag.FAULT);
                }
                if ((stInt & 2) == 2) {
                    s.dodajFlag(Flag.MAINTENENCE);
                }
                if ((stInt & 4) == 4) {
                    s.dodajFlag(Flag.ZERO);
                }
                if ((stInt & 8) == 8) {
                    s.dodajFlag(Flag.SPAN);
                }
                if ((stInt & 16) == 16) {
                    s.dodajFlag(Flag.CALIBRATION);
                }

            } catch (NumberFormatException ex) {
                throw new NevaljanStatusException(ex);
            }
        }
        return s;
    }
}
