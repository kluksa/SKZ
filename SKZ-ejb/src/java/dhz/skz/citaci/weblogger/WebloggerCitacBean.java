/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ProgramUredjajLinkFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.validatori.ValidatorFactory;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.facades.UredjajFacade;
import dhz.skz.aqdb.facades.ZadnjiSiroviFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.weblogger.validatori.WlValidatorFactory;
import dhz.skz.config.Config;
import dhz.skz.validatori.Validator;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class WebloggerCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(WebloggerCitacBean.class.getName());

    @Inject
    @Config
    private TimeZone timeZone;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    @EJB
    private UredjajFacade uredjajFacade;

    @EJB
    private ProgramUredjajLinkFacade programUredjajLinkFacade;

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private PostajaFacade posajaFacade;
    @EJB
    private ZadnjiSiroviFacade zadnjiSiroviFacade;

//    @EJB
//    private ValidatorFactory validatorFactory;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;

    @Resource
    private EJBContext context;

    private IzvorPodataka izvor;
//    private Collection<ProgramMjerenja> programNaPostaji;
    private Postaja aktivnaPostaja;
    private ValidatorFactory valFac;
    private HashMap<String, Postaja> postaje;
    private UserTransaction utx;

//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//    private void spremi(NavigableMap<Date, PodatakSirovi> podaci) {
//        podatakSiroviFacade.spremi(podaci.values());
//    }
//    enum Vrsta {
//
//        MJERENJE, KALIBRACIJA
//    }
    @PostConstruct
    public void init() {
        formatter.setTimeZone(timeZone);
    }

    @Override
