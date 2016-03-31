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

import com.csvreader.CsvReader;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.iox.validatori.IoxValidatorFactory;
import dhz.skz.validatori.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class PostajaCitacIox {

    private static final Logger log = Logger.getLogger(IoxCitacBean.class.getName());

    private final Postaja postaja;
    private final HashMap<ProgramMjerenja, Integer> program = new HashMap<>();
    private final HashMap<String, ProgramMjerenja> mapa = new HashMap<>();
    private final NavigableMap<Date, PodatakSirovi[]> podaci = new TreeMap<>();
    private final List<Validator> validatori = new ArrayList<>();
    private int i = 0;
    private final IoxValidatorFactory ivf;
    private int tempIdx;

    public Postaja getPostaja() {
        return postaja;
    }

    public PostajaCitacIox(Postaja postajaId, IoxValidatorFactory ivf) {
        this.postaja = postajaId;
        this.ivf = ivf;
    }

    public void dodajProgram(ProgramMjerenja pm, String uKljuc, String kKljuc) {
        program.put(pm, i);
        String kljuc = uKljuc + "::" + kKljuc;
        mapa.put(kljuc, pm);
        validatori.add(i, ivf.getValidator(uKljuc));
        if (uKljuc.equals("Container::Cont")) {
            tempIdx = i;
        }
        i++;
    }

    private void dodajPodatak(String device, String component, Date vrijeme, Float vrijednost, String jedinica, String status) {
        ProgramMjerenja pm = mapa.get(device + "::" + component);
        int ii = program.get(pm);
        if (!podaci.containsKey(vrijeme)) {
            podaci.put(vrijeme, new PodatakSirovi[mapa.size()]);
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
        podaci.get(vrijeme)[ii] = ps;
    }

    public Map<Date, PodatakSirovi[]> parseMjerenja(InputStream is) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
//            line = in.readLine();
//            log.log(Level.INFO,"LL: {0}", line);
            CsvReader csv = new CsvReader(in, '\t');
            csv.readHeaders();
            int n = csv.getHeaderCount();
            while (csv.readRecord()) {
                try {
                    Date vrijeme = sdf.parse(csv.get("Time"));
                    String device = csv.get("Device");
                    String component = csv.get("Component");
                    Float val = Float.parseFloat(csv.get("Mean"));
                    String unit = csv.get("Unit");
                    Integer validity = Integer.parseInt(csv.get("Validity"));
                    String status = csv.get("OpeStatus");
                    status += csv.get("ErrStatus");
                    status += csv.get("IntStatus");
//                    log.log(Level.INFO, "POD={0}::{1}::{2}::{3}::{4}", new Object[]{vrijeme, device, component, val, status});
                    dodajPodatak(device, component, vrijeme, val, unit, status);
                } catch (ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
            validiraj();
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return podaci;
    }

    private void validiraj() {
        for (Date d : podaci.navigableKeySet()) {
            PodatakSirovi[] get = podaci.get(d);
            for (int i = 0; i < get.length; i++) {
                PodatakSirovi ps = get[i];
                Validator v = validatori.get(i);
                v.setTemperatura(get[tempIdx].getVrijednost());
                v.validiraj(ps);
            }
        }
    }
}
