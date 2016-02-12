/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.varazdin_karlovac;

import com.csvreader.CsvReader;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
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
class VzKaZeroSpanParser implements WlFileParser {

    private static final Logger log = Logger.getLogger(VzKaZeroSpanParser.class.getName());

    private final Character separator;
    private final Charset chareset;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Date zadnjiPodatak;
    private final Map<String, ProgramMjerenja> programKljucMap;
    private Map<Integer, ProgramMjerenja> stupacProgramMap;
    private final ZeroSpanFacade zeroSpanFacade;
    private int brojStupaca;
    private Date trenutnoVrijeme;
    private Date terminDatoteke;
    private final Collection<ProgramMjerenja> programNaPostaji;
    private Integer nivo;
    private Map<Integer, String> modMapa;

    public VzKaZeroSpanParser(Collection<ProgramMjerenja> programNaPostaji, TimeZone timeZone, ZeroSpanFacade zeroSpanFacade) {
        this.zeroSpanFacade = zeroSpanFacade;
        this.programKljucMap = new HashMap<>();
        this.separator = ';';
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
                    if (zadnjiPodatak == null || trenutnoVrijeme.after(zadnjiPodatak)) {
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

        stupacProgramMap = new HashMap<>();
        modMapa = new HashMap<>();

        csv.readHeaders();
        brojStupaca = csv.getHeaderCount();

        String[] headeri = csv.getHeaders();
        for (int j = 1; j < headeri.length; j++) {
            String[] st = headeri[j].split("_");
            String komponenta = st[0];
            String modStr = st[1];
            if (modStr.compareToIgnoreCase("span") == 0) {
                modMapa.put(j, "AS");
            } else if (modStr.compareToIgnoreCase("zero") == 0) {
                modMapa.put(j, "AZ");
            } else {
                log.log(Level.INFO, "Los header {0} na poziciji {1}", new Object[]{modStr, j});
            }

            if (programKljucMap.containsKey(komponenta)) {
                stupacProgramMap.put(j, programKljucMap.get(komponenta));
            }
        }
    }

    private void parsaj_record(CsvReader csv) throws IOException {
        for (Integer i = 1; i < brojStupaca; i++) {
            if (stupacProgramMap.containsKey(i)) {
                ProgramMjerenja program = stupacProgramMap.get(i);

                String vrijednostStr = csv.get(i);
                if (vrijednostStr != null && !vrijednostStr.isEmpty() && !vrijednostStr.endsWith("N")) {
                    try {
                        Double iznos = Double.parseDouble(vrijednostStr);

                        ZeroSpan pod = new ZeroSpan();
                        pod.setVrijeme(trenutnoVrijeme);
                        pod.setProgramMjerenjaId(program);
                        pod.setVrsta(modMapa.get(i));
                        pod.setVrijednost(iznos);
                        zeroSpanFacade.create(pod);

                    } catch (NumberFormatException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    private void setNizKanala() {
        for (ProgramMjerenja pm : programNaPostaji) {
            if (pm.getIzvorProgramKljuceviMap() != null) {
                IzvorProgramKljuceviMap ipm = pm.getIzvorProgramKljuceviMap();
                if (ipm == null || ipm.getKKljuc().isEmpty() || ipm.getKKljuc() == null) {
                    log.log(Level.SEVERE, "izvor_program_kljucevi_map(program_mjerenja_id = {0}) ne sadrzi K Kljuc", pm.getId());
                } else {
                    programKljucMap.put(pm.getIzvorProgramKljuceviMap().getKKljuc(), pm);
                }
            } else {
                log.log(Level.SEVERE, "izvor_program_kljucevi_map ne sadrzi program_mjerenja_id = {0}", pm.getId());
            }
        }
    }

    @Override
    public void setNivoValidacije(Integer nivo) {
        this.nivo = nivo;
    }
}
