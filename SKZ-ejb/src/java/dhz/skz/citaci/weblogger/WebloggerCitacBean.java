/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.weblogger.util.NizPodataka;
import dhz.skz.citaci.weblogger.util.PodatakWl;
import dhz.skz.citaci.weblogger.util.SatniAgregator;
import dhz.skz.citaci.weblogger.validatori.ValidatorFactory;
import dhz.skz.citaci.weblogger.zerospan.WebloggerZeroSpanCitacBean;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class WebloggerCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(WebloggerCitacBean.class.getName());

    private final TimeZone timeZone = TimeZone.getTimeZone("UTC");

    @EJB
    private PodatakFacade dao;

    @EJB
    private FtpKlijent ftp;

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @EJB
    private ValidatorFactory validatorFac;

    @EJB
    private WebloggerZeroSpanCitacBean zeroSpan;

    public WebloggerCitacBean() {

    }

    @Override
    public Map<ProgramMjerenja, NizPodataka> procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> pocetak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public Map<ProgramMjerenja, NizPodataka> procitaj(IzvorPodataka izvor) {
        log.log(Level.INFO, "POCETAK CITANJA");
        em.refresh(izvor);
        Collection<Postaja> postajeZaIzvor = dao.getPostajeZaIzvor(izvor);
        for (Postaja p : postajeZaIzvor) {
            log.log(Level.INFO, "Citam: {0}", p.getNazivPostaje());
            pokupiMjerenjaSaPostaje(izvor, p);
            zeroSpan.pokupiZeroSpanSaPostaje(izvor, p);
        }
        log.log(Level.INFO, "KRAJ CITANJA");
        return null;
    }

    private Map<ProgramMjerenja, NizPodataka> getMapaNizova(Postaja p, IzvorPodataka izvor, Date zadnji) throws URISyntaxException {

        Map<ProgramMjerenja, NizPodataka> tmp = new HashMap<>();
        Collection<ProgramMjerenja> programNaPostajiZaIzvor = dao.getProgramNaPostajiZaIzvor(p, izvor, zadnji);
        for (ProgramMjerenja pm : programNaPostajiZaIzvor) {
            log.log(Level.INFO, "Program: {0}: {1}", new Object[]{pm.getPostajaId().getNazivPostaje(), pm.getKomponentaId().getFormula()});
            NizPodataka np = new NizPodataka();
            np.setKljuc(pm);
            np.setValidatori(validatorFac.getValidatori(pm));
            tmp.put(pm, np);
        }
        return tmp;
    }

    private void pokupiMjerenjaSaPostaje(IzvorPodataka izvor, Postaja p) {
        Date zadnji = getZadnjiPodatak(izvor, p);
        try {
            Map<ProgramMjerenja, NizPodataka> tmp = getMapaNizova(p, izvor, zadnji);
            ftp.connect(new URI(izvor.getUri()));

            WlDatotekaParser citac = new WlDatotekaParser(timeZone);
            citac.setZadnjiPodatak(zadnji);
            citac.setNizKanala(tmp);

            WlFileFilter fns = new WlFileFilter(p.getNazivPostaje(), zadnji, timeZone);
            for (FTPFile file : ftp.getFileList(fns)) {
                log.log(Level.INFO, "Datoteka :{0}", file.getName());
                try (InputStream ifs = ftp.getFileStream(file)) {
                    citac.parse(ifs);
                } catch (Exception ex) { // kakva god da se iznimka dogodi, nastavljamo
                    log.log(Level.SEVERE, null, ex);
                } finally {
                    ftp.zavrsi();
                }
            }
            obradiISpremiNizove(tmp);
        } catch (FtpKlijentException | URISyntaxException ex) {
            log.log(Level.SEVERE, null, ex);
        } finally {
            ftp.disconnect();
        }
    }

    private Date getZadnjiPodatak(IzvorPodataka izvor, Postaja p) {
        Date zadnji = dao.getVrijemeZadnjegNaPostajiZaIzvor(p, izvor);
        if (zadnji == null) {
            zadnji = new Date(0L);
        }
        log.log(Level.INFO, "Zadnji podatak na {0} u {1}", new Object[]{p.getNazivLokacije(), zadnji});
        return zadnji;
    }

    private void obradiISpremiNizove(Map<ProgramMjerenja, NizPodataka> ulaz) {
        for (ProgramMjerenja p : ulaz.keySet()) {

            NizPodataka niz = ulaz.get(p);
            if (!niz.getPodaci().isEmpty()) {
                try {
                    SatniAgregator a = new SatniAgregator();
                    a.setNeagregiraniNiz(niz);
                    a.agregiraj();
                    NizPodataka agr = a.getAgregiraniNiz();
                    log.log(Level.INFO, "Pospremam Postaja {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
                    if (!agr.getPodaci().isEmpty()) {
                        pospremiNiz(agr);
                        niz.getPodaci().clear();
                    }
                    niz.getValidatori().clear();
                } catch (ConstraintViolationException ex) {
                    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
                    log.log(Level.SEVERE, "", ex);
                } catch (Exception ex) {
                    log.log(Level.SEVERE, "Postaja {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
                    throw ex;
                }
            }
        }
    }
    
    
    public void pospremiNiz(NizPodataka niz) {
        ProgramMjerenja kljuc = niz.getKljuc();

        log.log(Level.INFO, "Postaja {0}, komponenta {1}, prvi {2}, zadnj {3}, ukupno {4}",
                new Object[]{kljuc.getPostajaId().getNazivPostaje(),
                    kljuc.getKomponentaId().getFormula(),
                    niz.getPodaci().firstKey(),
                    niz.getPodaci().lastKey(),
                    niz.getPodaci().size()});
        NivoValidacije nv = new NivoValidacije((short) 0);
        for (Date d : niz.getPodaci().keySet()) {
            PodatakWl wlp = niz.getPodaci().get(d);
            Podatak p = new Podatak();
            p.setVrijeme(d);
            p.setVrijednost(wlp.getVrijednost());
            p.setObuhvat(wlp.getObuhvat());
            p.setProgramMjerenjaId(kljuc);
            p.setNivoValidacijeId(nv);
            p.setStatus(wlp.getStatus());
            dao.create(p);
        }
    }


}
