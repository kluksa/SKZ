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
public class GrimmValidator implements Validator {


    @Override
    public Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException {
        Status s = new Status();
        if (iznos == -999.f) {
            s.dodajFlag(Flag.NEDOSTAJE);
        } else if (!statusStr.isEmpty()) {
            try {
                int stInt = Integer.parseInt(statusStr, 16);
                if (stInt > 255) {
                    s.dodajFlag(Flag.FAULT);
                }
                if ((stInt & 1) == 1) {
                    s.dodajFlag(Flag.MAINTENENCE);
                }
                if ((stInt & 2) == 2) {
                    s.dodajFlag(Flag.ZERO);
                }
                if ((stInt & 4) == 4) {
                    s.dodajFlag(Flag.SPAN);
                }
            } catch (NumberFormatException ex) {
                throw new NevaljanStatusException(ex);
            }
        }
        return s;
    }

}
