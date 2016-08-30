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
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.validatori.Validator;
import java.io.IOException;
import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class MjerenjaIoxParser extends IoxAbstractCitac<PodatakSirovi> {

    static final Logger log = Logger.getLogger(MjerenjaIoxParser.class.getName());

    private final NavigableMap<Date, Double> temperature = new TreeMap<>();
    
    public MjerenjaIoxParser(){
        super();
        this.setPeriodStr("1");
        this.setdBstr("av1.txt");
    }

    @Override
    protected void validiraj() {
        for (PodatakSirovi ps : getPodaci()) {
            Validator v = this.getValidatori().get(ps.getProgramMjerenjaId());
            v.setTemperatura(temperature.get(ps.getVrijeme()));
            v.validiraj(ps);
        }
    }

    @Override
    protected void parseLinija() throws IOException {
        CsvReader csv = getCsv();
        log.log(Level.INFO, "POD={0}", new Object[]{csv.getRawRecord()});
        try {
            if (!csv.get("Mean").isEmpty()) {
                Double vrijednost = getVrijednost(csv.get("Mean"), csv.get("Unit"), getAktualniProgram());
                Integer validity = Integer.parseInt(csv.get("Validity"));
                String statusStr = csv.get("ErrStatus") + csv.get("OpeStatus") + csv.get("IntStatus");
                PodatakSirovi ps = new PodatakSirovi();
                ps.setVrijednost(vrijednost);
                ps.setVrijeme(getAktualnoVrijeme());
                ps.setProgramMjerenjaId(getAktualniProgram());
                ps.setStatus(0);
                ps.setNivoValidacijeId(0);
                ps.setStatusString(statusStr);
                dodajPodatak(ps);
                
                if (getAktualniProgram().getKomponentaId().getFormula().equals("Tkont")) {
                    temperature.put(getAktualnoVrijeme(), vrijednost);
                }
            }
        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        
    }

    private Double getVrijednost(String vrijednostStr, String jedinica, ProgramMjerenja pm) {
        Double val = Double.parseDouble(vrijednostStr);
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
        return val * conv;
    }

    @Override
    protected void odrediVrijemeZadnjegPodatka() {
        PodatakSiroviFacade psf = (PodatakSiroviFacade) this.getDao();
        for ( ProgramMjerenja pm : this.getMapa().values()){
            PodatakSirovi zps = psf.getZadnji(pm);
            if ( zps != null && zps.getVrijeme().after(this.getVrijemeZadnjeg())) {
                this.setVrijemeZadnjeg(zps.getVrijeme());
            }
        }
    }
}
