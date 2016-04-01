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
package dhz.skz.citaci.iox;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.IzvorProgramKljuceviMap;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.IzvorProgramKljuceviMapFacade;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ProgramUredjajLinkFacade;
import dhz.skz.aqdb.facades.UredjajFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.citaci.iox.validatori.IoxValidatorFactory;
import dhz.skz.validatori.ValidatorFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
public class IoxCitacBean implements CitacIzvora {

    private static final Logger log = Logger.getLogger(IoxCitacBean.class.getName());

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
    private final IoxValidatorFactory ivf = new IoxValidatorFactory();

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
        log.log(Level.INFO, "POCETAK CITANJA");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd%20HH:mm");
        this.izvor = izvor;
//        postaje = new HashSet<Postaja>();
        programKljucevi = new HashMap<>();
//        valFac = new VzKaValidatorFactory(izvor.getProgramMjerenjaCollection());
        HashMap<Postaja, PostajaCitacIox> postajeSve = new HashMap<>();

        for (ProgramMjerenja programMjerenja : izvor.getProgramMjerenjaCollection()) {
            IzvorProgramKljuceviMap ipm = programMjerenja.getIzvorProgramKljuceviMap();
            Postaja postajaId = programMjerenja.getPostajaId();
            if (!postajeSve.containsKey(postajaId)) {
                postajeSve.put(postajaId, new PostajaCitacIox(postajaId, ivf));
            }
            PostajaCitacIox piox = postajeSve.get(postajaId);
            piox.dodajProgram(programMjerenja, ipm.getUKljuc(), ipm.getKKljuc());
        }

        for (PostajaCitacIox pio : postajeSve.values()) {
            log.log(Level.INFO, "CITAM POSTAJU: {0}", pio.getPostaja().getNazivPostaje());
            aktivnaPostaja = pio.getPostaja();
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
            log.log(Level.INFO, "ZADNJI: {0}", vrijemeZadnjegMjerenja);

            String uriStrT = izvor.getUri();
            uriStrT = uriStrT.replaceFirst("\\$\\{HOSTNAME\\}", aktivnaPostaja.getNetAdresa());
            uriStrT = uriStrT.replaceFirst("\\$\\{USERNAME\\}", "horiba");
            uriStrT = uriStrT.replaceFirst("\\$\\{PASSWORD\\}", "password");
            uriStrT = uriStrT.replaceFirst("\\$\\{PERIOD\\}", "1");

            Date sada = new Date();
            Date vrijeme = vrijemeZadnjegMjerenja;
            while (!vrijeme.after(sada)) {
                String uriStr = uriStrT.replaceFirst("\\$\\{POCETAK\\}", sdf.format(vrijeme));
                vrijeme = new Date(vrijeme.getTime() + 3600000);
                log.log(Level.INFO, "vrijeme={1} URL: {0}", new Object[]{uriStr, vrijeme});
                try (InputStream is = getInputStream(new URI(uriStr))) {
                    Map<Date, PodatakSirovi[]> mjerenja = pio.parseMjerenja(new BufferedInputStream(is));
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
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    public void parseZeroSpan(InputStream is) {

    }

    private InputStream getInputStream(URI uri) throws Exception {

//        String url = "http://varazdin/cgi-bin/cgi-iox?proc=60&path=iox/database/av1.txt&unit=2&crosstable=y&time=2016/02/14%2012:00&period=6";
        URL obj = uri.toURL();

        String userInfo = uri.getUserInfo();
//        String name = "horiba";
//        String password = "password";
//        String authString = name + ":" + password;
//        
//        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        byte[] authEncBytes = Base64.getEncoder().encode(userInfo.getBytes());
        String authStringEnc = new String(authEncBytes);

        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", "Basic " + authStringEnc);

        int responseCode = con.getResponseCode();
        log.log(Level.INFO, "Sending 'GET' request to URL : {0}", uri.toString());
        log.log(Level.INFO, "Response Code : {0}", responseCode);
        return con.getInputStream();
    }

    private void spremi(Map<Date, PodatakSirovi[]> mjerenja) {
        UserTransaction utx = context.getUserTransaction();
        for (Date d : mjerenja.keySet()) {
            for (PodatakSirovi ps : mjerenja.get(d)) {

                try {
                    utx.begin();
                    log.log(Level.SEVERE, "SPREMAM VRIJEME: {0}, PROGRAM: {1}", new Object[]{d, ps.getProgramMjerenjaId().getId()});
//                log.log(Level.INFO, "PS={0},{1},{2},{3},{4},{5},{6},{7},{8},{9}",new Object[]{ps.getVrijeme(), ps.getVrijemeUpisa(), ps.getGreska(), ps.getNivoValidacijeId(), ps.getProgramMjerenjaId().getId(),ps.getStatus(), ps.getStatusString(), ps.getVrijednost(), ps.getVrijeme(), ps.getVrijemeUpisa()});
                    podatakSiroviFacade.create(ps);
                    utx.commit();
                } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                    log.log(Level.SEVERE, "IZNIMKA VRIJEME: {0}, PROGRAM: {1}", new Object[]{d, ps.getProgramMjerenjaId().getId()});
                    log.log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}