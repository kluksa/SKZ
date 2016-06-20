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
package dhz.skz.citaci.Iskaz;

import com.csvreader.CsvReader;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.citaci.Iskaz.validatori.IskazValidatorFactory;
import dhz.skz.util.OperStatus;
import dhz.skz.validatori.Validator;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 *
 * @author kraljevic
 */
public class PostajaCitacIskaz {

    private static final Logger log = Logger.getLogger(IskazCitacBean.class.getName());

    private final Postaja postaja;
    private final HashMap<String, ProgramMjerenja> mapa;

    private final NavigableMap<Date, Double> temperature;
    private final Map<ProgramMjerenja, Validator> validatori;
//    private int i = 0;
    private final IskazValidatorFactory ivf;
    private ProgramMjerenja tempIdx;

    public Postaja getPostaja() {
        return postaja;
    }

    public PostajaCitacIskaz(Postaja postajaId, IskazValidatorFactory ivf) {
        this.mapa = new HashMap<>();
        this.validatori = new HashMap<>();
        this.temperature = new TreeMap<>();
        this.postaja = postajaId;
        this.ivf = ivf;
    }

    public void dodajProgram(ProgramMjerenja pm, String uKljuc, String kKljuc) {
        String kljuc = uKljuc + "::" + kKljuc;
        mapa.put(kljuc, pm);
        validatori.put(pm, ivf.getValidator(uKljuc));
        if (pm.getKomponentaId().getFormula().equals("Tkont")) {
            tempIdx = pm;
        }
    }

    private void dodajPodatak(Collection<PodatakSirovi> podaci, String device, String component, Date vrijeme, Double vrijednost, String jedinica, String status) {
        ProgramMjerenja pm = mapa.get(device + "::" + component);
        if (pm == null) {
            log.log(Level.FINE, "Nema programa za {0} :: {1}", new Object[]{device, component});
            return;
        }
        double conv;
        switch (jedinica) {
            case "ppb":
            case "ppm":
            case "nmol/mol":
            case "umol/mol":
                conv = pm.getKomponentaId().getKonvVUM();
                break;
            default:
                conv = 1.;
        }
        PodatakSirovi ps = new PodatakSirovi();
        ps.setVrijednost(vrijednost * conv);
        ps.setVrijeme(vrijeme);
        ps.setProgramMjerenjaId(pm);
        ps.setStatus(0);
        ps.setNivoValidacijeId(0);
        ps.setStatusString(status);
        podaci.add(ps);
        if (ps.getProgramMjerenjaId().equals(tempIdx)) {
            temperature.put(vrijeme, vrijednost);
        }
    }

