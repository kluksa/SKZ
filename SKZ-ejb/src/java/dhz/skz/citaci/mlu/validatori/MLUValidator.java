/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.mlu.validatori;

import dhz.skz.validatori.*;
import dhz.skz.util.OperStatus;


/**
 *
 * @author kraljevic
 */
public class MLUValidator extends ValidatorImpl {

    public MLUValidator() {
        this.setTemperatura(20.);
    }
    

    @Override
    public int provjeraStatusa(String statusStr) {
        String[] st = statusStr.split(";");
        int ss = Integer.parseInt(st[0]);
        int bs = Integer.parseInt(st[1]);
        int fs = Integer.parseInt(st[2]);
        int nc = Integer.parseInt(st[3]);

        int status = 0;

        if (fs != 0 || ss != 0) {
            status |= (1 << OperStatus.FAULT.ordinal());
        }
        switch ( bs & 15) {
            case 0:
                break;
            case 1:
                status |= (1 << OperStatus.KALIBRACIJA.ordinal());
                break;
            case 2:
                status |= (1 << OperStatus.ZERO.ordinal());
                break;
            case 4:
                status |= (1 << OperStatus.SPAN.ordinal());
                break;
            case 8:
                status |= (1 << OperStatus.ODRZAVANJE.ordinal());
                break;
            default:
                status |= (1 << OperStatus.FAULT.ordinal());
        }
        return status;
    }
}
