/*
 * Copyright (C) 2015 kraljevic
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
package dhz.skz.citaci.weblogger.validatori;

import dhz.skz.util.OperStatus;
import dhz.skz.validatori.ValidatorImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public abstract class APIValidator extends ValidatorImpl{

    protected Map<Integer, String> MY_MAP = createMap();
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

    @Override
    public Collection<String> opisStatusa(String statusStr) {
        int parseInt = Integer.parseInt(statusStr.trim(), 16);
        List<String> statusi = new ArrayList<>();
        for (int i=0; i<16; i++ ){
            if ((parseInt & 1) == 1 ) statusi.add(MY_MAP.get(i));
            parseInt =  parseInt >> 1;
        }
        return statusi;
    }
    
    protected abstract Map<Integer, String> createMap();
    
}
