/*
 * Copyright (C) 2016 kraljevic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dhz.skz.citaci.Iskaz;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.entity.ZeroSpan;
import dhz.skz.aqdb.facades.IzvorProgramKljuceviMapFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ProgramUredjajLinkFacade;
import dhz.skz.aqdb.facades.UredjajFacade;
import dhz.skz.aqdb.facades.ZadnjiSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.Iskaz.validatori.IskazValidatorFactory;
import dhz.skz.citaci.iox.validatori.IoxValidatorFactory;
import dhz.skz.validatori.Validator;
import dhz.skz.validatori.ValidatorFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
public class IskazCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(IskazCitacBean.class.getName());

    private final TimeZone timeZone = TimeZone.getTimeZone("GMT+1");
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    @EJB
    private UredjajFacade uredjajFacade;

    @EJB
    private ProgramUredjajLinkFacade programUredjajLinkFacade;

    @EJB
    private ProgramMjerenjaFacade programMjerenjaFacade;
    @EJB
    private IzvorProgramKljuceviMapFacade izvorProgramKljuceviMapFacade;
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
    private Collection<ProgramMjerenja> programNaPostaji;
    private Postaja aktivnaPostaja;

    private Date vrijemeZadnjegMjerenja, vrijemeZadnjegZeroSpan;
    private ValidatorFactory valFac;

    private HttpURLConnection con;
    private Set<Postaja> postaje;
    private HashMap<String, ProgramMjerenja> programKljucevi;
    private final IskazValidatorFactory ivf = new IskazValidatorFactory();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd%20HH:mm");

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
        log.log(Level.INFO, "POCETAK CITANJA {0}:{1}" , new Object[]{izvor.getNaziv(), izvor.getBean()});

        this.izvor = izvor;
        programKljucevi = new HashMap<>();
        HashMap<Postaja, PostajaCitacIskaz> postajeSve = new HashMap<>();

        for (ProgramMjerenja programMjerenja : izvor.getProgramMjerenjaCollection()) {
            IzvorProgramKljuceviMap ipm = programMjerenja.getIzvorProgramKljuceviMap();
            if (ipm != null) {
                Postaja postajaId = programMjerenja.getPostajaId();
                if (!postajeSve.containsKey(postajaId)) {
                    postajeSve.put(postajaId, new PostajaCitacIskaz(postajaId, ivf));
                }
                PostajaCitacIskaz piox = postajeSve.get(postajaId);

                piox.dodajProgram(programMjerenja, ipm.getUKljuc(), ipm.getKKljuc());
            }
        }

        for (PostajaCitacIskaz pio : postajeSve.values()) {
            log.log(Level.INFO, "{0}::napraviSatne::CITAM POSTAJU: {1}", new Object[]{izvor.getNaziv(), pio.getPostaja().getNazivPostaje()});
            aktivnaPostaja = pio.getPostaja();
            //            if ( aktivnaPostaja.getNacionalnaOznaka().equals("ZAG001")) {
            if (aktivnaPostaja.getNetAdresa()!=null && !aktivnaPostaja.getNetAdresa().isEmpty()) {
                citajMjerenja(pio);
                citajZeroSpan(pio);
            } else {
                log.log(Level.SEVERE, "{0}::napraviSatne::Postaja {1} nema definiranu adresu", new Object[]{izvor.getNaziv(), aktivnaPostaja.getNazivPostaje()});
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA {0}:{1}" , new Object[]{izvor.getNaziv(), izvor.getBean()});
//        return new AsyncResult<>(true);
        return true;
    }

    private String napraviURL(String datum, String tablica) {
        String uriStrT = izvor.getUri();
        uriStrT = uriStrT.replaceFirst("\\$\\{HOSTNAME\\}", aktivnaPostaja.getNetAdresa());
        uriStrT = uriStrT.replaceFirst("\\$\\{DATUM\\}", datum);
        uriStrT = uriStrT.replaceFirst("\\$\\{TABLICA\\}", tablica);
        uriStrT = uriStrT.replaceFirst("\\$\\{ID\\}", aktivnaPostaja.getRadnaOznaka());
        return uriStrT;
    }

    private void citajMjerenja(PostajaCitacIskaz pio) {
        

        vrijemeZadnjegMjerenja = zadnjiSiroviFacade.getVrijeme(izvor, aktivnaPostaja);
        
        log.log(Level.INFO, "ZADNJI: {0}", vrijemeZadnjegMjerenja);

        Date sada = new Date();
        Date vrijeme = vrijemeZadnjegMjerenja;
        while (!vrijeme.after(sada)) {
            String uriStr = napraviURL(sdf.format(vrijeme), "av1");
            vrijeme = new Date(vrijeme.getTime() + 5*24*3600000);
            log.log(Level.FINE, "vrijeme={1} URL: {0}", new Object[]{uriStr, vrijeme});
            try (InputStream is = getInputStream(new URI(uriStr))) {
                Collection<PodatakSirovi> mjerenja = pio.parseMjerenja(new BufferedInputStream(is));
                spremi(mjerenja);
            } catch (URISyntaxException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    private InputStream getInputStream(URI uri) throws Exception {
        URL obj = uri.toURL();

        String userInfo = uri.getUserInfo();
//        byte[] authEncBytes = Base64.getEncoder().encode(userInfo.getBytes());
//        String authStringEnc = new String(authEncBytes);

        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

//        con.setRequestProperty("Authorization", "Basic " + authStringEnc);
        con.setConnectTimeout(20000);

        int responseCode = con.getResponseCode();
        log.log(Level.FINE, "Sending 'GET' request to URL : {0}", uri.toString());
        log.log(Level.FINE, "Response Code : {0}", responseCode);
        return con.getInputStream();
    }

    private void spremi(Collection<PodatakSirovi> mjerenja) {
        UserTransaction utx = context.getUserTransaction();
        for (PodatakSirovi ps : mjerenja) {

            try {
                utx.begin();
                log.log(Level.FINEST, "SPREMAM VRIJEME: {0}, PROGRAM: {1}", new Object[]{ps.getVrijeme(), ps.getProgramMjerenjaId().getId()});
//                log.log(Level.INFO, "PS={0},{1},{2},{3},{4},{5},{6},{7},{8},{9}",new Object[]{ps.getVrijeme(), ps.getVrijemeUpisa(), ps.getGreska(), ps.getNivoValidacijeId(), ps.getProgramMjerenjaId().getId(),ps.getStatus(), ps.getStatusString(), ps.getVrijednost(), ps.getVrijeme(), ps.getVrijemeUpisa()});
                podatakSiroviFacade.create(ps);
                utx.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                log.log(Level.SEVERE, "IZNIMKA VRIJEME: {0}, PROGRAM: {1}", new Object[]{ps.getVrijeme(), ps.getProgramMjerenjaId().getId()});
                log.log(Level.SEVERE, null, ex);
            }
    }
    }

    private void spremiZS(Collection<ZeroSpan> mjerenja) {
        UserTransaction utx = context.getUserTransaction();
        try {
            utx.begin();
            for (ZeroSpan zs : mjerenja) {
                log.log(Level.INFO, "SPREMAM ZS: t={0}::pm={1}::val={2}::std={3}::ref={4}::vrsta={5}", new Object[]{zs.getVrijeme(), zs.getProgramMjerenjaId().getId(), zs.getVrijednost(), zs.getStdev(), zs.getReferentnaVrijednost(), zs.getVrsta()});
                zeroSpanFacade.create(zs);
            }
            utx.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SortedMap<String, String> opisiStatus(PodatakSirovi ps) {
        
        SortedMap<String,String> mapa = new TreeMap<>();
        IoxValidatorFactory ioxValidatorFactory = new IoxValidatorFactory();
        Validator validator = ioxValidatorFactory.getValidator(ps.getProgramMjerenjaId().getIzvorProgramKljuceviMap().getUKljuc());
        Collection<String> opisStatusa = validator.opisStatusa(ps.getStatusString());
        Integer i=0;
        for(String s: opisStatusa) {
            mapa.put(i.toString(), s);
            i++;
        }
        return mapa;
    }

    private void citajZeroSpan(PostajaCitacIskaz pio) {
        log.log(Level.INFO, "CITAM POSTAJU: {0}", pio.getPostaja().getNazivPostaje());
        Postaja postaja = pio.getPostaja();
        Date zadnji = zeroSpanFacade.getVrijemeZadnjeg(izvor, postaja);

        if (zadnji == null) {
            Date pocetakMjerenja = programMjerenjaFacade.getPocetakMjerenja(izvor, aktivnaPostaja);
            if (pocetakMjerenja == null) {
                return;
            }
            zadnji = pocetakMjerenja;
        }
        log.log(Level.INFO, "ZADNJI ZS: {0}", zadnji);

        Date sada = new Date();
        Date vrijeme = zadnji;
        while (!vrijeme.after(sada)) {
            String uriStr = napraviURL(sdf.format(vrijeme), "cal" );
            vrijeme = new Date(vrijeme.getTime() + 5*24 * 3600000);
            log.log(Level.FINE, "vrijeme={1} URL: {0}", new Object[]{uriStr, vrijeme});
            try (InputStream is = getInputStream(new URI(uriStr))) {
                log.log(Level.FINEST, "PRIJE");
                Collection<ZeroSpan> mjerenja = pio.parseZeroSpan(is);
                log.log(Level.FINEST, "POSLIJE");
                spremiZS(mjerenja);
                log.log(Level.FINEST, "SPREMIO");
            } catch (URISyntaxException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                log.log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

}
