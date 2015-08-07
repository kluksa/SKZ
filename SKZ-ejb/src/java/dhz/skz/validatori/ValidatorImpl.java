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
import dhz.skz.util.OperStatus;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author kraljevic
 */
public abstract class ValidatorImpl implements Validator {
    
    protected Double a, b, ldl, opseg;
    private Double temperatura;
    private final int mask = 0b1111100111111000;
    
    
    
    @Override
    public void setTemperatura(Double temperatura){
        this.temperatura = temperatura;
    }
    
    @Override
    public void setPodaciUmjeravanja(Double a, Double b, Double ldl, Double opseg){
        this.a = a;
        this.b = b;
        this.ldl = ldl;
        this.opseg = opseg;
    }

    private int provjeraIznosa(Double iznos){
        int status = 0;
        iznos = iznos*a+b;
        if ( iznos < -998.) 
            return 1 << OperStatus.NEDOSTAJE.ordinal();
        if (iznos > opseg){
            status |= (1 << OperStatus.OPSEG.ordinal());
        }
        if ( iznos < -Math.abs(ldl)) {
            status |= (1 << OperStatus.LDL.ordinal());
        } 
        return status;
    }
    
    private int provjeraOkolisnihUvjeta() {
        if (temperatura < 15 || temperatura > 30) {
            return 1 << OperStatus.OKOLISNI_UVJETI.ordinal();
        }
        return 0;
    }
    
    @Override
    public void validiraj(PodatakSirovi ps) {
        int status  = ps.getStatus() & ~mask;
        status |= provjeraIznosa(ps.getVrijednost());
        status |= provjeraOkolisnihUvjeta();
        status |= provjeraStatusa(ps.getStatusString());
        ps.setStatus(status);
    }
    
    public abstract int provjeraStatusa(String statusStr);

    public abstract Collection<String> opisStatusa(String statusStr);
    
}
