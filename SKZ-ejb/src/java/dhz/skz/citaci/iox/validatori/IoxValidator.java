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
        OperStatus st;
        StatusKlasa(Character chr, String opis, OperStatus st) {
            this.chr=chr;
            this.opis=opis;
            this.st=st;
        }
    }
    
    final protected StatusKlasa[] statusMapa = new StatusKlasa[30];

    public IoxValidator() {
        // ErrStatus
        statusMapa[0] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[1] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[2] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[3] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[4] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[5] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[6] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        statusMapa[7] = new StatusKlasa('x', "Nepoznato", OperStatus.FAULT);
        // OperStatus
        statusMapa[8] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[9] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[10] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[11] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[12] = new StatusKlasa('S', "Span", OperStatus.SPAN);
        statusMapa[13] = new StatusKlasa('Z', "Zero", OperStatus.ZERO);
        statusMapa[14] = new StatusKlasa('M', "Maintenence", OperStatus.ODRZAVANJE);
        statusMapa[15] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        // IntStatus
        statusMapa[16] = new StatusKlasa('I', "Initial calibration was active", OperStatus.W2);
        statusMapa[17] = new StatusKlasa('e', "Error during script processing", OperStatus.W2);
        statusMapa[18] = new StatusKlasa('2', "Span 2 invalid", OperStatus.W2);
        statusMapa[19] = new StatusKlasa('1', "Span 1 invalid", OperStatus.W2);
        statusMapa[20] = new StatusKlasa('z', "Zero invalid", OperStatus.W2);
        statusMapa[21] = new StatusKlasa('c', "Calibration invalid", OperStatus.W2);
        statusMapa[22] = new StatusKlasa('o', "Over range", OperStatus.W2);
        statusMapa[23] = new StatusKlasa('u', "Below range", OperStatus.W2);
        statusMapa[24] = new StatusKlasa('S', "Settling to sample",OperStatus.W2);
        statusMapa[25] = new StatusKlasa('2', "Span 2",OperStatus.SPAN);
        statusMapa[26] = new StatusKlasa('1', "Span 1",OperStatus.SPAN);
        statusMapa[27] = new StatusKlasa('Z', "Zero",OperStatus.ZERO);
        statusMapa[28] = new StatusKlasa('A', "Automatic calibration cycle",OperStatus.KALIBRACIJA);
        statusMapa[29] = new StatusKlasa('C', "Status calibration",OperStatus.KALIBRACIJA);
        a = 1.;
        b = 0.;
        ldl = -10.;
        opseg = 1000.;
    }
    
    @Override
    public int provjeraStatusa(String statusStr) {
        int st = 0;
        
        
        if (statusStr.equals("______________________________")) return st;

        for ( int i = 0; i<8; i++) {
            char chr = statusStr.charAt(i);
            if (chr != '_') {
                if (chr == statusMapa[i].chr) {
                    st |= (1 << statusMapa[i].st.ordinal());
                } else {
                    st |= (1 << OperStatus.FAULT.ordinal());
                }
            }
        }
        for ( int i = 8; i<30; i++) {
            char chr = statusStr.charAt(i);
            if (chr != '_') {
                if (chr == statusMapa[i].chr) {
                    st |= (1 << statusMapa[i].st.ordinal());
                } else {
                    st |= (1 << OperStatus.W2.ordinal());
                }
            }
        }
        return st;
    }

    @Override
    public Collection<String> opisStatusa(String statusStr) {
        List<String> statusi = new ArrayList<>();
        statusi.add(statusStr);
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
