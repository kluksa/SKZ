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

/**
 *
 * @author kraljevic
 */
public class APMA370Validator extends Horiba370Validator {
    
    public APMA370Validator(){
        super();
        statusMapa[8] = new StatusKlasa('x', "Nema");
        statusMapa[9] = new StatusKlasa('x', "Nema");
        statusMapa[10] = new StatusKlasa('x', "Nema");
        statusMapa[11] = new StatusKlasa('x', "Catalysator");
        statusMapa[12] = new StatusKlasa('x', "Press");
        statusMapa[13] = new StatusKlasa('x', "Flow");
        statusMapa[14] = new StatusKlasa('x', "Battery");
        statusMapa[15] = new StatusKlasa('C', "Calibration");
    }
}
