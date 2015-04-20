/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.validatori;

import javax.ejb.Stateless;
import dhz.skz.citaci.util.OperStatus;

/**
 *
 * @author kraljevic
 */
@Stateless
public class API100EValidator extends ValidatorImpl {
    /**
     * Parsa ulazni status string i vraca status integer u obliku 
     * or-anog OperStatus-a
     * 
     * @param statusStr string u kojem je enkodirana kompletna informacija o 
     * statusu i pogresci koju prijevljuje DAS
     * @return binarno enkodirani status
     * @see ValidatorImpl
    */
    @Override
    public int provjeraStatusa(String statusStr) {
        int status = 0;
        if (!statusStr.isEmpty()) {
            try {
                int stInt = Integer.parseInt(statusStr, 16);
                if (stInt > 255) {
                    status |= 1 << OperStatus.FAULT.ordinal();
                }
                switch (stInt & 30) {
                    case 2:
                        status |= 1 << OperStatus.ODRZAVANJE.ordinal();
                        break;
                    case 4:
                        status |= 1 << OperStatus.ZERO.ordinal();
                        break;
                    case 8:
                        status |= 1 << OperStatus.SPAN.ordinal();
                        break;
                    case 16:
                        status |= 1 << OperStatus.KALIBRACIJA.ordinal();
                        break;
                    default:
                        status |= 1 << OperStatus.FAULT.ordinal();
                }
            } catch (NumberFormatException ex) {
                status |= 1 << OperStatus.FAULT.ordinal();
            }
        }
        return status;
    }
}
