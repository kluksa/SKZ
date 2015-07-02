/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.ws;

import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.PodatakSirovi;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.facades.PodatakSiroviFacade;
import dhz.skz.aqdb.facades.ZeroSpanFacade;
import dhz.skz.citaci.CsvParser;
import dhz.skz.sirovi.exceptions.CsvPrihvatException;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@WebService(serviceName = "PrihvatPodatakaWS")
@Stateless()
public class PrihvatPodatakaWS {

    @EJB
    private PodatakSiroviFacade podatakSiroviFacade;
    @EJB
    private ZeroSpanFacade zeroSpanFacade;

    public static final Logger log = Logger.getLogger(PrihvatPodatakaWS.class.getName());
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private PostajaFacade postajaFacade;

    @WebMethod(operationName = "prihvatiOmotnicu")
    @Oneway
    public void prihvatiOmotnicu(@WebParam(name = "omotnica") CsvOmotnica omotnica) {
        log.log(Level.INFO, "Poceo  prihvatiOmotnicu : {0}, {1}, {2}, {3} ", new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta()});
        IzvorPodataka izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            String naziv = str + izvor.getBean().trim();
            log.log(Level.FINE, "Bean: {0}", naziv);
            CsvParser parser = (CsvParser) ctx.lookup(naziv);
            parser.prihvati(omotnica);

        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        log.log(Level.INFO, "Zavrsio prihvatiOmotnicu: {0}, {1}, {2}, {3} ", new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta()});
    }


    @WebMethod(operationName = "test")
    public String test(@WebParam(name = "inStr") String inStr) throws CsvPrihvatException {
        if (!inStr.equals("Orao javi se, orao javi se, prijem.")) {
            throw new CsvPrihvatException();
        }
        return "Orao pao, orao pao.";
    }

    @WebMethod(operationName = "getZadnjiZaOmotnicu")
    public long getZadnjiZaOmotnicu(@WebParam(name = "omotnica") final CsvOmotnica omotnica) {
        log.log(Level.INFO, "Poceo getZadnjiZaOmotnicu: {0}, {1}, {2}, {3}", new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta()});

        IzvorPodataka izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        Postaja postaja = postajaFacade.findByNacionalnaOznaka(omotnica.getPostaja());

        Date vrijemeZadnjegPodatka = new Date(0L);
        if (omotnica.getVrsta().compareToIgnoreCase("zero-span") == 0) {
            vrijemeZadnjegPodatka = zeroSpanFacade.getVrijemeZadnjeg(izvor, postaja, omotnica.getDatoteka());
        } else {
            PodatakSirovi zadnji = podatakSiroviFacade.getZadnji(izvor, postaja, omotnica.getDatoteka());
            if (zadnji != null) vrijemeZadnjegPodatka=zadnji.getVrijeme();
        }
        log.log(Level.INFO, "Zadnji podatak: {0},{1},{2}", new Object[]{izvor.getNaziv(), postaja.getNazivPostaje(),vrijemeZadnjegPodatka});

        return vrijemeZadnjegPodatka.getTime();
    }
}
