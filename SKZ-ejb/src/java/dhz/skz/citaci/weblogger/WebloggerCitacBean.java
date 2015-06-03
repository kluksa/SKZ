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
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
mport dhz.skz.aqdb.facades.NivoValidacijeFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.MinutniUSatne;
import dhz.skz.citaci.weblogger.validatori.WlValidatorFactory;
import dhz.skz.config.Config;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
mport javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @EJB
    private NivoValidacijeFacade nivoValidacijeFacade;

    @EJB
    private ProgramUredjajLinkFacade programUredjajLinkFacade;

    @EJB
    private MinutniUSatne siroviUSatneBean;

    private static final Logger log = Logger.getLogger(WebloggerCitacBean.class.getName());
    @Inject
    @Config
    private TimeZone timeZone;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private PodatakFacade podatakFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private PostajaFacade posajaFacade;
//    @EJB
//    private ValidatorFactory validatorFactory;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;

    @Resource
    private EJBContext context;

    @EJB
    private PodatakSiroviFacadeLocal podatakSiroviFacade;
    
    private IzvorPodataka izvor;
    private Collection<ProgramMjerenja> programNaPostaji;
    private Postaja aktivnaPostaja;
    private Date vrijemeZadnjegMjerenja, vrijemeZadnjegZeroSpan;
    private ValidatorFactory valFac;

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
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void napraviSatne(IzvorPodataka izvor) {

        NivoValidacije nv = nivoValidacijeFacade.find(0);

//        try {
        log.log(Level.INFO, "POCETAK CITANJA");
        this.izvor = izvor;
//            validatorFactory.init(izvor);
        valFac = new WlValidatorFactory(izvor.getProgramMjerenjaCollection());
        for (Iterator<Postaja> it = posajaFacade.getPostajeZaIzvor(izvor).iterator(); it.hasNext();) {
            aktivnaPostaja = it.next();

            try { // sto god da se desi, idemo na slijedecu postaju
                log.log(Level.INFO, "Citam: {0}", aktivnaPostaja.getNazivPostaje());

                programNaPostaji = programMjerenjaFacade.find(aktivnaPostaja, izvor);

                odrediVrijemeZadnjegPodatka(aktivnaPostaja);

                pokupiMjerenja();

                for (ProgramMjerenja pm : programNaPostaji) {
                    siroviUSatneBean.spremiSatneIzSirovih(pm, nv);
                }
            } catch (Throwable ex) {
                log.log(Level.SEVERE, "GRESKA KOD POSTAJE {1}:{0}", new Object[]{aktivnaPostaja.getNazivPostaje(), aktivnaPostaja.getId()});
                log.log(Level.SEVERE, "", ex);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
//        } catch (NamingException ex) {
//            log.log(Level.SEVERE, null, ex);
//        }
    }

    private void pokupiMjerenja() {
        FtpKlijent ftp = new FtpKlijent();
        UserTransaction utx = context.getUserTransaction();

        try {
            ftp.connect(new URI(izvor.getUri()));

            String ptStr = "^(" + Pattern.quote(aktivnaPostaja.getNazivPostaje().toLowerCase()) + ")(_c)?-(\\d{8})(.?)\\.csv";
            Pattern pattern = Pattern.compile(ptStr);
            for (FTPFile file : ftp.getFileList()) {
                Matcher m = pattern.matcher(file.getName().toLowerCase());
                if (m.matches()) {
                    try {
                        Date terminDatoteke = formatter.parse(m.group(3));
                        WlFileParser citac = napraviParser(m.group(2), terminDatoteke);

                        if (citac.isDobarTermin()) {

                            log.log(Level.INFO, "Datoteka :{0}", file.getName());
                            try (InputStream ifs = ftp.getFileStream(file)) {
                                utx.begin();
                                citac.parse(ifs);
                                utx.commit();
                            } catch (Exception ex) {
                                log.log(Level.SEVERE, "Datoteka :{0}, {1}", new Object[]{file.getName(), ex});
                                //When something is wrong, just rollback to the state before calling<!--DVFMTSC--> latest utx.begin();
                                utx.rollback();
                                throw ex;
                            } finally {
                                ftp.zavrsi();
                            }
                        }
                    } catch (ParseException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (FtpKlijentException | URISyntaxException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "XXXXXXXX", ex);
        } finally {
            ftp.disconnect();
        }
    }

    private void odrediVrijemeZadnjegPodatka(Postaja p) {
        vrijemeZadnjegMjerenja = podatakSiroviFacade.getVrijemeZadnjeg(izvor, p);
        vrijemeZadnjegZeroSpan = zeroSpanFacade.getVrijemeZadnjeg(izvor, p);
    }

    private WlFileParser napraviParser(String group, Date terminDatoteke) {
        WlFileParser parser;
        Collection<ProgramMjerenja> aktivniProgram = programMjerenjaFacade.findZaTermin(aktivnaPostaja, izvor, terminDatoteke);

        if (group == null || group.isEmpty()) {
            parser = new WlMjerenjaParser(aktivniProgram, timeZone, valFac, podatakSiroviFacade);
            parser.setZadnjiPodatak(vrijemeZadnjegMjerenja);
        } else {
            parser = new WlZeroSpanParser(aktivniProgram, timeZone, zeroSpanFacade);
            parser.setZadnjiPodatak(vrijemeZadnjegZeroSpan);
        }
        parser.setTerminDatoteke(terminDatoteke);
        return parser;
    }
}
