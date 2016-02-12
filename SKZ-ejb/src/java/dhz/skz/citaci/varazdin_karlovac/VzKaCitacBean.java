/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.varazdin_karlovac;

import dhz.skz.citaci.weblogger.*;
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
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.varazdin_karlovac.validatori.VzKaValidatorFactory;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.weblogger.validatori.WlValidatorFactory;
import dhz.skz.config.Config;
import dhz.skz.validatori.Validator;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import javax.transaction.UserTransaction;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.BEAN)
public class VzKaCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(VzKaCitacBean.class.getName());

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

//    @EJB
//    private ValidatorFactory validatorFactory;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;

    @Resource
    private EJBContext context;

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
    public void napraviSatne(IzvorPodataka izvor) {

//        try {
        log.log(Level.INFO, "POCETAK CITANJA");
        this.izvor = izvor;
        valFac = new VzKaValidatorFactory(izvor.getProgramMjerenjaCollection());
        for (Iterator<Postaja> it = posajaFacade.getPostajeZaIzvor(izvor).iterator(); it.hasNext();) {
            aktivnaPostaja = it.next();

            try { // sto god da se desi, idemo na slijedecu postaju
                log.log(Level.INFO, "Citam: {0}", aktivnaPostaja.getNazivPostaje());

                programNaPostaji = programMjerenjaFacade.find(aktivnaPostaja, izvor);
                PodatakSirovi zadnji = podatakSiroviFacade.getZadnji(izvor, aktivnaPostaja);
                if (zadnji == null) {
                    Date pocetakMjerenja = programMjerenjaFacade.getPocetakMjerenja(izvor, aktivnaPostaja);
                    if (pocetakMjerenja == null) {
                        continue;
                    }
                    vrijemeZadnjegMjerenja = pocetakMjerenja;
                } else {
                    vrijemeZadnjegMjerenja = zadnji.getVrijeme();
                }
                vrijemeZadnjegZeroSpan = zeroSpanFacade.getVrijemeZadnjeg(izvor, aktivnaPostaja);

                pokupiPodatke("/zapisi/mjerenja", new VzKaMjerenjaParser(aktivnaPostaja, programNaPostaji, timeZone, valFac, podatakSiroviFacade ));
                pokupiPodatke("/zapisi/zerospan", new VzKaZeroSpanParser(programNaPostaji, timeZone, zeroSpanFacade));

            } catch (Throwable ex) {
                log.log(Level.SEVERE, "GRESKA KOD POSTAJE {1}:{0}", new Object[]{aktivnaPostaja.getNazivPostaje(), aktivnaPostaja.getId()});
                log.log(Level.SEVERE, "", ex);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    private void pokupiPodatke(String path, WlFileParser citac) {

        FtpKlijent ftp = new FtpKlijent();
        UserTransaction utx = context.getUserTransaction();

        try {
            log.log(Level.INFO, "SPAJAM SE");
            ftp.connect(new URI(izvor.getUri()+"@" + aktivnaPostaja.getNetAdresa().trim()));
            String ptStr = "^(\\d{8})_(..)_.\\.csv";
            Pattern pattern = Pattern.compile(ptStr);
            log.log(Level.INFO, "LISTAM");

            for (FTPFile file : ftp.getFileList(path)) {
                Matcher m = pattern.matcher(file.getName().toLowerCase());
                if (m.matches()) {
                    try {
                        Date terminDatoteke = formatter.parse(m.group(1));
                        citac.setNivoValidacije(0);

                        if (isDobarTermin(terminDatoteke)) {
                            String fname = path + "/" + file.getName();
                            log.log(Level.INFO, "Datoteka : {0}", fname);
                            try (InputStream ifs = ftp.getFileStream(fname)) {

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


    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        Map<String, String> mapa = new HashMap<>();
        Uredjaj uredjaj = uredjajFacade.findByPodatakSirovi(ps);
        log.log(Level.INFO, "UREDJAJ: {0}", new Object[]{uredjaj.getModelUredjajaId().getOznakaModela()});
        VzKaValidatorFactory wlValidatorFactory = new VzKaValidatorFactory(null);
        Validator validator = wlValidatorFactory.getValidator(ps.getProgramMjerenjaId(),ps.getVrijeme());
        Collection<String> opisStatusa = validator.opisStatusa(ps.getStatusString());
        Integer i = 0;
        for (String s : opisStatusa) {
            mapa.put(i.toString(), s);
            i++;
        }
        return mapa;
    }


    private boolean isDobarTermin(Date terminDatoteke) {
        return (vrijemeZadnjegMjerenja == null) || (vrijemeZadnjegMjerenja.getTime() - terminDatoteke.getTime() < 24 * 3600 * 1000);
    }

    public void setAktivnaPostaja(Postaja aktivnaPostaja) {
        this.aktivnaPostaja = aktivnaPostaja;
    }

    public void setIzvor(IzvorPodataka izvor) {
        this.izvor = izvor;
    }

}

