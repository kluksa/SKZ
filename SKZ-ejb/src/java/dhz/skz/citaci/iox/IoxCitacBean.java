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

import com.csvreader.CsvReader;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.entity.ProgramMjerenja;
import dhz.skz.aqdb.facades.PodatakFacade;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.facades.ProgramMjerenjaFacade;
import dhz.skz.aqdb.facades.ProgramUredjajLinkFacade;
import dhz.skz.aqdb.facades.UredjajFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CitacIzvora;
import dhz.skz.validatori.ValidatorFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
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
        this.izvor = izvor;
//        valFac = new VzKaValidatorFactory(izvor.getProgramMjerenjaCollection());
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

//                vrijemeZadnjegZeroSpan = zeroSpanFacade.getVrijemeZadnjeg(izvor, aktivnaPostaja);
//                WlFileParser p = new VzKaMjerenjaParser(aktivnaPostaja, programNaPostaji, timeZone, valFac, podatakSiroviFacade);
//                p.setZadnjiPodatak(vrijemeZadnjegMjerenja);
//                pokupiPodatke("/zapisi/mjerenja", p);
//                p = new VzKaZeroSpanParser(programNaPostaji, timeZone, zeroSpanFacade);
//                p.setZadnjiPodatak(vrijemeZadnjegZeroSpan);
//                pokupiPodatke("/zapisi/zerospan", p);
            } catch (Throwable ex) {
                log.log(Level.SEVERE, "GRESKA KOD POSTAJE {1}:{0}", new Object[]{aktivnaPostaja.getNazivPostaje(), aktivnaPostaja.getId()});
                log.log(Level.SEVERE, "", ex);
            }
        }
        log.log(Level.INFO, "KRAJ CITANJA");
    }

    public void pokupiPodatke() {

    }

    public void parseMjerenja(InputStream is) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line = null;
            line = in.readLine();
            line = in.readLine();
            line = in.readLine();
            line = in.readLine();
            line = in.readLine();
            line = in.readLine();
            line = in.readLine();
            CsvReader csv = new CsvReader(is, '\t', Charset.forName("UTF-8"));
            csv.readHeaders();
            while (csv.readRecord()) {
            
            }

        } catch (IOException ex) {
            Logger.getLogger(IoxCitacBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void parseZeroSpan(InputStream is) {

    }

    private InputStream sendGet() throws Exception {

        String url = "http://varazdin/cgi-bin/cgi-iox?proc=60&path=iox/database/av1.txt&unit=2&crosstable=y&time=2016/02/14%2012:00&period=6";
        URL obj = new URL(url);

        String name = "horiba";
        String password = "password";

        String authString = name + ":" + password;
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Authorization", "Basic " + authStringEnc);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        return con.getInputStream();
    }

    @Override
    public Map<String, String> opisiStatus(PodatakSirovi ps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
