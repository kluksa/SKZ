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

/**
 *
 * @author kraljevic
 */
public abstract class Horiba370Validator extends IoxValidator {
    
    public Horiba370Validator(){
        super();
        statusMapa[0] = new StatusKlasa('x', "Nepoznato");
        statusMapa[1] = new StatusKlasa('x', "Nepoznato");
        statusMapa[2] = new StatusKlasa('x', "Nepoznato");
        statusMapa[3] = new StatusKlasa('x', "Nepoznato");
        statusMapa[4] = new StatusKlasa('S', "Span");
        statusMapa[5] = new StatusKlasa('Z', "Zero");
        statusMapa[6] = new StatusKlasa('M', "Maintenence");
        statusMapa[7] = new StatusKlasa('x', "Nepoznato");
    }
    @Override
    protected int provjeraStatusa(String ps){
        int st = 0;
        st |= super.provjeraStatusa(ps);
        
        if ( ps.substring(0,7).equals("________")) {
            return st;
        }
        if ( ps.charAt(1) == statusMapa[1].chr ) {
            st |= OperStatus.ODRZAVANJE.ordinal();
        } else if ( ps.charAt(2) == statusMapa[1].chr ) {
            st |= OperStatus.ZERO.ordinal();
        } else if ( ps.charAt(3) == statusMapa[1].chr ) {
            st |= OperStatus.SPAN.ordinal();
        } else {
            st |= OperStatus.FAULT.ordinal();
        }
        return st;
    }
}
