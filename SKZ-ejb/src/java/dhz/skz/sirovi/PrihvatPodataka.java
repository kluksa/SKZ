/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.sirovi;

import dhz.skz.aqdb.entity.IzvorPodataka;
import dhz.skz.aqdb.entity.Postaja;
import dhz.skz.aqdb.facades.IzvorPodatakaFacade;
import dhz.skz.aqdb.facades.PostajaFacade;
import dhz.skz.citaci.CsvParser;
import dhz.skz.sirovi.exceptions.CsvPrihvatException;
import dhz.skz.webservis.omotnica.CsvOmotnica;
import dhz.skz.wsbackend.PrihvatSirovihPodatakaRemote;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author kraljevic
 */
@Stateless
public class PrihvatPodataka implements PrihvatSirovihPodatakaRemote {

    @EJB
    private PostajaFacade postajaFacade;

    public static final Logger log = Logger.getLogger(PrihvatPodataka.class.getName());

    @EJB
    private IzvorPodatakaFacade izvorPodatakaFacade;

    @Override
    public void prihvatiOmotnicu(final CsvOmotnica omotnica) {
//        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        String fname = df.format(new Date());
//        fname += ".dat";
//        try {
//            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fname))) {
//                os.writeObject(omotnica);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(PrihvatPodataka.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(PrihvatPodataka.class.getName()).log(Level.SEVERE, null, ex);
//        }
        IzvorPodataka izvor = izvorPodatakaFacade.findByName(omotnica.getIzvor());
        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            String naziv = str + izvor.getBean().trim();
            log.log(Level.FINE, "Bean: {0}", naziv);
            CsvParser parser = (CsvParser) ctx.lookup(naziv);
            parser.obradi(omotnica);

        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long getUnixTimeZadnjeg(String izvorS, String postajaS, String datotekaS) {
        IzvorPodataka izvor = izvorPodatakaFacade.findByName(izvorS);
        Postaja postaja = postajaFacade.findByNacionalnaOznaka(postajaS);

        try {
            InitialContext ctx = new InitialContext();
            String str = "java:module/";

            String naziv = str + izvor.getBean().trim();
            log.log(Level.FINE, "Bean: {0}", naziv);
            CsvParser parser = (CsvParser) ctx.lookup(naziv);
            return parser.getVrijemeZadnjegPodatka(izvor, postaja, datotekaS).getTime();
        } catch (NamingException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String test(String inStr) throws CsvPrihvatException {
        if (!inStr.equals("Orao javi se, orao javi se, prijem.")) {
            throw new CsvPrihvatException();
        }
        return "Orao pao, orao pao.";
    }

}
