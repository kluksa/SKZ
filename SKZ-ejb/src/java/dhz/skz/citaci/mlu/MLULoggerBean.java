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
import dhz.skz.aqdb.facades.PodatakSiroviFacadeLocal;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.CsvParser;
import dhz.skz.citaci.weblogger.exceptions.NevaljanStatusException;
import dhz.skz.citaci.weblogger.util.Flag;
import dhz.skz.citaci.weblogger.util.Status;
import dhz.skz.citaci.weblogger.validatori.Validator;
import dhz.skz.validatori.ValidatorFactoryNovi;
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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
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
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    @EJB
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;
    @EJB
    private ValidatorFactoryNovi validatorFactory;
    
    @Resource
    private EJBContext context;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<Integer, ProgramMjerenja> mapa;
    private Postaja postaja;
    private CsvOmotnica omotnica;
    private IzvorPodataka izvor;
    private final int MIN_OBUHVAT = 45;
    private Map<Integer, String> modMapa;

    @Override
    public void obradi(CsvOmotnica omotnica) {
        try {
            log.log(Level.INFO, "Idem obraditi.");
            postaja = postajaFacade.findByNacionalnaOznaka(omotnica.getPostaja());
            izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.omotnica = omotnica;
            mapa = new HashMap<>();
            validatorFactory.init(izvor);
            
            if (omotnica.getVrsta().equalsIgnoreCase("zero-span")) {
                log.log(Level.INFO, "ZERO/SPAN");
                obradiZeroSpan(omotnica);
            } else {
                log.log(Level.INFO, "MJERENJE");
                obradiMjerenja(omotnica);
            }
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private void parseHeaders(String[] headeri) {
        for (int i = 1; i < headeri.length; i += 5) {
            String str = headeri[i];
            String datoteka = omotnica.getDatoteka();
            ProgramMjerenja pm = programMjerenjaFacade.find(postaja, izvor, str, datoteka);
            if (pm != null) {
                mapa.put(i, pm);
            }
        }
    }

    private void parseLinija(String[] linija, Date vrijeme, Collection<PodatakSirovi> podaci) {
        for (Integer i : mapa.keySet()) {
            ProgramMjerenja pm = mapa.get(i);
            Validator v = validatorFactory.getValidator(pm, vrijeme);
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
                    v.validiraj(ps);
                    podaci.add(ps);
                } catch (NumberFormatException | ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");

        for (ProgramMjerenja program : programMjerenjaFacade.find(izvor)) {
            try {
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

                UserTransaction utx = context.getUserTransaction();
                utx.begin();

                while (cal.getTime().before(kraj.getTime())) {
                    Date pocetakD = cal.getTime();
                    cal.add(Calendar.HOUR, 1);
                    Date krajD = cal.getTime();
                    Collection<PodatakSirovi> sirovi = podatakSiroviFacade.getPodaci(program, pocetakD, krajD);
                    if (!sirovi.isEmpty()) {
                        Podatak p = obradiSat(program, sirovi, krajD);
                        podatakFacade.spremi(p);
                    }
                }
                utx.commit();
            } catch (NotSupportedException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(MLULoggerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");

    }

    private Podatak obradiSat(ProgramMjerenja program, Collection<PodatakSirovi> pod, Date vrijeme) {

        Podatak podatak = new Podatak();

        float kum_sum = 0;
        int count = 0;
        Status status = new Status();

        podatak.setProgramMjerenjaId(program);

        for (PodatakSirovi p : pod) {
            try {
                if (p.getStatus()==0 && p.getGreska()==0) {
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
        podatak.setStatus(status.toInt());

        if (count > 0) {
            float iznos = kum_sum / count;
            podatak.setVrijednost(iznos);

        } else {
            podatak.setVrijednost(-999.f);
        }
        podatak.setVrijeme(vrijeme);
        podatak.setNivoValidacijeId(new NivoValidacije((short) 0));
        return podatak;
//        log.log(Level.INFO,"MLU PODATAK: {0}:{1}:{2}:{3}:{4}:{5}:{6}", new Object[]{podatak.getProgramMjerenjaId().getId(), 
//            podatak.getVrijeme(), 
//            podatak.getVrijednost(), 
//            podatak.toInt(), 
//            podatak.getObuhvat(), 
//            count,
//            podatak.getNivoValidacijeId().getId()});
//        podatakFacade.spremiPspremik);
    }

//    private void odradi(Date zadnjiSat, ProgramMjerenja program) {
//
//    }
    @Override
    public void procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> zadnjiPodatak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void obradiZeroSpan(CsvOmotnica omotnica) {
        UserTransaction utx = context.getUserTransaction();
        try {
            Collection<ZeroSpan> podaci = new ArrayList<>();

//        try {
//                String pocetniDatumStr = omotnica.getLinije().get(0)[0];
//                Date pocetak = sdf.parse(pocetniDatumStr);
            parseZSHeaders(omotnica.getHeaderi());

            Iterator<Long> it = omotnica.getVremena().iterator();
            for (String[] linija : omotnica.getLinije()) {
                parseZSLinija(linija, it.next(), podaci);
            }
            log.log(Level.INFO, "BROJ ZS: {0} {1} {2}", new Object[]{omotnica.getVremena().size(), omotnica.getLinije().size(), podaci.size()});
            //       napraviSatne(izvor);
//        } catch (ParseException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }

            utx.begin();
            zeroSpanFacade.spremi(podaci);
            utx.commit();
        } catch (NotSupportedException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

    private void parseZSHeaders(String[] headeri) {
        modMapa = new HashMap<>();
        String datoteka = omotnica.getDatoteka();
        for (Integer i = 1; i < headeri.length; i++) {
            String str = headeri[i];
            if (str.length() > 5) {
                String kraj = str.substring(str.length() - 5);
                String pocetak = str.substring(0, str.length() - 5);
                ProgramMjerenja pm = programMjerenjaFacade.find(postaja, izvor, pocetak, datoteka);
                if (pm != null) {
                    mapa.put(i, pm);
                    if (kraj.compareToIgnoreCase("_Span") == 0) {
                        modMapa.put(i, "AS");
                    } else if (kraj.compareToIgnoreCase("_Zero") == 0) {
                        modMapa.put(i, "AZ");
                    } else {
                        log.log(Level.INFO, "Los header {0} na poziciji {1}", new Object[]{str, i});
                    }
                } else {
                    log.log(Level.SEVERE, "NEMA PROGRAMA");
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
                    Float vrijednost;
                    if ("-9999".equals(linija[i])) {
                        vrijednost = -999.f;
                    } else {
                        vrijednost = format.parse(linija[i]).floatValue();
                    }
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

    

    private void obradiMjerenja(CsvOmotnica omotnica) {
        Collection<PodatakSirovi> podaci = new ArrayList<>();

//        try {
//                String pocetniDatumStr = omotnica.getLinije().get(0)[0];
//                Date pocetak = sdf.parse(pocetniDatumStr);
        parseHeaders(omotnica.getHeaderi());

        Iterator<Long> it = omotnica.getVremena().iterator();
        for (String[] linija : omotnica.getLinije()) {
            log.log(Level.INFO, "MLU Linija: {0}", linija[0]);
            Date vrijeme = new Date(it.next());
//                if ( vrijeme.after(zadnji)){
            parseLinija(linija, vrijeme, podaci);
//                }

        }
        //       napraviSatne(izvor);
//        } catch (ParseException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
        podatakSiroviFacade.spremi(podaci);
    }

    @Override
    public Date getVrijemeZadnjegPodatka(IzvorPodataka izvor, Postaja postaja, String datoteka) {
        Date vrijeme;
        if (datoteka.compareToIgnoreCase("zero-span") == 0) {
            vrijeme = zeroSpanFacade.getVrijemeZadnjeg(izvor, postaja);
        } else {
            vrijeme = podatakSiroviFacade.getVrijemeZadnjeg(izvor, postaja, datoteka);
        }
        return vrijeme;
    }

    @Override
    public Date getVrijemeZadnjegPodatka(CsvOmotnica omotnica) {
        log.log(Level.INFO, "OOOO:{0} {1} {2}", new Object[]{omotnica.getPostaja(), omotnica.getVrsta(), omotnica.getDatoteka()});
        postaja = postajaFacade.findByNacionalnaOznaka(omotnica.getPostaja());
        izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());

        if (omotnica.getVrsta().compareToIgnoreCase("zero-span") == 0) {
            return zeroSpanFacade.getVrijemeZadnjeg(izvor, postaja, omotnica.getDatoteka());
        } else {
            return podatakSiroviFacade.getVrijemeZadnjeg(izvor, postaja, omotnica.getDatoteka());
        }
    }

    @Override
    public Collection<PodatakSirovi> dohvatiSirove(ProgramMjerenja program, Date pocetak, Date kraj, boolean p, boolean k) {
        return podatakSiroviFacade.getPodaci(program, pocetak, kraj, false, true);
    }

}
