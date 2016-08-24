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

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.iox.validatori.IoxValidatorFactory;
import dhz.skz.validatori.Validator;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class IoxCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(IoxCitacBean.class.getName());

    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private IoxValidatorFactory ioxValidatorFactory;

    @Override
    public Boolean napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");

        Map<Postaja, CitacPostaje> postajeSve = new HashMap<>();

        for (ProgramMjerenja programMjerenja : izvor.getProgramMjerenjaCollection()) {
            IzvorProgramKljuceviMap ipm = programMjerenja.getIzvorProgramKljuceviMap();
            if (ipm != null) {
                Postaja postajaId = programMjerenja.getPostajaId();
                if (!postajeSve.containsKey(postajaId)) {
                    postajeSve.put(postajaId, citacBuilder(postajaId, izvor));
                }
                CitacPostaje piox = postajeSve.get(postajaId);
                piox.dodajPogram(programMjerenja);
            }
        }

        for (CitacPostaje pio : postajeSve.values()) {
            log.log(Level.INFO, "CITAM POSTAJU: {0}", pio.getPostaja().getNazivPostaje());
            Postaja aktivnaPostaja = pio.getPostaja();
            if (aktivnaPostaja.getNetAdresa() != null && !aktivnaPostaja.getNetAdresa().isEmpty()) {
                IoxKonekcija iocon = new IoxKonekcija(izvor.getUri(), aktivnaPostaja.getNetAdresa());
                pio.spremiPodatke(podatakSiroviFacade, zeroSpanFacade);
            } else {
                log.log(Level.SEVERE, "Postaja {0} nema definiranu adresu", aktivnaPostaja.getNazivPostaje());
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
        return true;
    }
    
    CitacPostaje citacBuilder(Postaja postaja, IzvorPodataka izvor){
        IoxKonekcija iocon = new IoxKonekcija(izvor.getUri(), postaja.getNetAdresa());
        CitacPostaje cp = new CitacPostaje(postaja, ioxValidatorFactory, iocon);
        return cp;
    }

    @Override
    public SortedMap<String, String> opisiStatus(PodatakSirovi ps) {

        SortedMap<String, String> mapa = new TreeMap<>();
        IoxValidatorFactory ioxValidatorFactory = new IoxValidatorFactory();
        Validator validator = ioxValidatorFactory.getValidator(ps.getProgramMjerenjaId().getIzvorProgramKljuceviMap().getUKljuc());
        Collection<String> opisStatusa = validator.opisStatusa(ps.getStatusString());
        Integer i = 0;
        for (String s : opisStatusa) {
            mapa.put(i.toString(), s);
            i++;
        }
        return mapa;
    }
}