//    @Asynchronous
//    public Future<Boolean> napraviSatne(IzvorPodataka izvor) {
    public Boolean napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        this.izvor = izvor;
        valFac = new WlValidatorFactory(izvor.getProgramMjerenjaCollection());
        utx = context.getUserTransaction();
        HashMap<String, WlPostajaDatoteke> mapa = popuniMapu();

        for (String p : mapa.keySet()) {
            try {
                FtpKlijent ftp = new FtpKlijent();
                ftp.connect(new URI(izvor.getUri()));
                try {
                    pokupiMjerenja(ftp, mapa.get(p));
                    pokupiZeroSpan(ftp, mapa.get(p));
                } catch (IllegalStateException | SecurityException | SystemException ex) {
                    log.log(Level.SEVERE, null, ex);
                }

                ftp.disconnect();
            } catch (URISyntaxException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (FtpKlijentException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
//        return new AsyncResult<Boolean>(true);
        return true;
    }

    private void pokupiMjerenja(FtpKlijent ftp, WlPostajaDatoteke datoteke) throws IllegalStateException, SecurityException, SystemException, FtpKlijentException {
        for (WlFileName file : datoteke.getMjerenjaFname()) {
            Collection<ProgramMjerenja> aktivniProgram = programMjerenjaFacade.findZaTermin(datoteke.getPostaja(), izvor, file.getTermin());
            WlFileParser parser = new WlMjerenjaParser(aktivniProgram, timeZone, valFac, podatakSiroviFacade);
            parser.setZadnjiPodatak(datoteke.getVrjmemeZadnjegMjerenja());
            pokupi(parser, file, ftp);
        }
    }

    private void pokupiZeroSpan(FtpKlijent ftp, WlPostajaDatoteke datoteke) throws IllegalStateException, SecurityException, SystemException, FtpKlijentException {
        for (WlFileName file : datoteke.getZsFname()) {
            Collection<ProgramMjerenja> aktivniProgram = programMjerenjaFacade.findZaTermin(datoteke.getPostaja(), izvor, file.getTermin());
            WlFileParser parser = new WlZeroSpanParser(aktivniProgram, timeZone, zeroSpanFacade);
            parser.setZadnjiPodatak(datoteke.getVrijemeZadnjegZS());
            pokupi(parser, file, ftp);
        }
    }

//    @Override
//    @Asynchronous
    public void napraviSatne2(IzvorPodataka izvor) {
//
////        try {
//        log.log(Level.INFO, "POCETAK CITANJA");
//        this.izvor = izvor;
////            validatorFactory.init(izvor);
//        valFac = new WlValidatorFactory(izvor.getProgramMjerenjaCollection());
//
//        FtpKlijent ftp = new FtpKlijent();
//        try {
//            ftp.connect(new URI(izvor.getUri()));
//            FTPFile[] fileList = ftp.getFileList();
//
//            for (Iterator<Postaja> it = posajaFacade.getPostajeZaIzvor(izvor).iterator(); it.hasNext();) {
//                aktivnaPostaja = it.next();
////            if ( !aktivnaPostaja.getNacionalnaOznaka().equals("PLJ01")) continue;
//                try { // sto god da se desi, idemo na slijedecu postaju
//                    log.log(Level.INFO, "Citam: {0}", aktivnaPostaja.getNazivPostaje());
//
//                    programNaPostaji = programMjerenjaFacade.find(aktivnaPostaja, izvor);
//
//                    PodatakSirovi zadnji = podatakSiroviFacade.getZadnji(izvor, aktivnaPostaja);
//                    if (zadnji == null) {
//                        Date pocetakMjerenja = programMjerenjaFacade.getPocetakMjerenja(izvor, aktivnaPostaja);
//                        if (pocetakMjerenja == null) {
//                            continue;
//                        }
//                        vrijemeZadnjegMjerenja = pocetakMjerenja;
//                    } else {
//                        vrijemeZadnjegMjerenja = zadnji.getVrijeme();
//                    }
//
//                    vrijemeZadnjegZeroSpan = zeroSpanFacade.getVrijemeZadnjeg(izvor, aktivnaPostaja);
//
//                    pokupiMjerenja(ftp, fileList);
//
//                } catch (Throwable ex) {
//                    log.log(Level.SEVERE, "GRESKA KOD POSTAJE {1}:{0}", new Object[]{aktivnaPostaja.getNazivPostaje(), aktivnaPostaja.getId()});
//                    log.log(Level.SEVERE, "", ex);
//                }
//            }
//        } catch (URISyntaxException | FtpKlijentException ex) {
//            log.log(Level.SEVERE, null, ex);
//        } finally {
//            ftp.disconnect();
//        }
//        log.log(Level.INFO, "KRAJ CITANJA");
    }

//    private void pokupiMjerenja(FtpKlijent ftp, FTPFile[] listaDatoteka) throws IllegalStateException, SecurityException, SystemException {
//        UserTransaction utx = context.getUserTransaction();
//
//        String ptStr = "^(" + Pattern.quote(aktivnaPostaja.getNazivPostaje().toLowerCase()) + ")(_c)?-(\\d{8})(.?)(\\.csv)";
//        Pattern pattern = Pattern.compile(ptStr);
//        for (FTPFile file : listaDatoteka) {
//            Matcher m = pattern.matcher(file.getName().toLowerCase());
//            if (m.matches()) {
//                try {
//                    Date terminDatoteke = formatter.parse(m.group(3));
//                    WlFileParser citac = napraviParser(m.group(2), terminDatoteke);
//                    citac.setNivoValidacije(0);
//                    if (citac.isDobarTermin()) {
//                        log.log(Level.INFO, "Datoteka : {0}", file.getName());
//                        try (InputStream ifs = ftp.getFileStream(file)) {
//                            utx.begin();
//                            citac.parse(ifs);
//                            utx.commit();
//                        } catch (NotSupportedException | SystemException | FtpKlijentException | WlFileException | IOException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
//                            log.log(Level.SEVERE, "Datoteka :{0}, {1}", new Object[]{file.getName(), ex});
//                            utx.rollback();
//                        }
//                    }
//                } catch (ParseException ex) {
//                    log.log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
//
//    private WlFileParser napraviParser(String group, Date terminDatoteke) {
//        WlFileParser parser;
//        Collection<ProgramMjerenja> aktivniProgram = programMjerenjaFacade.findZaTermin(aktivnaPostaja, izvor, terminDatoteke);
//
//        if (group == null || group.isEmpty()) {
//            parser = new WlMjerenjaParser(aktivniProgram, timeZone, valFac, podatakSiroviFacade);
//            parser.setZadnjiPodatak(vrijemeZadnjegMjerenja);
//        } else {
//            parser = new WlZeroSpanParser(aktivniProgram, timeZone, zeroSpanFacade);
//            parser.setZadnjiPodatak(vrijemeZadnjegZeroSpan);
//        }
//        parser.setTerminDatoteke(terminDatoteke);
//        return parser;
//    }
    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        Map<String, String> mapa = new HashMap<>();
        Uredjaj uredjaj = uredjajFacade.findByPodatakSirovi(ps);
        log.log(Level.INFO, "UREDJAJ: {0}", new Object[]{uredjaj.getModelUredjajaId().getOznakaModela()});
        WlValidatorFactory wlValidatorFactory = new WlValidatorFactory(null);
        Validator validator = wlValidatorFactory.getValidator(uredjaj);
        Collection<String> opisStatusa = validator.opisStatusa(ps.getStatusString());
        Integer i = 0;
        for (String s : opisStatusa) {
            mapa.put(i.toString(), s);
            i++;
        }
        return mapa;
    }

    private HashMap<String, WlPostajaDatoteke> popuniMapu() {
        HashMap<String, WlPostajaDatoteke> mapa = new HashMap<>();
        for (Postaja p : posajaFacade.getPostajeZaIzvor(izvor)) {
            WlPostajaDatoteke wpd = new WlPostajaDatoteke(p, timeZone);
            wpd.setVrijemeZadnjegZS(getVrijemeZadnjegZeroSpan(p));
            wpd.setVrjmemeZadnjegMjerenja(zadnjiSiroviFacade.getVrijeme(izvor, p));
            wpd.setImaZeroSpan(provjeriZS(p));
            mapa.put(p.getNazivPostaje().toLowerCase(), wpd);
        }

        Pattern pattern = Pattern.compile("^(\\w.+?)(_c)?-(\\d{8})([a-z]?)(\\.csv)$");
        FTPFile[] listFiles = izlistajDirektorij();
        for (FTPFile f : listFiles) {
            Matcher m = pattern.matcher(f.getName().toLowerCase());
            if (m.matches()) {
                try {
                    WlPostajaDatoteke wpd = mapa.get(m.group(1));
                    wpd.dodajFajl(f.getName(), m);
                } catch (ParseException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return mapa;
    }

    private Date getVrijemeZadnjegZeroSpan(Postaja p) {
        Date vrijemeZadnjeg = zeroSpanFacade.getVrijemeZadnjeg(izvor, p);
        if (vrijemeZadnjeg == null) {
            vrijemeZadnjeg = programMjerenjaFacade.getPocetakMjerenja(izvor, p);
        }
        return vrijemeZadnjeg;
    }

    private FTPFile[] izlistajDirektorij() {
        FTPFile[] listFiles = null;
        FtpKlijent ftp = new FtpKlijent();
        try {
            URI uri = new URI(izvor.getUri());
            ftp.connect(uri);
            listFiles = ftp.getFileList();
        } catch (URISyntaxException | FtpKlijentException ex) {
            log.log(Level.SEVERE, null, ex);
        } finally {
            ftp.disconnect();
        }
        return listFiles;
    }

    private void pokupi(WlFileParser parser, WlFileName file, FtpKlijent ftp) throws IllegalStateException, SecurityException, SystemException, FtpKlijentException {
        log.log(Level.INFO, "Datoteka : {0}", file.getFname());
        try (InputStream ifs = ftp.getFileStream(file.getFname())) {
            utx.begin();
            parser.parse(ifs);
            utx.commit();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Datoteka :{0}, {1}", new Object[]{file.getFname(), ex});
            utx.rollback();
        }  finally {
            ftp.zavrsi();
        }
    }

    private boolean provjeriZS(Postaja p) {
        boolean ima = false;
        for (ProgramMjerenja pm : programMjerenjaFacade.find(p, izvor)) {
            if ((pm.getIzvorProgramKljuceviMap() != null) && (pm.getIzvorProgramKljuceviMap().getUKljuc() != null) 
                    && !pm.getIzvorProgramKljuceviMap().getUKljuc().isEmpty()){
                ima |= true;
            }
        }
        return ima;
        
//        return  programMjerenjaFacade.find(p, izvor).stream()
//                .filter(pm -> Objects.nonNull(pm.getIzvorProgramKljuceviMap()))
//                .filter(pm -> Objects.nonNull(pm.getIzvorProgramKljuceviMap().getKKljuc()))
//                .map(pm -> pm.getIzvorProgramKljuceviMap().getKKljuc())
//                .anyMatch(ukljuc -> !ukljuc.isEmpty());
    }
}
