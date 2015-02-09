/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import com.csvreader.CsvReader;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.citaci.weblogger.util.NizProcitanih;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author kraljevic
 */
class WlZeroSpanParser implements WlFileParser{

    private static final Logger log = Logger.getLogger(WlZeroSpanParser.class.getName());

    private final Character separator;
    private final Charset chareset;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm z");

    private Date zadnjiPodatak;
    private Map<ProgramMjerenja, NizProcitanih> nizKanala;
    private final Map<String, ProgramMjerenja> wlKanalProgram;
    private Map<Integer, ProgramMjerenja> wlStupacProgram;

    private int brojStupaca;
    private Date trenutnoVrijeme;
    private Collection<ProgramMjerenja> aktivniProgram;

    public WlZeroSpanParser(TimeZone timeZone) {
        this.wlKanalProgram = new HashMap<>();
        this.separator = ',';
        this.chareset = Charset.forName("UTF-8");
        sdf.setTimeZone(timeZone);
    }

    @Override
    public void setZadnjiPodatak(Date zadnjiPodatak) {
        this.zadnjiPodatak = zadnjiPodatak;
    }

    @Override
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
                    log.log(Level.SEVERE, "Promijenio se broj stupaca kod zapisa {0}", csv.getRawRecord());
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

        wlStupacProgram = new HashMap<>();

        csv.readHeaders();
        brojStupaca = csv.getHeaderCount();

        String[] headers = csv.getHeaders();
        for (int j = 2; j < headers.length; j += 5) {
            String komponenta = headers[j].toLowerCase();
            if (wlKanalProgram.containsKey(komponenta)) {
                wlStupacProgram.put(j, wlKanalProgram.get(komponenta));
            }
        }
    }

    private void parsaj_record(CsvReader csv) throws IOException {
        for (Integer stupac : wlStupacProgram.keySet()) {
            ProgramMjerenja program = wlStupacProgram.get(stupac);
            NizProcitanih nizPodataka = nizKanala.get(program);

            String modStr = csv.get(stupac);
            String statusStr = csv.get(stupac + 1);
            String certStr = csv.get(stupac + 2);
            String varStr = csv.get(stupac + 3);
            String vrijednostStr = csv.get(stupac + 4);
//            Uredjaj uredjaj = nizPodataka.getUredjaji().floorEntry(trenutnoVrijeme).getValue();
            if (!modStr.isEmpty()) {
                try {
                    Float iznos = Float.parseFloat(vrijednostStr);
                    Float varijanca = Float.parseFloat(varStr);
                    Float refV = Float.parseFloat(certStr);

                    ZeroSpan pod = new ZeroSpan();
                    pod.setVrijeme(trenutnoVrijeme);
                    pod.setProgramMjerenjaId(program);
                    pod.setVrsta(modStr);
                    pod.setReferentnaVrijednost(refV);
                    pod.setStdev(varijanca);
                    pod.setVrijednost(iznos);

                    nizPodataka.dodajZeroSpan(trenutnoVrijeme, pod);

                } catch (NumberFormatException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void setNizKanala(Map<ProgramMjerenja, NizProcitanih> nizKanala, Collection<ProgramMjerenja> aktivniProgram) {
        this.nizKanala = nizKanala;
        // mapiranje kanal -> programMjerenja (inverzno mapiranje, jer pm->kanal je 
        // trivijalno jer pm sadrzi wlMap koji sadrzi id kanala
        for (ProgramMjerenja kljuc : aktivniProgram) {
            if (kljuc.getIzvorProgramKljuceviMap() != null) {
                IzvorProgramKljuceviMap ipm = kljuc.getIzvorProgramKljuceviMap();
                if (ipm == null || ipm.getUKljuc() == null || ipm.getUKljuc().isEmpty()) {
                    log.log(Level.SEVERE, "izvor_program_kljucevi_map(program_mjerenja_id = {0}) ne sadrzi K Kljuc", kljuc.getId());
                } else {
                    wlKanalProgram.put(ipm.getUKljuc().toLowerCase(), kljuc);
                    log.log(Level.INFO, "Zero/span {0} : {1} : {2}", new Object[]{kljuc.getPostajaId().getNazivPostaje(), ipm.getUKljuc(),
                        kljuc.getKomponentaId().getFormula()});
                }
            } else {
                log.log(Level.SEVERE, "izvor_program_kljucevi_map ne sadrzi program_mjerenja_id = {0}", kljuc.getId());
            }
        }
    }
}
