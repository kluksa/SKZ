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

import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.UmjeravanjeHasIspitneVelicine;
import dhz.skz.aqdb.facades.UmjeravanjeHasIspitneVelicineFacade;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class AbstractValidatorFactory {
    private static final Logger log = Logger.getLogger(AbstractValidatorFactory.class.getName());
    
    private UmjeravanjeHasIspitneVelicineFacade umjeravanjeHasIspitneVelicineFacade;
    
    protected Map<ProgramMjerenja, NavigableMap<Date, Validator>> programVrijemeValidatorMapa;
    protected Map<ProgramMjerenja, NavigableMap<Date, Double>> koefAMapa;
    protected Map<ProgramMjerenja, NavigableMap<Date, Double>> koefBMapa;
    protected Map<ProgramMjerenja, NavigableMap<Date, Double>> srzMapa;
    protected Map<ProgramMjerenja, NavigableMap<Date, Double>> opsegMapa;

    public Validator getValidator(ProgramMjerenja pm, Date vrijeme) {
        NavigableMap<Date, Validator> get = programVrijemeValidatorMapa.get(pm);
        Map.Entry<Date, Validator> floorEntry = get.floorEntry(vrijeme);
        try {
            Validator v = programVrijemeValidatorMapa.get(pm).floorEntry(vrijeme).getValue();
            v.setPodaciUmjeravanja(koefAMapa.get(pm).floorEntry(vrijeme).getValue(), koefBMapa.get(pm).floorEntry(vrijeme).getValue(), 3.33f * srzMapa.get(pm).floorEntry(vrijeme).getValue() / koefAMapa.get(pm).floorEntry(vrijeme).getValue(), opsegMapa.get(pm).floorEntry(vrijeme).getValue());
            return programVrijemeValidatorMapa.get(pm).floorEntry(vrijeme).getValue();
        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, "EEEEEEEEEEE: pm(id)={0}, pvvm(size)={1}, pvvm(first)={2}, pvvm(last)={3}", new Object[]{pm.getId(), programVrijemeValidatorMapa.get(pm).size(), programVrijemeValidatorMapa.get(pm).firstKey(), programVrijemeValidatorMapa.get(pm).lastKey()});
            throw ex;
        }
    }

    protected NavigableMap<Date, Double> getAKoefMapa(ProgramMjerenja pm) {
        NavigableMap<Date, Double> mapa = getIspitneVelicineMapa(pm, "A");
        mapa.put(new Date(0L), 1.);
        return mapa;
    }

    protected NavigableMap<Date, Double> getBKoefMapa(ProgramMjerenja pm) {
        NavigableMap<Date, Double> mapa = getIspitneVelicineMapa(pm, "B");
        mapa.put(new Date(0L), 0.);
        return mapa;
    }

    protected NavigableMap<Date, Double> getOpsegMapa(ProgramMjerenja pm) {
        NavigableMap<Date, Double> mapa = getIspitneVelicineMapa(pm, "o");
        mapa.put(new Date(0L), 1000.);
        return mapa;
    }

    protected NavigableMap<Date, Double> getSrzMapa(ProgramMjerenja pm) {
        NavigableMap<Date, Double> mapa = getIspitneVelicineMapa(pm, "Srz");
        mapa.put(new Date(0L), 1.);
        return mapa;
    }

    protected NavigableMap<Date, Double> getIspitneVelicineMapa(ProgramMjerenja pm, String iv) {
        NavigableMap<Date, Double> mapa = new TreeMap<>();
        for (UmjeravanjeHasIspitneVelicine uiv : umjeravanjeHasIspitneVelicineFacade.find(pm, iv)) {
            mapa.put(uiv.getUmjeravanje().getDatum(), uiv.getIznos());
        }
        return mapa;
    }

    public void setUmjeravanjeHasIspitneVelicineFacade(UmjeravanjeHasIspitneVelicineFacade umjeravanjeHasIspitneVelicineFacade) {
        this.umjeravanjeHasIspitneVelicineFacade = umjeravanjeHasIspitneVelicineFacade;
    }
}
