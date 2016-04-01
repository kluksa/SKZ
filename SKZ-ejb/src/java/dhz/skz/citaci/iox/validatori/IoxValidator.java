/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.citaci.iox.validatori;

import dhz.skz.util.OperStatus;
import dhz.skz.validatori.ValidatorImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author kraljevic
 */
public class IoxValidator extends ValidatorImpl{
    protected class StatusKlasa {
        Character  chr;
        String opis;
        StatusKlasa(Character chr, String opis) {
            this.chr=chr;
            this.opis=opis;
        }
    }
    
    final protected StatusKlasa[] statusMapa = new StatusKlasa[30];

    public IoxValidator() {
        statusMapa[16] = new StatusKlasa('I', "Initial calibration was active");
        statusMapa[17] = new StatusKlasa('e', "Error during script processing");
        statusMapa[18] = new StatusKlasa('2', "Span 2 invalid");
        statusMapa[19] = new StatusKlasa('1', "Span 1 invalid");
        statusMapa[20] = new StatusKlasa('z', "Zero invalid");
        statusMapa[21] = new StatusKlasa('c', "Calibration invalid");
        statusMapa[22] = new StatusKlasa('o', "Over range");
        statusMapa[23] = new StatusKlasa('u', "Below range");
        statusMapa[24] = new StatusKlasa('S', "Settling to sample");
        statusMapa[25] = new StatusKlasa('2', "Span 2");
        statusMapa[26] = new StatusKlasa('1', "Span 1");
        statusMapa[27] = new StatusKlasa('Z', "Zero");
        statusMapa[28] = new StatusKlasa('A', "Automatic calibration cycle");
        statusMapa[29] = new StatusKlasa('C', "Status calibration");
        a = 1.;
        b = 0.;
        ldl = -10.;
        opseg = 1000.;
    }
    
    @Override
    protected int provjeraStatusa(String statusStr) {
        int st = 0;
        if (statusStr.equals("______________")) return st;
        
        if ( statusStr.charAt(16) == statusMapa[16].chr ) {
            st |= OperStatus.KALIBRACIJA.ordinal();
        }
        if ( statusStr.charAt(17) == statusMapa[17].chr ) {
            st |= OperStatus.KALIBRACIJA.ordinal();
        }
        if ( statusStr.charAt(18) == statusMapa[18].chr ) {
            st |= OperStatus.ZERO.ordinal();
        }
        if ( statusStr.charAt(19) == statusMapa[19].chr ) {
            st |= OperStatus.SPAN.ordinal();
        }
        if ( statusStr.charAt(20) == statusMapa[20].chr ) {
            st |= OperStatus.SPAN.ordinal();
        }
        if ( statusStr.charAt(21) == statusMapa[21].chr ) {
            st |= OperStatus.KALIBRACIJA.ordinal();
        }
        if ( statusStr.charAt(22) == statusMapa[22].chr ) {
            st |= OperStatus.OPSEG.ordinal();
        }
        if ( statusStr.charAt(23) == statusMapa[23].chr ) {
            st |= OperStatus.OPSEG.ordinal();
        }
        if ( statusStr.charAt(24) == statusMapa[24].chr ) {
            st |= OperStatus.FAULT.ordinal();
        }
        if ( statusStr.charAt(25) == statusMapa[25].chr ) {
            st |= OperStatus.FAULT.ordinal();
        }
        if ( statusStr.charAt(26) == statusMapa[26].chr ) {
            st |= OperStatus.FAULT.ordinal();
        }
        if ( statusStr.charAt(27) == statusMapa[27].chr ) {
            st |= OperStatus.FAULT.ordinal();
        }
        if ( statusStr.charAt(28) == statusMapa[28].chr ) {
            st |= OperStatus.FAULT.ordinal();
        }
        if ( statusStr.charAt(29) == statusMapa[29].chr ) {
            st |= OperStatus.KALIBRACIJA.ordinal();
        }
        return st;
    }

    @Override
    public Collection<String> opisStatusa(String statusStr) {
        List<String> statusi = new ArrayList<>();
        for (int i=0; i<30; i++ ){
            char c = statusStr.charAt(i);
            if (  c != '_') {
                if ( c == statusMapa[i].chr) {
                    statusi.add(statusMapa[i].opis);
                } else {
                    statusi.add("Nepoznati status");
                }
            }
        }
        return statusi;
    }
}
