/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.ws;

import dhz.skz.sirovi.exceptions.CsvPrihvatException;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import dhz.skz.wsbackend.PrihvatSirovihPodatakaRemote;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author kraljevic
 */
@WebService(serviceName = "PrihvatPodatakaWS")
@Stateless()
public class PrihvatPodatakaWS {

    public static final Logger log = Logger.getLogger(PrihvatPodatakaWS.class.getName());
    @EJB
    private PrihvatSirovihPodatakaRemote ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "prihvatiOmotnicu")
    @Oneway
    public void prihvatiOmotnicu(@WebParam(name = "omotnica") CsvOmotnica omotnica) {
        ejbRef.prihvatiOmotnicu(omotnica);
    }

    @WebMethod(operationName = "getUnixTimeZadnjeg")
    public Long getUnixTimeZadnjeg(@WebParam(name = "izvorS") String izvorS, @WebParam(name = "postajaS") String postajaS, @WebParam(name = "datotekaS") String datotekaS) {
        return ejbRef.getUnixTimeZadnjeg(izvorS, postajaS, datotekaS);
    }

    @WebMethod(operationName = "test")
    public String test(@WebParam(name = "inStr") String inStr) throws CsvPrihvatException {
        return ejbRef.test(inStr);
    }

    @WebMethod(operationName = "getZadnjiZaOmotnicu")
    public long getZadnjiZaOmotnicu(@WebParam(name = "omotnica") final CsvOmotnica omotnica) {
        log.log(Level.INFO, "Omotnica od: {0} " , new Object[]{omotnica.getIzvor()});
        log.log(Level.INFO, "Omotnica od: {0} " , new Object[]{ omotnica.getPostaja()});
        log.log(Level.INFO, "Omotnica od: {0} " , new Object[]{ omotnica.getDatoteka()});
        log.log(Level.INFO, "Omotnica od: {0} " , new Object[]{ omotnica.getVrsta()});
        Date vrijemeZadnjeg = ejbRef.getVrijemeZadnjeg(omotnica);
        return vrijemeZadnjeg.getTime();
    }

}
