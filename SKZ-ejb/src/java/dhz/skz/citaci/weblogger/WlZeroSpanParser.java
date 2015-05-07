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
import dhz.skz.aqdb.facades.ZeroSpanFacade;
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
    private final Map<String, ProgramMjerenja> wlKanalProgram;
    private Map<Integer, ProgramMjerenja> wlStupacProgram;

    private int brojStupaca;
    private Date trenutnoVrijeme;
    private Date terminDatoteke;
    private final ZeroSpanFacade zeroSpanFacade;
    private final Collection<ProgramMjerenja> programNaPostaji;

    public WlZeroSpanParser(Collection<ProgramMjerenja> programNaPostaji, TimeZone timeZone, ZeroSpanFacade zeroSpanFacade) {
        this.zeroSpanFacade = zeroSpanFacade;
        this.wlKanalProgram = new HashMap<>();
        this.separator = ',';
        this.chareset = Charset.forName("UTF-8");
        this.programNaPostaji = programNaPostaji;
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
//        if (nizKanala == null) {
//            throw new WlFileException("Niz kanala nije postavljen");
//        }
        setNizKanala();
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

            String modStr = csv.get(stupac);
            String statusStr = csv.get(stupac + 1);
            String certStr = csv.get(stupac + 2);
            String varStr = csv.get(stupac + 3);
            String vrijednostStr = csv.get(stupac + 4);
            if (!modStr.isEmpty()) {
                try {
                    Double iznos = Double.parseDouble(vrijednostStr);
                    Double varijanca = Double.parseDouble(varStr);
                    Double refV = Double.parseDouble(certStr);

                    ZeroSpan pod = new ZeroSpan();
                    pod.setVrijeme(trenutnoVrijeme);
                    pod.setProgramMjerenjaId(program);
                    pod.setVrsta(modStr);
                    pod.setReferentnaVrijednost(refV);
                    pod.setStdev(varijanca);
                    pod.setVrijednost(iznos);
                    zeroSpanFacade.spremi(pod);

                } catch (NumberFormatException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void setNizKanala() {
//        this.nizKanala = nizKanala;
        // mapiranje kanal -> programMjerenja (inverzno mapiranje, jer pm->kanal je 
        // trivijalno jer pm sadrzi wlMap koji sadrzi id kanala
        for (ProgramMjerenja kljuc : programNaPostaji) {
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
    
    @Override
    public void setTerminDatoteke(Date terminDatoteke) {
        this.terminDatoteke = terminDatoteke;
    }

    @Override
    public boolean isDobarTermin() {
        boolean aktivna = false;
        boolean dobroVrijeme;
        for (ProgramMjerenja pm : programNaPostaji) {
            if ((pm.getIzvorProgramKljuceviMap() != null) && (pm.getIzvorProgramKljuceviMap().getUKljuc() != null)) {
                boolean a = terminDatoteke.compareTo(pm.getPocetakMjerenja()) >= 0;
                boolean b = (pm.getZavrsetakMjerenja() == null) || (terminDatoteke.compareTo(pm.getZavrsetakMjerenja()) <= 0);
                aktivna |= (a && b);
            }
        }
        dobroVrijeme = (zadnjiPodatak == null) || (zadnjiPodatak.getTime() - terminDatoteke.getTime() < 24 * 3600 * 1000);
        return aktivna && dobroVrijeme;
    }
}
