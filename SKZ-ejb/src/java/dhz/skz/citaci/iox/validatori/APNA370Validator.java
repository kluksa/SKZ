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
public class APNA370Validator extends Horiba370Validator {
    public APNA370Validator(){
        super();
        statusMapa[8] = new StatusKlasa('x', "Nema", OperStatus.W2);
        statusMapa[9] = new StatusKlasa('x', "Nema", OperStatus.W2);
        statusMapa[10] = new StatusKlasa('x', "Temp ISGG", OperStatus.FAULT);
        statusMapa[11] = new StatusKlasa('x', "Converter", OperStatus.FAULT);
        statusMapa[12] = new StatusKlasa('x', "Press", OperStatus.FAULT);
        statusMapa[13] = new StatusKlasa('x', "Flow", OperStatus.FAULT);
        statusMapa[14] = new StatusKlasa('x', "Battery", OperStatus.W2);
        statusMapa[15] = new StatusKlasa('C', "Calibration", OperStatus.W2);
    }
    
}
