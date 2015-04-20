/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.citaci.weblogger;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.NivoValidacije;
import dhz.skz.aqdb.entity.Podatak;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.NivoValidacijeFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacadeLocal;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.FtpKlijent;
import dhz.skz.citaci.weblogger.exceptions.FtpKlijentException;
import dhz.skz.citaci.weblogger.exceptions.WlFileException;
import dhz.skz.citaci.util.AgregatorPodatka;
import dhz.skz.citaci.weblogger.util.MijesaniProgramiException;
import dhz.skz.citaci.weblogger.util.NizProcitanihWl;
import dhz.skz.citaci.weblogger.util.SatniIterator;
import dhz.skz.validatori.ValidatorFactory;
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
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
public class WebloggerCitacBean implements CitacIzvora {
    @EJB
    private NivoValidacijeFacade nivoValidacijeFacade;

    private static final Logger log = Logger.getLogger(WebloggerCitacBean.class.getName());
    private final TimeZone timeZone = TimeZone.getTimeZone("UTC");
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    @PersistenceContext(unitName = "LIKZ-ejbPU")
    private EntityManager em;

    @EJB
    private ProgramMjerenjaFacadeLocal programMjerenjaFacade;

    @EJB
    private PodatakFacade podatakFacade;

    @EJB
    private ZeroSpanFacade zeroSpanFacade;

    @EJB
    private PostajaFacade posajaFacade;

    @EJB
    private ValidatorFactory validatorFactory;

    private IzvorPodataka izvor;
    private Collection<ProgramMjerenja> programNaPostaji;
    private Postaja aktivnaPostaja;
    private Date vrijemeZadnjegMjerenja, vrijemeZadnjegZeroSpan;

    enum Vrsta {

        MJERENJE, KALIBRACIJA
    }

    public WebloggerCitacBean() {
        formatter.setTimeZone(timeZone);
    }

