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

    public Horiba370Validator() {
        super();
        statusMapa[0] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[1] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[2] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[3] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
        statusMapa[4] = new StatusKlasa('S', "Span", OperStatus.SPAN);
        statusMapa[5] = new StatusKlasa('Z', "Zero", OperStatus.ZERO);
        statusMapa[6] = new StatusKlasa('M', "Maintenence", OperStatus.ODRZAVANJE);
        statusMapa[7] = new StatusKlasa('x', "Nepoznato", OperStatus.W2);
    }

    @Override
    protected int provjeraStatusa(String ps) {
        int st = 0;

        for (int i = 8; i <= 15; i++) {
            char chr = ps.charAt(i);
            if (chr != '_') {
                if (chr == statusMapa[i].chr) {
                    st |= (1 << statusMapa[i].st.ordinal());
                } else {
                    st |= (1 << OperStatus.FAULT.ordinal());
                }
            }
        }

        st |= super.provjeraStatusa(ps);

        if (ps.substring(0, 7).equals("________")) {
            return st;
        } else {

            for (int i = 0; i <= 7; i++) {
                char chr = ps.charAt(i);
                if (chr != '_') {
                    if (chr == statusMapa[i].chr) {
                        st |= (1 << statusMapa[i].st.ordinal());
                    } else {
                        st |= (1 << OperStatus.W2.ordinal());
                    }
                }
            }
        }

        return st;
    }
}
