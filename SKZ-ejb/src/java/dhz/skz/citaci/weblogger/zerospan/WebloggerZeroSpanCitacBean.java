/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger.zerospan;

import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.weblogger.util.NizZeroSpanPodataka;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ProgramUredjajLink;
import dhz.skz.aqdb.entity.Uredjaj;
import dhz.skz.aqdb.entity.ZeroSpan;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class WebloggerZeroSpanCitacBean {

    @EJB
    private ZeroSpanFacade dao;

    private static final Logger log = Logger.getLogger(WebloggerZeroSpanCitacBean.class.getName());

    private final TimeZone timeZone = TimeZone.getTimeZone("UTC");

    @EJB
    private FtpKlijent ftp;
    private IzvorPodataka izvor;
    private Postaja postaja;

    public void pokupiZeroSpanSaPostaje(IzvorPodataka izvor, Postaja p) {
        this.izvor = izvor;
        this.postaja = p;
        Date zadnji = getZadnjiZS(izvor, p);

        if (imaZS(izvor, p)) {

            try {
                Map<ProgramMjerenja, NizZeroSpanPodataka> tmp = getMapaZeroSpanNizova(p, izvor, zadnji);
                ftp.connect(new URI(izvor.getUri()));

                WlZeroSpanDatotekaParser citac = new WlZeroSpanDatotekaParser(timeZone);
                citac.setZadnjiPodatak(zadnji);
                citac.setNizKanala(tmp);

                WlZeroSpanFileFilter fns = new WlZeroSpanFileFilter(p.getNazivPostaje(), zadnji, timeZone);
                for (FTPFile file : ftp.getFileList(fns)) {
                    log.log(Level.INFO, "Zero/span datoteka :{0}", file.getName());
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
    }

    private Date getZadnjiZS(IzvorPodataka izvor, Postaja p) {
        Date zadnji = dao.getVrijemeZadnjeg(izvor, p);
        if (zadnji == null) {
            zadnji = new Date(0L);
        }
        log.log(Level.INFO, "Zadnji zero/span na {0} u {1}", new Object[]{p.getNazivLokacije(), zadnji});
        return zadnji;
    }

    private Map<ProgramMjerenja, NizZeroSpanPodataka> getMapaZeroSpanNizova(Postaja p, IzvorPodataka izvor, Date zadnji) {
        Map<ProgramMjerenja, NizZeroSpanPodataka> tmp = new HashMap<>();
        Collection<ProgramMjerenja> programNaPostajiZaIzvor = dao.getProgramNaPostajiZaIzvor(p, izvor, zadnji);
        for (ProgramMjerenja pm : programNaPostajiZaIzvor) {
            NizZeroSpanPodataka np = new NizZeroSpanPodataka();
            np.setUredjaji(getUredjajiMapa(pm));
            np.setKljuc(pm);
            tmp.put(pm, np);
        }
        return tmp;
    }

    private void obradiISpremiNizove(Map<ProgramMjerenja, NizZeroSpanPodataka> ulaz) {
        for (ProgramMjerenja p : ulaz.keySet()) {
            NizZeroSpanPodataka niz = ulaz.get(p);
            if (!niz.getPodaci().isEmpty()) {
                try {
                    log.log(Level.INFO, "Pospremam ZS Postaja {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
                    pospremiNiz(niz);
                    niz.getPodaci().clear();
                } catch (Exception ex) {
                    log.log(Level.SEVERE, "Postaja ZS {0}, komponenta {1}", new Object[]{p.getPostajaId().getNazivPostaje(), p.getKomponentaId().getFormula()});
                    throw ex;
                }
            }
        }
    }

    private NavigableMap<Date, Uredjaj> getUredjajiMapa(ProgramMjerenja pm) {
        NavigableMap<Date, Uredjaj> mapa = new TreeMap<>();
        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
            mapa.put(pul.getVrijemePostavljanja(), pul.getUredjajId());
        }
        return mapa;
    }

    private boolean imaZS(IzvorPodataka izvor, Postaja p) {
        List<ProgramMjerenja> list = dao.getProgramSaZeroSpanomNaPostaji(izvor, p);

        return !((list == null) || (list.isEmpty()));
    }

    public void pospremiNiz(NizZeroSpanPodataka niz) {
        ProgramMjerenja kljuc = niz.getKljuc();
        if (niz.getPodaci().isEmpty()) {
            return;
        }

        log.log(Level.INFO, "Postaja {0}, komponenta {1}, prvi {2}, zadnj {3}, ukupno {4}",
                new Object[]{kljuc.getPostajaId().getNazivPostaje(),
                    kljuc.getKomponentaId().getFormula(),
                    niz.getPodaci().firstKey(),
                    niz.getPodaci().lastKey(),
                    niz.getPodaci().size()});
        for (Date d : niz.getPodaci().keySet()) {
            ZeroSpan zs = niz.getPodaci().get(d);
            dao.create(zs);
        }
    }

}
