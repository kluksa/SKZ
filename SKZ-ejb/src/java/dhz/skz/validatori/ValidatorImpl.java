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
package dhz.skz.validatori;

import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.citaci.util.OperStatus;

/**
 *
 * @author kraljevic
 */
public abstract class ValidatorImpl implements Validator {
    
    float a, b, ldl, opseg;
    
    @Override
    public void setPodaciUmjeravanja(float a, float b, float ldl, float opseg){
        this.a = a;
        this.b = b;
        this.ldl = ldl;
        this.opseg = opseg;
    }

    private int provjeraIznosa(float iznos){
        int status = 0;
        iznos = iznos*a+b;
        if ( iznos < -998.) 
            return 1 << OperStatus.NEDOSTAJE.ordinal();
        if (iznos > opseg){
            status |= (1 << OperStatus.OBUHVAT.ordinal());
        }
        if ( iznos < -Math.abs(ldl)) {
            status |= (1 << OperStatus.LDL.ordinal());
        } 
        return status;
    }
    
    private int provjeraOkolisnihUvjeta(float temperatura) {
        if (temperatura < 15 || temperatura > 30) {
            return 1 << OperStatus.OKOLISNI_UVJETI.ordinal();
        }
        return 0;
    }
    
    
    @Override
    public void validiraj(PodatakSirovi ps) {
        int status = 0;
        status |= provjeraIznosa(ps.getVrijednost());
        status |= provjeraOkolisnihUvjeta(0.f);
        status |= provjeraStatusa(ps.getStatusString());
        ps.setStatus(status);
    }
    
    public abstract int provjeraStatusa(String statusStr);
    
}