    @Override
    public void napraviSatne(IzvorPodataka izvor) {
        try {
            log.log(Level.INFO, "POCETAK CITANJA");
            this.izvor = izvor;
            validatorFactory.init(izvor);
            for (Iterator<Postaja> it = posajaFacade.getPostajeZaIzvor(izvor).iterator(); it.hasNext();) {

                aktivnaPostaja = it.next();
                log.log(Level.INFO, "Citam: {0}", aktivnaPostaja.getNazivPostaje());

                Map<ProgramMjerenja, NizProcitanihWl> mapaMjernihNizova = napraviMapuNizova();

                odrediVrijemeZadnjegPodatka(aktivnaPostaja);

                pokupiMjerenja(mapaMjernihNizova);

                obradiISpremi(mapaMjernihNizova);

            }
            log.log(Level.INFO, "KRAJ CITANJA");
        } catch (NamingException ex) {
            Logger.getLogger(WebloggerCitacBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Map<ProgramMjerenja, NizProcitanihWl> napraviMapuNizova() {

        Map<ProgramMjerenja, NizProcitanihWl> tmp = new HashMap<>();
        programNaPostaji = programMjerenjaFacade.find(aktivnaPostaja, izvor);
        for (ProgramMjerenja pm : programNaPostaji) {
            log.log(Level.FINEST, "Program: {0}: {1}", new Object[]{pm.getPostajaId().getNazivPostaje(), pm.getKomponentaId().getFormula()});
            tmp.put(pm, new NizProcitanihWl());
        }
        return tmp;
    }

    private void pokupiMjerenja(Map<ProgramMjerenja, NizProcitanihWl> mapaMjernihNizova) {
        FtpKlijent ftp = new FtpKlijent();

        try {

            ftp.connect(new URI(izvor.getUri()));

            String ptStr = "^(" + Pattern.quote(aktivnaPostaja.getNazivPostaje().toLowerCase()) + ")(_c)?-(\\d{8})(.?)\\.csv";
            Pattern pattern = Pattern.compile(ptStr);
            for (FTPFile file : ftp.getFileList()) {
                Matcher m = pattern.matcher(file.getName().toLowerCase());
                if (m.matches()) {
                    try {
                        Vrsta vrstaDatoteke = odrediVrstuDatoteke(m.group(2));
                        Date terminDatoteke = formatter.parse(m.group(3));

                        if (isDobarTermin(terminDatoteke, vrstaDatoteke)) {

                            WlFileParser citac = napraviParser(vrstaDatoteke);

                            Collection<ProgramMjerenja> aktivniProgram = programMjerenjaFacade.findZaTermin(aktivnaPostaja, izvor, terminDatoteke);

                            citac.setNizKanala(mapaMjernihNizova, aktivniProgram);

                            log.log(Level.INFO, "Datoteka :{0}", file.getName());
                            try (InputStream ifs = ftp.getFileStream(file)) {
                                citac.parse(ifs);
                            } catch (WlFileException ex) {
                                log.log(Level.SEVERE, null, ex);
                            } catch (Exception ex) { // kakva god da se iznimka dogodi, nastavljamo
                                log.log(Level.SEVERE, null, ex);
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
        Podatak zadnjiP = podatakFacade.getZadnji(izvor, p);
        if (zadnjiP != null) {
            vrijemeZadnjegMjerenja = zadnjiP.getVrijeme();
        } else {
            vrijemeZadnjegMjerenja = null;
        }
        vrijemeZadnjegZeroSpan = zeroSpanFacade.getVrijemeZadnjeg(izvor, p);
    }

    private void obradiISpremi(Map<ProgramMjerenja, NizProcitanihWl> mapaMjernihNizova) {
        
        NivoValidacije nv = nivoValidacijeFacade.find(0);
        for (ProgramMjerenja program : mapaMjernihNizova.keySet()) {
            int ocekivaniBroj = 60;

            NizProcitanihWl niz = mapaMjernihNizova.get(program);

            if (!niz.getPodaci().isEmpty()) {
                NavigableMap<Date, PodatakSirovi> podaci = niz.getPodaci();

                SatniIterator sat = new SatniIterator(niz.getPodaci().firstKey(), niz.getPodaci().lastKey());
                Date po = sat.getVrijeme();
                while (sat.next()) {
                    Date kr = sat.getVrijeme();
                    AgregatorPodatka agregator = new AgregatorPodatka(nv, ocekivaniBroj);
                    NavigableMap<Date, PodatakSirovi> podmapa = podaci.subMap(po, false, kr, true);
                    try {
                        agregator.agregiraj(podmapa.values(), kr);
                        if (agregator.hasPodatak()) {
                            podatakFacade.create(agregator.getPodatak());
                        }
                        if (agregator.hasZero()) {
                            zeroSpanFacade.create(agregator.getZero());
                        }
                        if (agregator.hasSpan()) {
                            zeroSpanFacade.create(agregator.getSpan());
                        }
                    } catch (MijesaniProgramiException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                    po = kr;
                }
            }
            log.log(Level.INFO, "Pospremam Postaja {0}, komponenta {1}", new Object[]{program.getPostajaId().getNazivPostaje(), program.getKomponentaId().getFormula()});
        }
    }

    private boolean isDobarTermin(Date t, Vrsta vrsta) {
        if (vrsta == Vrsta.MJERENJE) {
            return isDobarTerminMjerenje(t);
        } else {
            return isDobarTerminZeroSpan(t);
        }
    }

    private boolean isDobarTerminMjerenje(Date t) {
        boolean aktivna = false;
        boolean dobroVrijeme;
        for (ProgramMjerenja pm : programNaPostaji) {
            if (pm.getIzvorProgramKljuceviMap() != null) {
                boolean a = t.compareTo(pm.getPocetakMjerenja()) >= 0;
                boolean b = (pm.getZavrsetakMjerenja() == null) || (t.compareTo(pm.getZavrsetakMjerenja()) <= 0);
                aktivna |= (a && b);

//                aktivna |= t.compareTo(pm.getPocetakMjerenja()) >= 0
//                    && ((pm.getZavrsetakMjerenja() == null) || (t.compareTo(pm.getZavrsetakMjerenja()) <= 0));
            }
        }
        dobroVrijeme = (vrijemeZadnjegMjerenja == null) || (vrijemeZadnjegMjerenja.getTime() - t.getTime() < 24 * 3600 * 1000);
        return aktivna && dobroVrijeme;
    }

    private boolean isDobarTerminZeroSpan(Date t) {
        boolean aktivna = false;
        boolean dobroVrijeme;
        for (ProgramMjerenja pm : programNaPostaji) {
            if ((pm.getIzvorProgramKljuceviMap() != null) && (pm.getIzvorProgramKljuceviMap().getUKljuc() != null)) {
                boolean a = t.compareTo(pm.getPocetakMjerenja()) >= 0;
                boolean b = (pm.getZavrsetakMjerenja() == null) || (t.compareTo(pm.getZavrsetakMjerenja()) <= 0);
                aktivna |= (a && b);

//                aktivna |= t.compareTo(pm.getPocetakMjerenja()) >= 0
//                    && ((pm.getZavrsetakMjerenja() == null) || (t.compareTo(pm.getZavrsetakMjerenja()) <= 0));
            }
        }
        dobroVrijeme = (vrijemeZadnjegZeroSpan == null) || (vrijemeZadnjegZeroSpan.getTime() - t.getTime() < 24 * 3600 * 1000);
        return aktivna && dobroVrijeme;
    }

    private WlFileParser napraviParser(Vrsta vrstaDatoteke) {
        WlFileParser parser;
        if (vrstaDatoteke == Vrsta.MJERENJE) {
            parser = new WlMjerenjaParser(timeZone, validatorFactory);
            parser.setZadnjiPodatak(vrijemeZadnjegMjerenja);
        } else {
            parser = new WlZeroSpanParser(timeZone);
            parser.setZadnjiPodatak(vrijemeZadnjegMjerenja);
        }

        return parser;
    }

    private Vrsta odrediVrstuDatoteke(String group) {
        if (group == null || group.isEmpty()) {
            return Vrsta.MJERENJE;
        } else {
            return Vrsta.KALIBRACIJA;
        }
    }

    @Override
    public Collection<PodatakSirovi> dohvatiSirove(ProgramMjerenja program, Date pocetak, Date kraj, boolean p, boolean k) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void procitaj(IzvorPodataka izvor, Map<ProgramMjerenja, Podatak> pocetak) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private Date getVrijemePocetka(Postaja p) {
//        Date zadnji;
//        Podatak zadnjiP = dao.getZadnji(izvor, p);
//        if (zadnjiP != null) {
//            zadnji = zadnjiP.getVrijeme();
//        } else {
//            zadnji = programMjerenjaFacade.getPocetakMjerenja(izvor, p);
//        }
//
////        Collection<ProgramMjerenja> programi = izvorPodatakaFacade.getProgram(p, izvor);
////        Date zadnji = new Date(0L);
////        for (ProgramMjerenja pm : programi) {
////            Date tmp;
////            if (pm.getZavrsetakMjerenja() == null) {
////                tmp = dao.getZadnjiPodatak(pm);
////
////            } else {
////                tmp = pm.getZavrsetakMjerenja();
////            }
////            if (zadnji.before(tmp)) {
////                zadnji = tmp;
////            }
////
////        }
//        log.log(Level.INFO, "Zadnji podatak na {0} u {1}", new Object[]{p.getNazivPostaje(), zadnji});
//        return zadnji;
//    }
//    private NavigableMap<Date, Uredjaj> getUredjajiMapa(ProgramMjerenja pm) {
//        NavigableMap<Date, Uredjaj> mapa = new TreeMap<>();
//        for (ProgramUredjajLink pul : pm.getProgramUredjajLinkCollection()) {
//            mapa.put(pul.getVrijemePostavljanja(), pul.getUredjajId());
//        }
//        return mapa;
//    }
//    private Map<ProgramMjerenja, NizZeroSpanPodataka> getMapaZeroSpanNizova() {
//        Map<ProgramMjerenja, NizZeroSpanPodataka> tmp = new HashMap<>();
//        for (ProgramMjerenja pm : programNaPostaji) {
//            NizZeroSpanPodataka np = new NizZeroSpanPodataka();
//            np.setKljuc(pm);
//            tmp.put(pm, np);
//        }
//        return tmp;
//    }
}
