/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.citaci.weblogger.util.NizPodataka;
import com.csvreader.CsvReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.citaci.weblogger.util.PodatakWl;
import dhz.skz.citaci.weblogger.util.Status;
import dhz.skz.citaci.weblogger.validatori.Validator;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.ProgramMjerenja;

/**
 *
 * @author kraljevic
 */
public class WlDatotekaParser {

    private static final Logger log = Logger.getLogger(WlDatotekaParser.class.getName());

    private final Character separator;
    private final Charset chareset;
    private int temperaturaKontejneraStupac;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm z");

    private float temperatura;
    private Date zadnjiPodatak;
    private int brojStupaca;
    private Date trenutnoVrijeme;
    private final Map<String, ProgramMjerenja> wlKanalProgram;

    // mapiranje stupac -> programMjerenja 
    private Map<Integer, ProgramMjerenja> wlStupacProgram;
    private Map<ProgramMjerenja, NizPodataka> nizKanala;

    public WlDatotekaParser( TimeZone tz) {
        this.temperatura = -999.f;
        this.wlKanalProgram = new HashMap<>();
        this.separator = ',';
        this.chareset = Charset.forName("UTF-8");
        sdf.setTimeZone(tz);
    }

    public void setZadnjiPodatak(Date zadnjiPodatak) {
        this.zadnjiPodatak = zadnjiPodatak;
    }
    

    public void setNizKanala(Map<ProgramMjerenja, NizPodataka> nizKanala) {
        this.nizKanala = nizKanala;
        // mapiranje kanal -> programMjerenja (inverzno mapiranje, jer pm->kanal je 
        // trivijalno jer pm sadrzi wlMap koji sadrzi id kanala
        for (ProgramMjerenja kljuc : nizKanala.keySet()) {
            if (kljuc.getIzvorProgramKljuceviMap() != null) {
                IzvorProgramKljuceviMap ipm = kljuc.getIzvorProgramKljuceviMap();
                if (ipm == null || ipm.getKKljuc().isEmpty()) {
                    log.log(Level.SEVERE, "izvor_program_kljucevi_map(program_mjerenja_id = {0}) ne sadrzi K Kljuc", kljuc.getId());
                } else {
                    wlKanalProgram.put(kljuc.getIzvorProgramKljuceviMap().getKKljuc(), kljuc);
                }
            } else {
                log.log(Level.SEVERE, "izvor_program_kljucevi_map ne sadrzi program_mjerenja_id = {0}", kljuc.getId());
            }
        }
    }

    public void parse(InputStream fileStream) throws WlFileException, IOException {
//        log.log(Level.INFO, "Pocinjem parsati {0} od {1}", new Object[]{
//            nizKanala.getPostaja().getNazivPostaje(), zadnjiPodatak});
        if (nizKanala == null) {
            throw new WlFileException("Niz kanala nije postavljen");
        }

        CsvReader csv = new CsvReader(fileStream, separator, chareset);
        try {
            parsaj_zaglavlje(csv);

            while (csv.readRecord()) {
                int nc = csv.getColumnCount();
                if (brojStupaca != nc) {
                    log.log(Level.SEVERE,"Promijenio se broj stupaca kod zapisa {0}", csv.getRawRecord());
                    return;
                }
                try {
                    trenutnoVrijeme = sdf.parse(csv.get(0) + " " + csv.get(1) + " " + csv.get(brojStupaca - 1));
                    if (trenutnoVrijeme.after(zadnjiPodatak)) {
                        parsaj_record(csv);
                    }
                } catch (ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            csv.close();
        }
//        log.log(Level.INFO, "Procitao  {0} do {1}", new Object[]{
//            nizKanala.getPostaja().getNazivPostaje(), trenutnoVrijeme});
    }

    private void parsaj_zaglavlje(CsvReader csv) throws IOException {
        /*
         Logika:
         - nisu svi stupci (kanali) u datoteci bitni, parsamo samo neke
         - vani drzimo mapiranje kanal -> PodatakKljuc (u stvari postaja, komponenta, usporedno) 
         kako god tu klasu zvali
         - treba naci koji su stupci bitni i napraviti mapiranje
         stupac -> PodatakKljuc
         - prica sa temperaturom je jos nedovrsena, ne znam kako ju dovrsiti
         opcija je da temperatura bude obicna komponenta, ali onda validator
         treba lijepiti status kad je prekoracena odmah u parasaj_record,
         u svakom slucaju treba znati unaprijed koji je stupac sa temperaturom
         */
        final Pattern pattern = Pattern.compile("^(\\d{1,3})-");
        final Pattern tempKontejnerPtrn = Pattern.compile("Enc.Temp");

        temperaturaKontejneraStupac = -1;
        wlStupacProgram = new HashMap<>();

        csv.readHeaders();
        brojStupaca = csv.getHeaderCount();

        String[] headers = csv.getHeaders();
        for (int j = 2; j < headers.length; j = j + 2) {
            Matcher tmpKM = tempKontejnerPtrn.matcher(headers[j]);
            Matcher matcher = pattern.matcher(headers[j]);
            if (matcher.find()) {
                String kanalBr = matcher.group(1).trim();
                if (wlKanalProgram.containsKey(kanalBr)) {
                    wlStupacProgram.put(j, wlKanalProgram.get(kanalBr));
                }
            } else if (tmpKM.find()) {
                temperaturaKontejneraStupac = j;
            }
        }
    }

    private void parsaj_record(CsvReader csv) throws IOException {
        if (temperaturaKontejneraStupac > 0) {
            try {
                String tmpStr = csv.get(temperaturaKontejneraStupac);

                if (!tmpStr.isEmpty()) {
                    temperatura = Float.parseFloat(tmpStr);
                }
            } catch (NumberFormatException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
        for (Integer stupac : wlStupacProgram.keySet()) {
            NizPodataka nizPodataka = nizKanala.get(wlStupacProgram.get(stupac));
            NavigableMap<Date, Validator> validatori = nizPodataka.getValidatori();

            String iznosStr = csv.get(stupac);
            String statusStr = csv.get(stupac + 1);
            if (!iznosStr.isEmpty()) {
                try {
                    Float iznos = Float.parseFloat(iznosStr);
                    Map.Entry<Date, Validator> floorEntry = validatori.floorEntry(trenutnoVrijeme);
                    Validator value = floorEntry.getValue();
                    Status status = value.getStatus(iznos, statusStr, temperatura);
//                    Status status = validatori.floorEntry(trenutnoVrijeme).getValue()
//                            .getStatus(iznos, statusStr, temperatura);

                    PodatakWl pod = new PodatakWl();
                    pod.setVrijeme(trenutnoVrijeme);
                    pod.setStatus(status.getStatus());
                    pod.setVrijednost(iznos);
                    nizPodataka.dodajPodatak(trenutnoVrijeme, pod);

                } catch (NumberFormatException | NevaljanStatusException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