    public Collection<PodatakSirovi> parseMjerenja(InputStream is) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Collection<PodatakSirovi> podaci = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(is)) {
            JsonArray array = jsonReader.readArray();

            for (JsonValue element : array) {
                try {
                    JsonArray polje = (JsonArray) element;
                    String component = polje.getString(0).trim();
                    Date datum = sdf.parse(polje.getString(1).trim());
                    String unit = polje.getString(2).trim();
                    String device = polje.getString(3).trim();
//            Integer frekvencija = Integer.parseInt(polje.getString(4));
//            Integer obuhvat = Integer.parseInt(polje.getString(5));
//            Integer st1 = Integer.parseInt(polje.getString(6));
//            Integer st2 = Integer.parseInt(polje.getString(7));
//            Integer st3 = Integer.parseInt(polje.getString(8));
                    String status = polje.getString(9).trim();
                    status += polje.getString(10).trim();
                    Double val = Double.parseDouble(polje.getString(11).trim());
//            Float jsonNumber1 = Float.parseFloat(polje.getString(12));
//            Float jsonNumber2 = Float.parseFloat(polje.getString(13));
//            Float jsonNumber3 = Float.parseFloat(polje.getString(14));
//            Float jsonNumber4 = Float.parseFloat(polje.getString(15));
//            Float jsonNumber5 = Float.parseFloat(polje.getString(16));

                    log.log(Level.FINEST, "POD={0}::{1}::{2}::{3}::{4}", new Object[]{datum, device, component, val, status});
                    dodajPodatak(podaci, device, component, datum, val, unit, status);
                } catch (ParseException | NumberFormatException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
        validiraj(podaci);
        return podaci;
    }

    private void validiraj(Collection<PodatakSirovi> podaci) {
        for (PodatakSirovi ps : podaci) {
            Validator v = validatori.get(ps.getProgramMjerenjaId());
            v.setTemperatura(temperature.get(ps.getVrijeme()));
            v.validiraj(ps);
        }
    }

    public Collection<ZeroSpan> parseZeroSpan(InputStream is) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ZeroSpan> zeroSpan = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(is)) {
            JsonArray array = jsonReader.readArray();

            for (JsonValue element : array) {
                try {
                    JsonArray polje = (JsonArray) element;
                    String component = polje.getString(0).trim();

                    Date vrijeme = sdf.parse(polje.getString(1).trim());
                    String device = polje.getString(2).trim();
                    String unit = polje.getString(3).trim();
                    Integer obuhvat = Integer.parseInt(polje.getString(4));
                    Integer st1 = Integer.parseInt(polje.getString(5));
                    Integer st2 = Integer.parseInt(polje.getString(6));
                    Integer st3 = Integer.parseInt(polje.getString(7));
                    String status = polje.getString(9).trim();
                    status += polje.getString(8).trim();
                    String zeroSetp = polje.getString(10).trim();
                    String zeroVal = polje.getString(11).trim();
                    String spanSetp = polje.getString(12).trim();
                    String spanVal = polje.getString(13).trim();
                            

                    ProgramMjerenja pm = mapa.get(device + "::" + component);
                    if (pm != null) {
                        double conv;
                        switch (unit) {
                            case "ug/m3":
                            case "mg/m3":
                                conv = 1. / pm.getKomponentaId().getKonvVUM();
                                break;
                            default:
                                conv = 1.;
                        }

                        log.log(Level.FINEST, "ZS={0}::{1}::{2}::{3}::{4}", new Object[]{vrijeme, device, component, polje.getString(9).trim(), polje.getString(10).trim(), status});
                        Validator v = validatori.get(pm);
                        int statusInt = v.provjeraStatusa(status);
                        int statusMaska = ~( 1 << OperStatus.SPAN.ordinal() | 1 << OperStatus.ZERO.ordinal() |  1 << OperStatus.KALIBRACIJA.ordinal() );

                        if ((statusMaska| statusInt) < (1 << OperStatus.FAULT.ordinal())) {
                            if ((statusInt & (1 << OperStatus.ZERO.ordinal())) != 0 ) {
                                dodajZeroSpan(zeroSpan, pm, vrijeme, conv, zeroVal, zeroSetp, "AZ");
                            }
                            if ((statusInt & (1 << OperStatus.SPAN.ordinal())) != 0 ) {
                                dodajZeroSpan(zeroSpan, pm, vrijeme, conv, spanVal, spanSetp, "AS");
                            }
                        }
                    }
                } catch (ParseException | NumberFormatException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
        return zeroSpan;

    }

    private void dodajZeroSpan(List<ZeroSpan> zeroSpan, ProgramMjerenja pm, Date vrijeme, double conv, String val, String setp, String vrsta) {
        if (val == null || val.isEmpty()) {
            return;
        }
        ZeroSpan zs = new ZeroSpan();
        zs.setVrijeme(vrijeme);
        zs.setProgramMjerenjaId(pm);
        zs.setVrijednost(conv * Double.parseDouble(val));
        if (setp != null && !setp.isEmpty()) {
            zs.setReferentnaVrijednost(conv*Double.parseDouble(setp));
        }
        zs.setVrsta(vrsta);
        zeroSpan.add(zs);
    }
}
