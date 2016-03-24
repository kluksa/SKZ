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

/**
 *
 * @author kraljevic
 */
@Stateless
@LocalBean
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
            if ( ! postajeSve.containsKey(postajaId)) {
                postajeSve.put(postajaId,new PostajaCitacIox(postajaId));
            }
            PostajaCitacIox piox = postajeSve.get(postajaId);
            StringBuilder sb = new StringBuilder();
            sb.append(ipm.getUKljuc().trim());
            sb.append("::");
            sb.append(ipm.getKKljuc().trim());
            log.log(Level.INFO, "KLJUC: {0}, program: {1}", new Object[]{sb.toString(), programMjerenja});
            programKljucevi.put(sb.toString(), programMjerenja);
            piox.dodajProgram(programMjerenja, sb.toString());
        }
        
        for ( PostajaCitacIox pio : postajeSve.values()) {
            log.log(Level.INFO, "CITAM POSTAJU: {0}", pio.getPostaja().getNazivPostaje());
            aktivnaPostaja = pio.getPostaja();
            log.log(Level.INFO, "jedan: {0}", new Date());
            PodatakSirovi zadnji = podatakSiroviFacade.getZadnji(izvor, aktivnaPostaja);
            log.log(Level.INFO, "dva  : {0}", new Date());
            
            if (zadnji == null) {
                Date pocetakMjerenja = programMjerenjaFacade.getPocetakMjerenja(izvor, aktivnaPostaja);
                if (pocetakMjerenja == null) {
                    continue;
                }
                vrijemeZadnjegMjerenja = pocetakMjerenja;
            } else {
                vrijemeZadnjegMjerenja = zadnji.getVrijeme();
            }
//            Date pocetakMjerenja = programMjerenjaFacade.getPocetakMjerenja(izvor, aktivnaPostaja);
//
//            vrijemeZadnjegMjerenja = pocetakMjerenja;

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
                vrijeme = new Date(vrijeme.getTime()+3600000);
                log.log(Level.INFO, "vrijeme={1} URL: {0}", new Object[]{uriStr, vrijeme});
                try {
                    InputStream is = getInputStream(new URI(uriStr));
                    pio.parseMjerenja(new BufferedInputStream(is));
                    is.close();

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
        System.out.println("\nSending 'GET' request to URL : " + uri.toString());
        System.out.println("Response Code : " + responseCode);
        return con.getInputStream();
    }

    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
