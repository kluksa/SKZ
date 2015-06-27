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
package dhz.skz.citaci.mlu;

import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.mlu.validatori.MLUValidatorFactory;
import dhz.skz.diseminacija.dem.LokalnaZona;
import dhz.skz.validatori.Validator;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class MjerenjaPrihvat implements OmotnicaPrihvat {

    private static final Logger log = Logger.getLogger(MjerenjaPrihvat.class.getName());
    private final PodatakSiroviFacade podatakSiroviFacade;
    private MLUValidatorFactory validatorFactory;
    private final ProgramMjerenjaFacade programMjerenjaFacade;
    private Map<Integer, ProgramMjerenja> mapa;

    private CsvOmotnica omotnica;
    private IzvorPodataka izvor;
    private Postaja postaja;

    MjerenjaPrihvat(ProgramMjerenjaFacade programMjerenjaFacade, PodatakSiroviFacade podatakSiroviFacade) {
        this.podatakSiroviFacade = podatakSiroviFacade;
        this.programMjerenjaFacade = programMjerenjaFacade;
    }

    @Override
    public void prihvati(CsvOmotnica omotnica, Postaja postaja, IzvorPodataka izvor) {
        this.omotnica = omotnica;
        this.postaja = postaja;
        this.izvor = izvor;
        this.mapa = new HashMap<>();
        this.validatorFactory = new MLUValidatorFactory(izvor.getProgramMjerenjaCollection());

        Collection<PodatakSirovi> podaci = new ArrayList<>();
        log.log(Level.INFO, "MLU omotnica od: {0} do {1}", new Object[]{
            new Date(omotnica.getVremena().get(0)), new Date(omotnica.getVremena().get(omotnica.getVremena().size()-1))});

        parseHeaders(omotnica.getHeaderi());

        Iterator<Long> it = omotnica.getVremena().iterator();
        for (String[] linija : omotnica.getLinije()) {
            Date vrijeme = new Date(it.next());
            parseLinija(linija, vrijeme, podaci);
        }
        podatakSiroviFacade.spremi(podaci);
    }

    private void parseHeaders(String[] headeri) {
        for (int i = 1; i < headeri.length; i += 5) {
            String str = headeri[i];
            String datoteka = omotnica.getDatoteka();
            ProgramMjerenja pm = programMjerenjaFacade.find(postaja, izvor, str, datoteka);
            if (pm != null) {
                mapa.put(i, pm);
            }
        }
    }

    private void parseLinija(String[] linija, Date vrijeme, Collection<PodatakSirovi> podaci) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(LokalnaZona.getZone());
            Date datum = df.parse(linija[0]);
            if ( ! datum.equals(vrijeme) ) {
                log.log(Level.SEVERE, "Problem sa vremenom: {0}, {1}, {2}", new Object[]{linija[0], vrijeme, datum});
            }
        } catch (ParseException ex) {
            Logger.getLogger(MjerenjaPrihvat.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Integer i : mapa.keySet()) {
            ProgramMjerenja pm = mapa.get(i);
            Validator v = validatorFactory.getValidator(pm, vrijeme);
            if (!(linija[i].equalsIgnoreCase("null") || linija[i].equals("-9999"))) {
                try {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    if(linija[i].contains(",")) { 
                        symbols.setDecimalSeparator(',');
                    } else {
                        symbols.setDecimalSeparator('.');
                    }
                    DecimalFormat format = new DecimalFormat("#.###");
                    format.setDecimalFormatSymbols(symbols);
                    Float vrijednost = format.parse(linija[i]).floatValue();

                    PodatakSirovi ps = new PodatakSirovi();
                    ps.setProgramMjerenjaId(pm);
                    ps.setVrijeme(vrijeme);
                    ps.setVrijednost(vrijednost);
                    ps.setStatus(0);
                    ps.setNivoValidacijeId(0);

                    String ss = linija[i + 1];
                    String ns = linija[i + 2];
                    String cs = linija[i + 3];
                    String vs = linija[i + 4];
                    ps.setStatusString(ss + ";" + ns + ";" + cs + ";" + vs);

                    v.validiraj(ps);
                    podaci.add(ps);
                } catch (NumberFormatException | ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
