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

import dhz.skz.validatori.ValidatorFactoryFF;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.validatori.Validator;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author kraljevic
 */
public class WlValidatorFactory extends ValidatorFactoryFF {
    HashMap<String, Validator> valMapa;
    
    public WlValidatorFactory(Collection<ProgramMjerenja> programi) {
        super(programi);
        valMapa = new HashMap<>();
        valMapa.put("EAS Envimet", new API100EValidator());
        valMapa.put("Teledyne API", new API100EValidator());
        valMapa.put("Grimm", new GrimmValidator());
        valMapa.put("Synspec", new SynspecValidator());
    }
    
    @Override
    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        Uredjaj uredjaj = getUredjaj(pm, vrijeme);
        Validator v = valMapa.get(uredjaj.getModelUredjajaId().getProizvodjacId().getNaziv());
        v.setPodaciUmjeravanja(getA(pm, vrijeme), getB(pm, vrijeme), getDL(pm, vrijeme), getOpseg(pm, vrijeme));
        return v;
    }
}
