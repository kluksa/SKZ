/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.validatori.*;
import dhz.skz.util.OperStatus;

/**
 *
 * @author kraljevic
 */
public class API100EValidator extends ValidatorImpl {

    /**
     * Parsa ulazni status string i vraca status integer u obliku or-anog
     * OperStatus-a
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
            int stInt = Integer.parseInt(statusStr,16);
            if ((stInt & 0xff) != 0) {
                status |= 1 << OperStatus.FAULT.ordinal();
            }
            if ((stInt & 0x0100) == 0x0100) {
                status |= 1 << OperStatus.KALIBRACIJA.ordinal();
            }
            if ((stInt & 0x0200) == 0x0200) {
                status |= 1 << OperStatus.ODRZAVANJE.ordinal();
            }
            if ((stInt & 0x0400) == 0x0400) {
                status |= 1 << OperStatus.ZERO.ordinal();
            }
            if ((stInt & 0x0800) == 0x0800) {
                status |= 1 << OperStatus.SPAN.ordinal();
            }
            if ((stInt & 0x1000) == 0x1000) {
                status |= 1 << OperStatus.FAULT.ordinal();
            }
            if ((stInt & 0x8000) == 0x8000) {
                status |= 1 << OperStatus.FAULT.ordinal();
            }
        }
        return status;
    }
}
