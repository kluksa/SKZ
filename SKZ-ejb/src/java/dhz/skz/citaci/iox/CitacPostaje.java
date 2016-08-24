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
package dhz.skz.citaci.iox;

import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.AbstractFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.iox.validatori.IoxValidatorFactory;
import dhz.skz.validatori.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class CitacPostaje {
    private static final Logger log = Logger.getLogger(CitacPostaje.class.getName());
    private final Postaja postaja;
    private final Map<ProgramMjerenja, Validator> validatori = new HashMap<>();
    private final Map<String, ProgramMjerenja> mapa = new HashMap<>();
    private final IoxValidatorFactory ioxValidatorFactory;
    private final IoxKonekcija iocon;

    
    CitacPostaje(Postaja postaja, IoxValidatorFactory ioxValidatorFactory, IoxKonekcija iocon) {
        this.postaja = postaja;
        this.ioxValidatorFactory = ioxValidatorFactory;
        this.iocon = iocon;
    }


    Postaja getPostaja() {
        return postaja;
    }

    void spremiPodatke(PodatakSiroviFacade podatakSiroviFacade, ZeroSpanFacade zeroSpanFacade) {
        //"1", "av1.txt"
        spremi(new MjerenjaIoxParser(), podatakSiroviFacade);
        spremi(new ZeroSpanIoxParser(), podatakSiroviFacade);

    }
    
    void spremi(IoxAbstractCitac citac, AbstractFacade fac) {
        citac.setProgramMapa(mapa);
        citac.setValidatoriMapa(validatori);
        citac.setDaoFacade(fac);
        citac.setKonekcija(iocon);
        citac.procitaj();
        
    }
    
    void dodajPogram(ProgramMjerenja pm) {
        String kljuc = pm.getIzvorProgramKljuceviMap().getUKljuc() + "::" + pm.getIzvorProgramKljuceviMap().getKKljuc();
        mapa.put(kljuc, pm);
        validatori.put(pm, ioxValidatorFactory.getValidator(pm.getIzvorProgramKljuceviMap().getUKljuc()));
        
    }
    
}
