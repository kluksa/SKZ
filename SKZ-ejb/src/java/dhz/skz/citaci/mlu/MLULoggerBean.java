/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.mlu;

import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.CsvParser;
import dhz.skz.citaci.MinutniUSatne;
import dhz.skz.config.Config;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class MLULoggerBean implements CsvParser, CitacIzvora {

    private static final Logger log = Logger.getLogger(MLULoggerBean.class.getName());
    @EJB
    private MinutniUSatne siroviUSatneBean;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private PostajaFacade postajaFacade;
    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Postaja postaja;
    private IzvorPodataka izvor;
    @Inject
    @Config
    private TimeZone tzone;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void prihvati(CsvOmotnica omotnica) {
        log.log(Level.INFO, "Idem obraditi.");
        postaja = postajaFacade.findByNacionalnaOznaka(omotnica.getPostaja());
        izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        sdf.setTimeZone(tzone);

        OmotnicaPrihvat op = parserFactory(omotnica);
        op.prihvati(omotnica, postaja, izvor);

    }

    private OmotnicaPrihvat parserFactory(CsvOmotnica omotnica) {
        if (omotnica.getVrsta().equalsIgnoreCase("zero-span")) {
            log.log(Level.INFO, "ZERO/SPAN");
            return new ZeroSpanPrihvat(programMjerenjaFacade, zeroSpanFacade);
        } else {
            log.log(Level.INFO, "MJERENJE");
            return new MjerenjaPrihvat(programMjerenjaFacade, podatakSiroviFacade);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void napraviSatne(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        NivoValidacije nv = new NivoValidacije(0);
        for (ProgramMjerenja program : programMjerenjaFacade.find(izvor)) {
            siroviUSatneBean.spremiSatneIzSirovih(program, nv);
        }
        log.log(Level.INFO, "KRAJ CITANJA");
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
}
