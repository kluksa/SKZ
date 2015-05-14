/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.ws;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.citaci.CsvParser;
import dhz.skz.sirovi.exceptions.CsvPrihvatException;
import dhz.skz.webservis.omotnica.CsvOmotnica;
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

    public static final Logger log = Logger.getLogger(PrihvatPodatakaWS.class.getName());
    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;
    @EJB
    private PostajaFacade postajaFacade;

    @WebMethod(operationName = "prihvatiOmotnicu")
    @Oneway
    public void prihvatiOmotnicu(@WebParam(name = "omotnica") CsvOmotnica omotnica) {
        log.log(Level.INFO, "Poceo  prihvatiOmotnicu : {0}, {1}, {2}, {3} " , new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta() });
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
        log.log(Level.INFO, "Zavrsio prihvatiOmotnicu: {0}, {1}, {2}, {3} " , new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta() });
    }

    @WebMethod(operationName = "getUnixTimeZadnjeg")
    public Long getUnixTimeZadnjeg(@WebParam(name = "izvorS") String izvorS, @WebParam(name = "postajaS") String postajaS, @WebParam(name = "datotekaS") String datotekaS) {
        log.log(Level.INFO, "Poceo getUnixTimeZadnjeg: {0}, {1}, {2}, {3} " , new Object[]{postajaS, datotekaS });
        IzvorPodataka izvor = izvorPodatakaFacade.findByName(izvorS);
        Postaja postaja = postajaFacade.findByNacionalnaOznaka(postajaS);

        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            String naziv = str + izvor.getBean().trim();
            log.log(Level.FINE, "Bean: {0}", naziv);
            CsvParser parser = (CsvParser) ctx.lookup(naziv);
            log.log(Level.INFO, "Zavrsio getUnixTimeZadnjeg: {0}, {1}, {2}, {3} " , new Object[]{postajaS, datotekaS });
            return parser.getVrijemeZadnjegPodatka(izvor, postaja, datotekaS).getTime();
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return null;
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
        log.log(Level.INFO, "Poceo getZadnjiZaOmotnicu: {0}, {1}, {2}, {3}" , new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta() });
        
        IzvorPodataka izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            String naziv = str + izvor.getBean().trim();
            log.log(Level.FINE, "Bean: {0}", naziv);
            CsvParser parser = (CsvParser) ctx.lookup(naziv);
            log.log(Level.INFO, "Zavrsio getZadnjiZaOmotnicu: {0}, {1}, {2}, {3} " , new Object[]{omotnica.getIzvor(), omotnica.getPostaja(), omotnica.getDatoteka(), omotnica.getVrsta() });

            return parser.getVrijemeZadnjegPodatka(omotnica).getTime();

        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
