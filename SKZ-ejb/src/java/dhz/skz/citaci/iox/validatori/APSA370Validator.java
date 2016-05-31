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
public class APSA370Validator extends IoxValidator {
    public APSA370Validator(){
        super();
        statusMapa[0] = new StatusKlasa('x', "Nema", OperStatus.W2);
        statusMapa[1] = new StatusKlasa('L', "Lamp", OperStatus.FAULT);
        statusMapa[2] = new StatusKlasa('x', "Temp ISGG", OperStatus.FAULT);
        statusMapa[3] = new StatusKlasa('x', "Converter H2S", OperStatus.FAULT);
        statusMapa[4] = new StatusKlasa('x', "Press", OperStatus.FAULT);
        statusMapa[5] = new StatusKlasa('x', "Flow", OperStatus.FAULT);
        statusMapa[6] = new StatusKlasa('x', "Battery", OperStatus.W2);
        statusMapa[7] = new StatusKlasa('C', "Calibration", OperStatus.W2);
    }
}
