/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.mlu;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.CsvParser;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.util.NizPodataka;
import dhz.skz.citaci.weblogger.util.Status;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class MLULoggerBean implements CsvParser, CitacIzvora {

    private static final Logger log = Logger.getLogger(MLULoggerBean.class.getName());

    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<Integer, ProgramMjerenja> mapa;
    private Postaja postaja;
    private CsvOmotnica omotnica;
    private IzvorPodataka izvor;
    private final int MIN_OBUHVAT = 45;
    private Map<Integer, String> modMapa;

    @Override
    public void obradi(CsvOmotnica omotnica) {
        log.log(Level.INFO, "Idem obraditi.");
        postaja = postajaFacade.findByNacionalnaOznaka(omotnica.getPostaja());
        izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.omotnica = omotnica;
        mapa = new HashMap<>();

        if (omotnica.getDatoteka().equalsIgnoreCase("zero_span")) {
            obradiZeroSpan(omotnica);

        } else {
            obradiMjerenja(omotnica);
        }
    }

    private void parseHeaders(String[] headeri) {
        for (int i = 1; i < headeri.length; i += 5) {
            String str = headeri[i];
            String datoteka = omotnica.getDatoteka();
            ProgramMjerenja pm = izvorPodatakaFacade.getProgram(postaja, izvor, str, datoteka);
            if (pm != null) {
                mapa.put(i, pm);
            }
        }
    }

    private void parseLinija(String[] linija, Date vrijeme, Collection<PodatakSirovi> podaci) {
        for (Integer i : mapa.keySet()) {
            ProgramMjerenja pm = mapa.get(i);
            if (!linija[i].equalsIgnoreCase("null")) {
                try {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    DecimalFormat format = new DecimalFormat("#.#");
                    format.setDecimalFormatSymbols(symbols);
                    Float vrijednost = format.parse(linija[i]).floatValue();

                    PodatakSirovi ps = new PodatakSirovi();
                    ps.setProgramMjerenjaId(pm);
                    ps.setVrijeme(vrijeme);
                    ps.setVrijednost(vrijednost);

                    String ss = linija[i + 1];
                    String ns = linija[i + 2];
                    String cs = linija[i + 3];
                    String vs = linija[i + 4];

                    ps.setStatusString(ss + ";" + ns + ";" + cs + ";" + vs);
                    podaci.add(ps);
                } catch (NumberFormatException | ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public Map<ProgramMjerenja, NizPodataka> procitaj(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        em.refresh(izvor);

        for (ProgramMjerenja program : izvor.getProgramMjerenjaCollection()) {
            Date zadnjiSatni = podatakFacade.getZadnjiPodatak(program);
            Date zadnjiSirovi = podatakSiroviFacade.getZadnjiPodatak(program);
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            Calendar kraj = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

            log.log(Level.INFO, "ZADNJI SATNI: {0}; SIROVI:", new Object[]{zadnjiSatni, zadnjiSirovi});

            kraj.setTime(zadnjiSirovi);
            kraj.set(Calendar.MINUTE, 0);
            kraj.set(Calendar.SECOND, 0);
            kraj.set(Calendar.MILLISECOND, 0);

            cal.setTime(zadnjiSatni);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.HOUR, 1);

            while (cal.compareTo(kraj) <= 0) {
                log.log(Level.FINEST, "ICAL : {0}", cal.getTime());
                Collection<PodatakSirovi> sirovi = podatakSiroviFacade.getPodatkeZaSat(program, cal.getTime());
                obradiSat(program, sirovi, cal.getTime());
                cal.add(Calendar.HOUR, 1);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
        return null;

    }

    private void obradiSat(ProgramMjerenja program, Collection<PodatakSirovi> pod, Date vrijeme) {
        if (pod == null || pod.isEmpty()) {
            return;
        }

        Podatak podatak = new Podatak();

        float kum_sum = 0;
        int count = 0;
        Status status = new Status();

        podatak.setProgramMjerenjaId(program);

        for (PodatakSirovi p : pod) {
            try {
                Status s = getStatus(p.getVrijednost(), p.getStatusString());
                if (s.isValid()) {
                    count++;
                    kum_sum += p.getVrijednost();
                }
                status.dodajStatus(s);
            } catch (NevaljanStatusException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }

        int obuhvat = 100 * count / 60;
        if (obuhvat < MIN_OBUHVAT) {
            status.dodajFlag(Flag.OBUHVAT);
        }

        podatak.setObuhvat((short) obuhvat);
        podatak.setStatus(status.getStatus());

        if (count > 0) {
            float iznos = kum_sum / count;
            podatak.setVrijednost(iznos);

        } else {
            podatak.setVrijednost(-999.f);
        }
        podatak.setVrijeme(vrijeme);
        podatak.setNivoValidacijeId(new NivoValidacije((short) 0));
        podatakFacade.spremiPodatak(podatak);
    }

//    private void odradi(Date zadnjiSat, ProgramMjerenja program) {
//
//    }
    @Override
    public Map<ProgramMjerenja, NizPodataka> procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> zadnjiPodatak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void obradiZeroSpan(CsvOmotnica omotnica) {
        Collection<ZeroSpan> podaci = new ArrayList<>();

//        try {
//                String pocetniDatumStr = omotnica.getLinije().get(0)[0];
//                Date pocetak = sdf.parse(pocetniDatumStr);
        parseZSHeaders(omotnica.getHeaderi());

        Iterator<Long> it = omotnica.getVremena().iterator();
        for (String[] linija : omotnica.getLinije()) {

            parseZSLinija(linija, it.next(), podaci);

        }

        //       procitaj(izvor);
//        } catch (ParseException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
        zeroSpanFacade.spremi(podaci);

    }

    private void parseZSHeaders(String[] headeri) {
        String datoteka = omotnica.getDatoteka();
        for (int i = 1; i < headeri.length; i++) {
            String str = headeri[i];
            if (str.length() > 5) {
                String kraj = str.substring(str.length() - 5);
                String pocetak = str.substring(0, str.length() - 5);
                ProgramMjerenja pm = izvorPodatakaFacade.getProgram(postaja, izvor, pocetak, datoteka);
                if (pm != null) {
                    mapa.put(i, pm);
                    if (kraj.compareToIgnoreCase("_Span") == 0) {
                        modMapa.put(i, "AS");
                    } else if (kraj.compareToIgnoreCase("_Zero") == 0) {
                        modMapa.put(i, "AZ");
                    } else {
                        log.log(Level.INFO, "Los header {0} na poziciji {1}", new Object[]{str, i});
                    }
                }
            } else {
                log.log(Level.INFO, "Los header {0} na poziciji {1}", new Object[]{str, i});
            }
        }
    }

    private void parseZSLinija(String[] linija, Long next, Collection<ZeroSpan> podaci) {
        Date vrijeme = new Date(next);
        for (Integer i : mapa.keySet()) {
            ProgramMjerenja pm = mapa.get(i);
            if (!linija[i].equalsIgnoreCase("null")) {
                try {
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    DecimalFormat format = new DecimalFormat("#.#");
                    format.setDecimalFormatSymbols(symbols);
                    Float vrijednost = format.parse(linija[i]).floatValue();

                    ZeroSpan ps = new ZeroSpan();
                    ps.setProgramMjerenjaId(pm);
                    ps.setVrijeme(vrijeme);
                    ps.setVrijednost(vrijednost);
                    ps.setVrsta(modMapa.get(i));
                    podaci.add(ps);
                } catch (NumberFormatException | ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Status getStatus(Float iznos, String statusStr) throws NevaljanStatusException {
        Status s = new Status();
        String[] st = statusStr.split(";");
        int ss = Integer.parseInt(st[0]);
        int bs = Integer.parseInt(st[1]);
        int fs = Integer.parseInt(st[2]);
        int nc = Integer.parseInt(st[3]);

        if (iznos == -9999.f) {
            s.dodajFlag(Flag.NEDOSTAJE);
        }
        if (fs != 0) {
            s.dodajFlag(Flag.FAULT);
        }
        if (ss != 0) {
            s.dodajFlag(Flag.FAULT);
        }
        if ((bs & 1) == 1) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 2) == 2) {
            s.dodajFlag(Flag.ZERO);
        }
        if ((bs & 4) == 4) {
            s.dodajFlag(Flag.SPAN);
        }
        if ((bs & 8) == 8) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 16) == 16) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 32) == 32) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 64) == 64) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if ((bs & 128) == 128) {
            s.dodajFlag(Flag.MAINTENENCE);
        }
        if (ss != 0) {
            s.dodajFlag(Flag.FAULT);
        }
        return s;
    }

    private void obradiMjerenja(CsvOmotnica omotnica) {
        Collection<PodatakSirovi> podaci = new ArrayList<>();

//        try {
//                String pocetniDatumStr = omotnica.getLinije().get(0)[0];
//                Date pocetak = sdf.parse(pocetniDatumStr);
        parseHeaders(omotnica.getHeaderi());

        Iterator<Long> it = omotnica.getVremena().iterator();
        for (String[] linija : omotnica.getLinije()) {
            Date vrijeme = new Date(it.next());
//                if ( vrijeme.after(zadnji)){
            parseLinija(linija, vrijeme, podaci);
//                }

        }
        //       procitaj(izvor);
//        } catch (ParseException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
        podatakSiroviFacade.spremi(podaci);
    }

    @Override
    public Date getVrijemeZadnjegPodatka(IzvorPodataka izvor, Postaja postaja, String datoteka) {
        Date vrijeme;
        if ( datoteka.compareToIgnoreCase("zero_span") == 0) {
            vrijeme = zeroSpanFacade.getVrijemeZadnjeg(izvor, postaja);
        } else {
            vrijeme = podatakSiroviFacade.getVrijemeZadnjeg(izvor, postaja, datoteka);
        }
        return vrijeme;
    }

}
