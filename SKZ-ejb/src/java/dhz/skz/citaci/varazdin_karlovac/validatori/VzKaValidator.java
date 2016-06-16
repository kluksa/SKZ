/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.varazdin_karlovac.validatori;

import dhz.skz.validatori.*;
import dhz.skz.util.OperStatus;
import java.util.Collection;

/**
 *
 * @author kraljevic
 */
public class VzKaValidator extends ValidatorImpl {

    public VzKaValidator() {
        this.setTemperatura(20.);
    }

    @Override
    public int provjeraStatusa(String statusStr) {
        String[] st = statusStr.split(";");
        String os = st[0];
        String fs = st[1];

        int status = 0;

        if (!fs.equals("N")) {
            status |= (1 << OperStatus.FAULT.ordinal());
        }

        if (!os.equals("N")) {
            if (os.contains("Z")) {
                status |= (1 << OperStatus.ZERO.ordinal());

            }
            if (os.contains("S")) {
                status |= (1 << OperStatus.SPAN.ordinal());
            }
            if (os.contains("M")) {
                status |= (1 << OperStatus.ODRZAVANJE.ordinal());
            }
        }
        return status;
    }

    @Override
    public Collection<String> opisStatusa(String statusStr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
