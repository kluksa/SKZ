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

import dhz.skz.validatori.ValidatorFactory;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.validatori.NulValidator;
import dhz.skz.validatori.Validator;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class WlValidatorFactory extends ValidatorFactory {

    HashMap<String, Validator> valMapa;
    private static final Logger log = Logger.getLogger(WlValidatorFactory.class.getName());

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
        if (pm.getKomponentaId().getVrstaKomponente() == 'K') {
            Uredjaj uredjaj = getUredjaj(pm, vrijeme);
            Validator v = valMapa.get(uredjaj.getModelUredjajaId().getProizvodjacId().getNaziv());
            v.setPodaciUmjeravanja(getA(pm, vrijeme), getB(pm, vrijeme), getDL(pm, vrijeme), getOpseg(pm, vrijeme));
            return v;
        } else {
            return new NulValidator();
        }
    }

    public Validator getValidator(Uredjaj u) {
        String naziv = u.getModelUredjajaId().getProizvodjacId().getNaziv();
        switch (naziv) {
            case "EAS Envimet":
                switch (u.getModelUredjajaId().getOznakaModela()) {
                    case "E100":
                        return new API100EValidator();
                    case "E200":
                        return new API200EValidator();
                    case "E300":
                        return new API300EValidator();
                    case "E400":
                    default:
                        return new API400EValidator();
                }
            default:
                return new GrimmValidator();
        }

    }
}
