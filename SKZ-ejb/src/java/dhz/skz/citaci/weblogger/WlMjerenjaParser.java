/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import com.csvreader.CsvReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import dhz.skz.validatori.Validator;
import dhz.skz.validatori.ValidatorFactory;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author kraljevic
 */
class WlMjerenjaParser implements WlFileParser {

    private static final Logger log = Logger.getLogger(WlMjerenjaParser.class.getName());

    private final Character separator;
    private final Charset chareset;
    private int temperaturaKontejneraStupac;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm z");

    private Double temperatura;
    private Date zadnjiPodatak;
    private int brojStupaca;
    private Date trenutnoVrijeme;
    private final Map<String, ProgramMjerenja> wlKanalProgram;

    // mapiranje stupac -> programMjerenja 
    private Map<Integer, ProgramMjerenja> wlStupacProgram;
//    private Map<ProgramMjerenja, NizProcitanihWl> nizKanala;
    private final ValidatorFactory validatorFactory;
    private Date terminDatoteke;
    private final PodatakSiroviFacadeLocal podatakSiroviFacade;
    private final Collection<ProgramMjerenja> programNaPostaji;
    private final PodatakFacade podatakFacade;

    public WlMjerenjaParser(Collection<ProgramMjerenja> programNaPostaji, TimeZone tz, ValidatorFactory validatorFactory, PodatakSiroviFacadeLocal podatakSiroviFacade, PodatakFacade podatakFacade) {
        this.temperatura = -999.;
        this.wlKanalProgram = new HashMap<>();
        this.separator = ',';
        this.chareset = Charset.forName("UTF-8");
        sdf.setTimeZone(tz);
        this.validatorFactory = validatorFactory;
        this.podatakSiroviFacade = podatakSiroviFacade;
        this.podatakFacade = podatakFacade;
        this.programNaPostaji = programNaPostaji;
    }

    @Override
    public void setZadnjiPodatak(Date zadnjiPodatak) {
        this.zadnjiPodatak = zadnjiPodatak;
    }

    @Override
    public void parse(InputStream fileStream) throws WlFileException, IOException {
        HashSet<Date> procitanaVremena = new HashSet<>();

        CsvReader csv = new CsvReader(fileStream, separator, chareset);
        setNizKanala();
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
                    if (trenutnoVrijeme.after(zadnjiPodatak) ) {
                        if ( !procitanaVremena.contains(trenutnoVrijeme)) {
                            parsaj_record(csv);
                            procitanaVremena.add(trenutnoVrijeme);
                        } else {
                            log.log(Level.SEVERE, "DUPLICIRANI TERMINI U DATOTECI!!!! {0}::{1}", new Object[]{csv.getCurrentRecord(),csv.getRawRecord()});
                        }
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
                    temperatura = Double.parseDouble(tmpStr);
                }
            } catch (NumberFormatException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
        for (Integer stupac : wlStupacProgram.keySet()) {
            ProgramMjerenja pm = wlStupacProgram.get(stupac);
            Validator v = validatorFactory.getValidator(pm, trenutnoVrijeme);
            String iznosStr = csv.get(stupac);
            String statusStr = csv.get(stupac + 1);
            if (!iznosStr.equals("-999.00") && !iznosStr.isEmpty()) {
                try {
                    Float iznos = Float.parseFloat(iznosStr);
                    PodatakSirovi pod = new PodatakSirovi();
                    pod.setStatus(0);
                    pod.setProgramMjerenjaId(pm);
                    pod.setVrijeme(trenutnoVrijeme);
                    pod.setStatusString(statusStr);
                    pod.setVrijednost(iznos * pm.getKomponentaId().getKonvVUM());
                    v.setTemperatura(temperatura);
                    v.validiraj(pod);
                    podatakSiroviFacade.spremi(pod);
                } catch (NumberFormatException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void setNizKanala() {
        for (ProgramMjerenja pm : programNaPostaji) {
            if (pm.getIzvorProgramKljuceviMap() != null) {
                IzvorProgramKljuceviMap ipm = pm.getIzvorProgramKljuceviMap();
                if (ipm == null || ipm.getKKljuc().isEmpty()) {
                    log.log(Level.SEVERE, "izvor_program_kljucevi_map(program_mjerenja_id = {0}) ne sadrzi K Kljuc", pm.getId());
                } else {
                    wlKanalProgram.put(pm.getIzvorProgramKljuceviMap().getKKljuc(), pm);
                }
            } else {
                log.log(Level.SEVERE, "izvor_program_kljucevi_map ne sadrzi program_mjerenja_id = {0}", pm.getId());
            }
        }
    }

    @Override
    public boolean isDobarTermin() {
        boolean aktivna = false;
        boolean dobroVrijeme;
        for (ProgramMjerenja pm : programNaPostaji) {
            if (pm.getIzvorProgramKljuceviMap() != null) {
                boolean a = terminDatoteke.compareTo(pm.getPocetakMjerenja()) >= 0;
                boolean b = (pm.getZavrsetakMjerenja() == null) || (terminDatoteke.compareTo(pm.getZavrsetakMjerenja()) <= 0);
                aktivna |= (a && b);
            }
        }
        dobroVrijeme = (zadnjiPodatak == null) || (zadnjiPodatak.getTime() - terminDatoteke.getTime() < 24 * 3600 * 1000);
        return aktivna && dobroVrijeme;
    }

    @Override
    public void setTerminDatoteke(Date terminDatoteke) {
        this.terminDatoteke = terminDatoteke;
    }
}
