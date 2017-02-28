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
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.util.OperStatus;
import dhz.skz.validatori.Validator;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kraljevic
 */
public class ZeroSpanIoxParser extends IoxAbstractCitac<ZeroSpan> {

    private static final Logger log = Logger.getLogger(ZeroSpanIoxParser.class.getName());

    public ZeroSpanIoxParser() {
        super();
        this.setPeriodStr("24");
        this.setdBstr("cal.txt");
    }

    @Override
    protected void parseLinija() throws IOException {
        CsvReader csv = getCsv();
        try {

            String intstatus = csv.get("IntStatus");
            String vrsta;
            if (intstatus.charAt(12) == 'A') {
                vrsta = "A";
            } else {
                vrsta = "M";
            }
//                        Integer validity = Integer.parseInt(csv.get("Validity"));
            String status = csv.get("ErrStatus");
            status += csv.get("OpeStatus");
            status += csv.get("IntStatus");

            Validator v = this.getValidatori().get(getAktualniProgram());
            int provjeraStatusa = v.provjeraStatusa(status);

            provjeraStatusa &= ~(1 << OperStatus.SPAN.ordinal());
            provjeraStatusa &= ~(1 << OperStatus.ZERO.ordinal());
            provjeraStatusa &= ~(1 << OperStatus.KALIBRACIJA.ordinal());

            if (provjeraStatusa < (1 << OperStatus.FAULT.ordinal())) {
//                        if (csv.get("ErrStatus").equals("________")) {  
                if (intstatus.charAt(11) == 'Z') {
                    parseGrupa("Zero", vrsta.concat("Z"));
                }
                if (intstatus.charAt(10) == '1') {
                    parseGrupa("Span-1", vrsta.concat("S"));
                }
                if (intstatus.charAt(9) == '2') {
                    parseGrupa("Span-2", vrsta.concat("X"));
                }
            }
        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void parseGrupa(String grupaStr, String vrsta) throws IOException {
        CsvReader csv = getCsv();
        if (!csv.get(grupaStr).isEmpty()) {
            Double vrijednost = getVrijednost(csv.get(grupaStr), csv.get("Unit"), getAktualniProgram());
            Double stdev = getVrijednost(csv.get(grupaStr + " StdDev"), csv.get("Unit"), getAktualniProgram());
            Double ref = getVrijednost(csv.get(grupaStr + " Setp"), csv.get("Unit"), getAktualniProgram());
            ZeroSpan ps = new ZeroSpan();
            ps.setVrijednost(vrijednost);
            ps.setVrijeme(getAktualnoVrijeme());
            ps.setProgramMjerenjaId(getAktualniProgram());
            ps.setReferentnaVrijednost(ref);
            ps.setStdev(stdev);
            ps.setVrsta(vrsta);
            getPodaci().add(ps);
        }
    }

    private Double getVrijednost(String vrijednostStr, String jedinica, ProgramMjerenja pm) {
        Double val = Double.parseDouble(vrijednostStr);
        double conv;
        switch (jedinica) {
            case "ug/m3":
            case "mg/m3":
                conv = 1. / pm.getKomponentaId().getKonvVUM();
                break;
            default:
                conv = 1.;
        }
        return val * conv;
    }

    @Override
    protected void validiraj() {
    }
    
    @Override
    protected void odrediVrijemeZadnjegPodatka() {
        // TODO ovdje nesto ne stima. Iz nekog razloga za Varazdin i Karlovac upisuje zero/span dvostruko
        ZeroSpanFacade psf = (ZeroSpanFacade) this.getDao();
        for ( ProgramMjerenja pm : this.getMapa().values()){
            Date zps = psf.getVrijemeZadnjeg(pm);
            if ( zps != null && zps.after(this.getVrijemeZadnjeg())) {
                this.setVrijemeZadnjeg(zps);
            }
        }
        
    }
}
